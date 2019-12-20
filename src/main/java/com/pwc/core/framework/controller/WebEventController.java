package com.pwc.core.framework.controller;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.driver.MicroserviceAndroidDriver;
import com.pwc.core.framework.driver.MicroserviceChromeDriver;
import com.pwc.core.framework.driver.MicroserviceEdgeDriver;
import com.pwc.core.framework.driver.MicroserviceFirefoxDriver;
import com.pwc.core.framework.driver.MicroserviceIOSDriver;
import com.pwc.core.framework.driver.MicroserviceInternetExplorerDriver;
import com.pwc.core.framework.driver.MicroserviceRemoteWebDriver;
import com.pwc.core.framework.driver.MicroserviceSafariDriver;
import com.pwc.core.framework.driver.MicroserviceWebDriver;
import com.pwc.core.framework.processors.web.KeyboardActivityProcessor;
import com.pwc.core.framework.processors.web.MouseActivityProcessor;
import com.pwc.core.framework.processors.web.ViewActivityProcessor;
import com.pwc.core.framework.service.WebEventService;
import com.pwc.core.framework.util.DebuggingUtils;
import com.pwc.core.framework.util.GridUtils;
import com.pwc.core.framework.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.pwc.logging.service.LoggerService.LOG;


@Component
public class WebEventController {

    private static final String WINDOWS_OS = "windows";
    private static final String LINUX_OS = "linux";

    @Value("${web.url}")
    private String webUrl;

    @Value("${grid.enabled:true}")
    private boolean gridEnabled;

    @Value("${grid.hub.url}")
    private String gridUrl;

    @Value("${enable.siteMinder:false}")
    private boolean siteMinderEnabled;

    @Value("${capture.video:false}")
    private boolean videoCaptureEnabled;

    @Value("${siteminder.open.url}")
    private String siteMinderOpenUrl;

    @Value("${default.wait.for.sleep.millis:1000}")
    public long defaultWaitForSleepDurationInMillis;

    @Value("${element.wait.timeout.seconds:180}")
    public long defaultWaitForElementTimeoutInSeconds;

    @Value("${browser.wait.timeout.seconds:10}")
    public long defaultWaitForPageTimeoutInSeconds;

    @Value("${enable.ajax.requests.waiting:true}")
    private boolean waitForAjaxRequestsEnabled;

    @Value("${browserstack.username}")
    private String browserstackUser;

    @Value("${browserstack.accesskey}")
    private String browserstackAccesskey;

    @Value("${browserstack.local:true}")
    private String browserstackLocal;

    @Value("${experitest.accesskey}")
    private String experitestAccesskey;

    private MicroserviceWebDriver remoteWebDriver;
    private WebEventService webEventService;
    private DesiredCapabilities capabilities;
    private String currentTestName;
    private String currentJobId;

    /**
     * Start and configure browser and WebDriver instance
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
                case FrameworkConstants.HEADLESS_FIREFOX_BROWSER_MODE: {
                    this.remoteWebDriver = getHeadlessFirefoxBrowser();
                    break;
                }
                case FrameworkConstants.CHROME_BROWSER_MODE: {
                    this.remoteWebDriver = getChromeBrowser();
                    break;
                }
                case FrameworkConstants.HEADLESS_CHROME_BROWSER_MODE: {
                    this.remoteWebDriver = getHeadlessChromeBrowser();
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
                case FrameworkConstants.PHANTOM_JS_BROWSER_MODE: {
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

            try {
                int width = Integer.parseInt(StringUtils.substringBefore(capabilities.getCapability(FrameworkConstants.SCREEN_RESOLUTION_CAPABILITY).toString(), "x"));
                int height = Integer.parseInt(StringUtils.substringAfter(capabilities.getCapability(FrameworkConstants.SCREEN_RESOLUTION_CAPABILITY).toString(), "x"));
                this.remoteWebDriver.manage().window().setSize(new Dimension(width, height));
            } catch (Exception e) {
                LOG(false, "unable to set window size");
            }

            webEventService.setTimeOutInSeconds(defaultWaitForElementTimeoutInSeconds);
            webEventService.setSleepInMillis(defaultWaitForSleepDurationInMillis);
            webEventService.setPageTimeoutInSeconds(defaultWaitForPageTimeoutInSeconds);
            webEventService.setVideoCaptureEnabled(videoCaptureEnabled);
            webEventService.setWaitForAjaxRequestsEnabled(waitForAjaxRequestsEnabled);

            webEventService.redirectToUrl(webUrl);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to initialize a valid browser for the RemoteWebDriver", e);
        }
    }

    /**
     * Check if Browser Stack is being used by the downstream user based on the username and password being defined.
     *
     * @return browser stack enabled | disabled flag
     */
    private boolean isBrowserStackEnabled() {

        return StringUtils.isNotEmpty(browserstackUser)
                && StringUtils.isNotEmpty(browserstackAccesskey);
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

        } else if (isBrowserStackEnabled()) {

            GridUtils.initBrowserType();

            capabilities.setCapability(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_BROWSER_VERSION_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_BROWSER_VERSION_PROPERTY)));
            if (StringUtils.isEmpty(System.getProperty(FrameworkConstants.BROWSER_STACK_OS_PROPERTY))) {
                capabilities.setCapability(FrameworkConstants.BROWSER_STACK_OS_PROPERTY, WINDOWS_OS);
            } else {
                capabilities.setCapability(FrameworkConstants.BROWSER_STACK_OS_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_OS_PROPERTY)));
            }
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_OS_VERSION_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_OS_VERSION_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_RESOLUTION_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_RESOLUTION_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_LOCAL_PROPERTY, browserstackLocal);
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_LOCAL_IDENTIFIER_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_LOCAL_IDENTIFIER_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_PROJECT_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_PROJECT_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_BUILD_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_BUILD_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_NAME_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_TEST_RUN_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_TEST_RUN_NAME_PROPERTY)));
            capabilities.setCapability(FrameworkConstants.BROWSER_STACK_TIMEZONE_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_TIMEZONE_PROPERTY)));

            LOG(true, "Initiating BrowserStack test execution with browser='%s', platform='%s'",
                    capabilities.getCapability(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY),
                    capabilities.getCapability(FrameworkConstants.BROWSER_STACK_OS_PROPERTY));

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
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getIOSBrowser() throws Exception {
        LOG("starting iOS browser");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.SAFARI);
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceIOSDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Firefox Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getFirefoxBrowser() throws MalformedURLException {

        LOG("starting firefox browser");
        setDriverExecutable();
        capabilities.setCapability("marionette", true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceFirefoxDriver());
            }
        }
        return null;
    }

    /**
     * Get Headless Firefox Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getHeadlessFirefoxBrowser() throws MalformedURLException {

        LOG("starting headless firefox browser");
        setDriverExecutable();
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.addArguments("window-size=1920,1080");
        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
        capabilities.setCapability("marionette", true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
        capabilities.setCapability("video", "True");

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceFirefoxDriver(firefoxOptions));
            }
        }
        return null;

    }

    /**
     * Get a group of SauceLabs generic Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getAnyBrowser() throws MalformedURLException {

        LOG("starting sauce labs browser(s)");
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        }
        return null;
    }

    /**
     * Get Chrome Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getChromeBrowser() throws MalformedURLException {

        LOG("starting chrome browser");
        setDriverExecutable();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-running-insecure-content");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
        capabilities.setCapability("video", "True");
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceChromeDriver(capabilities));
            }
        }

        return null;
    }

    /**
     * Get Headless Chrome Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getHeadlessChromeBrowser() throws MalformedURLException {

        LOG("starting headless chrome browser");
        setDriverExecutable();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1920,1080");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
        capabilities.setCapability("video", "True");
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceChromeDriver(capabilities));
            }
        }

        return null;
    }

    /**
     * Get Android Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getAndroidBrowser() throws Exception {

        LOG("starting android browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.ANDROID);
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }
        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceAndroidDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Microsoft Edge Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getEdgeBrowser() throws MalformedURLException {

        LOG("starting microsoft edge browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.EDGE);
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceEdgeDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Internet Explorer Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getInternetExplorerBrowser() throws MalformedURLException {

        LOG("starting internet explorer browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceInternetExplorerDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Safari Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getSafariBrowser() throws MalformedURLException {

        LOG("starting safari browser");
        setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.SAFARI);
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                return (new MicroserviceSafariDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get PhantomJS Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws MalformedURLException url exception
     */
    public MicroserviceWebDriver getPhantomJsBrowser() throws MalformedURLException {

        LOG("starting PhantomJS virtual browser");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.PHANTOMJS);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, setDriverExecutable());
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
                "--web-security=false",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true",
                "--webdriver-loglevel=DEBUG"});
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }

        if (gridEnabled) {
            if (this.remoteWebDriver == null) {
                MicroserviceRemoteWebDriver microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), capabilities);
                microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteWebDriver;
            }
        } else {
            if (this.remoteWebDriver == null) {
                Assert.fail("Local PhantomJS Execution Not Yet Supported. Please use the GRID for this driver!");
                //this.remoteWebDriver = (new MicroservicePhantomJsDriver(capabilities));
                //this.remoteWebDriver = (MicroserviceWebDriver) new PhantomJSDriver(capabilities);
            }
        }
        return null;
    }

    /**
     * Set the resources path to the WebDriver executable depending on the ENV the scripts
     * are running on
     *
     * @return driver executable file path
     */
    protected String setDriverExecutable() {

        final String DESIRED_BROWSER = StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), "");
        File executable;

        switch (DESIRED_BROWSER) {
            case FrameworkConstants.CHROME_BROWSER_MODE: {
                if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), WINDOWS_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("chrome_win.exe");
                } else if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), LINUX_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("chrome_linux_64");
                } else {
                    executable = PropertiesUtils.getFirstFileFromTestResources("chrome_mac");
                }
                System.setProperty(FrameworkConstants.WEB_DRIVER_CHROME, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.HEADLESS_CHROME_BROWSER_MODE: {
                if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), WINDOWS_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("chrome_win.exe");
                } else if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), LINUX_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("chrome_linux_64");
                } else {
                    executable = PropertiesUtils.getFirstFileFromTestResources("chrome_mac");
                }
                System.setProperty(FrameworkConstants.WEB_DRIVER_CHROME, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.FIREFOX_BROWSER_MODE: {
                if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), WINDOWS_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver.exe");
                } else if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), LINUX_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver_linux_64");
                } else {
                    executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver_mac");
                }
                System.setProperty(FrameworkConstants.WEB_DRIVER_GECKO, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.HEADLESS_FIREFOX_BROWSER_MODE: {
                if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), WINDOWS_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver.exe");
                } else if (StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), LINUX_OS)) {
                    executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver_linux_64");
                } else {
                    executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver_mac");
                }
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
            case FrameworkConstants.EDGE_BROWSER_MODE: {
                executable = PropertiesUtils.getFirstFileFromTestResources("edge.exe");
                System.setProperty(FrameworkConstants.WEB_DRIVER_EDGE, PropertiesUtils.getPath(executable));
                break;
            }
            case FrameworkConstants.PHANTOM_JS_BROWSER_MODE: {

                if (StringUtils.isNotEmpty(System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY))) {
                    return System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY);
                } else {
                    executable = StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), WINDOWS_OS) ?
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
     * @return time in milliseconds for Mouse-specific web event to execute
     */
    public long webAction(final WebElement webElement, final Object webElementValue) {

        if (videoCaptureEnabled) {
            DebuggingUtils.takeScreenShot(remoteWebDriver);
        }
        if (MouseActivityProcessor.applies(webElement)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            MouseActivityProcessor.getInstance().webAction(webElement, webElementValue);
            getWebEventService().waitForBrowserToLoad();
            stopWatch.stop();
            return stopWatch.getTotalTimeMillis();
        } else if (KeyboardActivityProcessor.applies(webElement)) {
            KeyboardActivityProcessor.getInstance().webAction(webElement, webElementValue);
            getWebEventService().waitForBrowserToLoad();
        } else if (ViewActivityProcessor.applies(webElement)) {
            ViewActivityProcessor.getInstance().webAction(webElement, webElementValue);
        }
        if (videoCaptureEnabled) {
            DebuggingUtils.takeScreenShot(remoteWebDriver);
        }
        return 0L;
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

    public void setRemoteWebDriver(MicroserviceWebDriver remoteWebDriver) {
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

    public boolean isVideoCaptureEnabled() {
        return videoCaptureEnabled;
    }

    public void setVideoCaptureEnabled(boolean videoCaptureEnabled) {
        this.videoCaptureEnabled = videoCaptureEnabled;
    }

}
