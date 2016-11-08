package qcom.itlegal.ipit.framework.controller;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.Reporter;
import qcom.itlegal.ipit.framework.FrameworkConstants;
import qcom.itlegal.ipit.framework.data.Credentials;
import qcom.itlegal.ipit.framework.driver.*;
import qcom.itlegal.ipit.framework.processors.web.KeyboardActivityProcessor;
import qcom.itlegal.ipit.framework.processors.web.MouseActivityProcessor;
import qcom.itlegal.ipit.framework.processors.web.ViewActivityProcessor;
import qcom.itlegal.ipit.framework.service.WebEventService;
import qcom.itlegal.ipit.framework.util.GridUtils;
import qcom.itlegal.ipit.framework.util.PropertiesUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;

@Component
public class WebEventController {

    @Value("${web.url}")
    private String webUrl;

    @Value("${grid.enabled:true}")
    private boolean gridEnabled;

    @Value("${grid.hub.url}")
    private String gridUrl;

    @Value("${enable.siteMinder:false}")
    private boolean siteMinderEnabled;

    @Value("${siteminder.open.url}")
    private String siteMinderOpenUrl;

    @Value("${default.wait.for.sleep.millis:1000}")
    public long defaultWaitForSleepDurationInMillis;

    @Value("${element.wait.timeout.seconds:180}")
    public long defaultWaitForElementTimeoutInSeconds;

    @Value("${browser.wait.timeout.seconds:10}")
    public long defaultWaitForPageTimeoutInSeconds;

    private QualcommWebDriver remoteWebDriver;
    private WebEventService webEventService;
    private DesiredCapabilities capabilities;
    private String currentTestName;
    private String currentJobId;

    /**
     * Start and configure browser & WebDriver instance
     *
     * @param credentials <code>Credentials</code> user of SiteMinder testing if needed
     */
    public void initiateBrowser(final Credentials credentials) {
        try {

            setDefaultDesiredCapabilities();

            switch (System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)) {
                case FrameworkConstants.FIREFOX_BROWSER_MODE: {
                    this.remoteWebDriver = getFirefoxBrowser();
                    break;
                }
                case FrameworkConstants.CHROME_BROWSER_MODE: {
                    this.remoteWebDriver = getChromeBrowser();
                    break;
                }
                case FrameworkConstants.ANDROID_MODE: {
                    this.remoteWebDriver = getAndroidBrowser();
                    break;
                }
                case FrameworkConstants.IOS_MODE: {
                    this.remoteWebDriver = getIOSBrowser();
                    break;
                }
                case FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE: {
                    this.remoteWebDriver = getInternetExplorerBrowser();
                    break;
                }
                case FrameworkConstants.EDGE_BROWSER_MODE: {
                    this.remoteWebDriver = getEdgeBrowser();
                    break;
                }
                case FrameworkConstants.SAFARI_BROWSER_MODE: {
                    this.remoteWebDriver = getSafariBrowser();
                    break;
                }
                case FrameworkConstants.PHANTOMJS_BROWSER_MODE: {
                    this.remoteWebDriver = getPhantomJsBrowser();
                    break;
                }
                default: {
                    this.remoteWebDriver = getAnyBrowser();
                }
            }

            currentJobId = ((RemoteWebDriver) this.remoteWebDriver).getSessionId().toString();

            webEventService = new WebEventService(remoteWebDriver);
            if (isSiteMinderEnabled()) {
                webEventService.authenticateSiteMinder(webUrl, credentials, siteMinderOpenUrl, false);
            }

            webEventService.setTimeOutInSeconds(defaultWaitForElementTimeoutInSeconds);
            webEventService.setSleepInMillis(defaultWaitForSleepDurationInMillis);
            webEventService.setPageTimeoutInSeconds(defaultWaitForPageTimeoutInSeconds);

            webEventService.redirectToUrl(webUrl);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to initialize a valid browser for the RemoteWebDriver", e);
        }
    }

    /**
     * Set all browser based runtime capabilities for:
     * - browser type
     * - browser version
     * - screen resolution
     * - platform (OS)
     */
    protected void setDefaultDesiredCapabilities() {

        constructTestName();

        capabilities = new DesiredCapabilities();

        if (!StringUtils.isEmpty(System.getenv(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY)) &&
                !StringUtils.isEmpty(System.getenv(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY)) &&
                !StringUtils.isEmpty(System.getenv(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY))) {

            LOG(true, "Initiating Sauce-OnDemand test execution with browser='%s', version='%s', platform='%s'",
                    System.getenv(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY), System.getenv(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY), System.getenv(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY));

            capabilities.setBrowserName(System.getenv(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY));
            capabilities.setCapability(CapabilityType.VERSION, System.getenv(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY));
            capabilities.setCapability(CapabilityType.PLATFORM, System.getenv(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY));
            capabilities.setCapability(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY)));

            System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, System.getenv(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY));

        } else {
            LOG(true, "Initiating User Defined test execution");
            GridUtils.initBrowserType();
            capabilities.setCapability(CapabilityType.PLATFORM, GridUtils.initPlatformType());
            capabilities.setCapability(CapabilityType.VERSION, GridUtils.initBrowserVersion());
            capabilities.setCapability(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY)));
        }

        if (!StringUtils.isEmpty(System.getenv(FrameworkConstants.SAUCELABS_TUNNEL_IDENTIFIER_PROPERTY))) {
            capabilities.setCapability(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, System.getenv(FrameworkConstants.SAUCELABS_TUNNEL_IDENTIFIER_PROPERTY));
        }

        capabilities.setCapability(FrameworkConstants.TIME_ZONE_CAPABILITY, GridUtils.initTimeZone());
        capabilities.setCapability(FrameworkConstants.SCREEN_RESOLUTION_CAPABILITY, GridUtils.initBrowserResolution());
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        capabilities.setCapability(FrameworkConstants.SCRIPT_NAME_CAPABILITY, getReadableTestName());

    }

    /**
     * Get iOS Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getIOSBrowser() throws Exception {
        LOG("starting iOS browser");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.SAFARI);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommIOSDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Firefox Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getFirefoxBrowser() throws MalformedURLException {
        LOG("starting firefox browser");
        setDriverExecutable();
        //ToDo: awaiting fix for Firefox 'Certificate is not secure'
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("log", "{level: info}");
        capabilities.setCapability("moz:firefoxOptions", options);
        capabilities.setCapability("marionette", true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommFirefoxDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get a group of SauceLabs generic Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getAnyBrowser() throws MalformedURLException {
        LOG("starting sauce labs browser(s)");
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        }
        return null;
    }

    /**
     * Get Chrome Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getChromeBrowser() throws MalformedURLException {
        LOG("starting chrome browser");
        setDriverExecutable();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("ignore-certificate-errors");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-extensions");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommChromeDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Android Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getAndroidBrowser() throws Exception {
        LOG("starting android browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.ANDROID);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommAndroidDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Microsoft Edge Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getEdgeBrowser() throws MalformedURLException {
        LOG("starting microsoft edge browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.EDGE);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommEdgeDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Internet Explorer Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getInternetExplorerBrowser() throws MalformedURLException {
        LOG("starting internet explorer browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommInternetExplorerDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Safari Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getSafariBrowser() throws MalformedURLException {
        LOG("starting safari browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.SAFARI);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new QualcommSafariDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get PhantomJS Web Driver for local or RemoteWebDriver capability
     *
     * @return QualcommWebDriver instance
     * @throws MalformedURLException
     */
    public QualcommWebDriver getPhantomJsBrowser() throws MalformedURLException {
        LOG("starting PhantomJS virtual browser");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.PHANTOMJS);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, setDriverExecutable());
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
                "--web-security=false",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true",
                "--webdriver-loglevel=DEBUG"});
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                QualcommRemoteWebDriver qualcommRemoteWebDriver = new QualcommRemoteWebDriver(new URL(gridUrl), capabilities);
                qualcommRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return qualcommRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                Assert.fail("Local PhantomJS Execution Not Yet Supported. Please use the GRID for this driver!");
                //this.remoteWebDriver = (new QualcommPhantomJsDriver(capabilities));
                //this.remoteWebDriver = (QualcommWebDriver) new PhantomJSDriver(capabilities);
            }
        }
        return null;
    }

    /**
     * Set the resources path to the WebDriver executable depending on the ENV the scripts
     * are running on
     */
    public String setDriverExecutable() {

        final String DESIRED_BROWSER = StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), "");
        File executable;

        switch (DESIRED_BROWSER) {
            case FrameworkConstants.CHROME_BROWSER_MODE: {
                executable = StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), "windows") ?
                        PropertiesUtils.getFirstFileFromTestResources("chrome_win.exe") :
                        PropertiesUtils.getFirstFileFromTestResources("chrome_mac");
                System.setProperty(FrameworkConstants.WEB_DRIVER_CHROME, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.FIREFOX_BROWSER_MODE: {
                executable = StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), "windows") ?
                        PropertiesUtils.getFirstFileFromTestResources("geckodriver.exe") :
                        PropertiesUtils.getFirstFileFromTestResources("geckodriver");
                System.setProperty(FrameworkConstants.WEB_DRIVER_GECKO, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE: {
                executable = StringUtils.equals(System.getProperty(FrameworkConstants.SYSTEM_JVM_TYPE), "32") ?
                        PropertiesUtils.getFirstFileFromTestResources("ie_win32.exe") :
                        PropertiesUtils.getFirstFileFromTestResources("ie_win64.exe");
                System.setProperty(FrameworkConstants.WEB_DRIVER_IE, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.PHANTOMJS_BROWSER_MODE: {

                if (StringUtils.isNotEmpty(System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY))) {
                    return System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY);
                } else {
                    executable = StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), "windows") ?
                            PropertiesUtils.getFirstFileFromTestResources("phantomjs.exe") :
                            PropertiesUtils.getFirstFileFromTestResources("phantomjs");
                    System.setProperty(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY, PropertiesUtils.getPath(executable));
                    System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PropertiesUtils.getPath(executable));
                    return PropertiesUtils.getPath(executable);
                }
            }

        }
        return "";
    }

    /**
     * Core web element action sorting logic
     *
     * @param webElement      DOM element to act upon
     * @param webElementValue DOM element value to alter
     */
    public void webAction(final WebElement webElement, final Object webElementValue) {
        if (MouseActivityProcessor.applies(webElement)) {
            MouseActivityProcessor.getInstance().webAction(webElement, webElementValue);
        } else if (KeyboardActivityProcessor.applies(webElement)) {
            KeyboardActivityProcessor.getInstance().webAction(webElement, webElementValue);
        } else if (ViewActivityProcessor.applies(webElement)) {
            ViewActivityProcessor.getInstance().webAction(webElement, webElementValue);
        }
    }

    /**
     * Build current test name from TestNG Reporter
     */
    private void constructTestName() {
        try {
            if (Reporter.getCurrentTestResult() != null) {
                this.currentTestName = StringUtils.substringAfterLast(Reporter.getCurrentTestResult().getTestClass().getName(), ".");
            } else {
                this.currentTestName = StringUtils.appendIfMissing(StringUtils.removeStart(
                        currentTestName, "test"), "Test");
            }
        } catch (Exception e) {
            this.currentTestName = "";
        }
    }

    /**
     * Quit the Selenium browser
     */
    public void performQuit() {
        try {
            this.remoteWebDriver.quit();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public WebEventService getWebEventService() {
        return webEventService;
    }

    public void setWebEventService(WebEventService service) {
        this.webEventService = service;
    }

    public void setRemoteWebDriver(QualcommWebDriver remoteWebDriver) {
        this.remoteWebDriver = remoteWebDriver;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
    }

    public void setGridUrl(String gridUrl) {
        this.gridUrl = gridUrl;
    }

    public void setSiteMinderEnabled(boolean enabled) {
        this.siteMinderEnabled = enabled;
    }

    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(DesiredCapabilities desiredCapabilities) {
        this.capabilities = desiredCapabilities;
    }

    public boolean isSiteMinderEnabled() {
        return siteMinderEnabled;
    }

    public String getReadableTestName() {
        return this.currentTestName;
    }

    public void setReadableTestName(String name) {
        this.currentTestName = name;
    }

    public String getCurrentJobId() {
        return currentJobId;
    }

}