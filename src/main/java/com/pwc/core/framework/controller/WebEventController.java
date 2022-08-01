package com.pwc.core.framework.controller;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.SeleniumArgument;
import com.pwc.core.framework.driver.MicroserviceChromeDriver;
import com.pwc.core.framework.driver.MicroserviceEdgeDriver;
import com.pwc.core.framework.driver.MicroserviceFirefoxDriver;
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
import lombok.Getter;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pwc.logging.service.LoggerService.LOG;

@Component
public class WebEventController {

    protected static final String WINDOWS_OS = "windows";
    protected static final String LINUX_OS = "linux";

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

    @Value("${saucelabs.username}")
    private String sauceLabsUser;

    @Value("${saucelabs.accesskey}")
    private String sauceLabsAccesskey;

    @Value("${browserstack.username}")
    private String browserstackUser;

    @Value("${browserstack.accesskey}")
    private String browserstackAccesskey;

    @Value("${browserstack.local:true}")
    private String browserstackLocal;

    @Value("${experitest.accesskey}")
    private String experitestAccesskey;

    @Getter
    @Value("${proxy.browsermob.enabled:false}")
    private boolean browsermobEnabled;

    @Getter
    @Value("${enable.loggingPrefs:false}")
    private boolean loggingPrefsEnabled;

    private MicroserviceWebDriver remoteWebDriver;
    private WebEventService webEventService;
    private String currentTestName;
    private String currentJobId;
    private BrowserMobProxy browserProxy;

    /**
     * Start and configure browser and WebDriver instance.
     *
     * @param credentials <code>Credentials</code> user of SiteMinder testing if needed
     */
    public void initiateBrowser(final Credentials credentials) {
        try {

            if (StringUtils.isEmpty(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY))
                            || StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.CHROME_BROWSER_MODE)) {
                this.remoteWebDriver = getChromeBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.HEADLESS_CHROME_BROWSER_MODE)) {
                this.remoteWebDriver = getHeadlessChromeBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.FIREFOX_BROWSER_MODE)) {
                this.remoteWebDriver = getFirefoxBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.HEADLESS_FIREFOX_BROWSER_MODE)) {
                this.remoteWebDriver = getHeadlessFirefoxBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.IOS_MODE)) {
                this.remoteWebDriver = getSafariBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE)) {
                this.remoteWebDriver = getInternetExplorerBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.EDGE_BROWSER_MODE)) {
                this.remoteWebDriver = getEdgeBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.HEADLESS_EDGE_BROWSER_MODE)) {
                this.remoteWebDriver = getHeadlessEdgeBrowser();
            } else if (StringUtils.equals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.SAFARI_BROWSER_MODE)) {
                this.remoteWebDriver = getSafariBrowser();
            } else {
                this.remoteWebDriver = getChromeBrowser();
            }

            if (null != ((RemoteWebDriver) this.remoteWebDriver).getSessionId()) {
                currentJobId = ((RemoteWebDriver) this.remoteWebDriver).getSessionId().toString();
            }

            webEventService = new WebEventService(remoteWebDriver);
            if (isSiteMinderEnabled()) {
                webEventService.authenticateSiteMinder(webUrl, credentials, siteMinderOpenUrl, false);
            }

            try {
                String resolution = GridUtils.initBrowserResolution();
                int width = Integer.parseInt(StringUtils.substringBefore(resolution, "x"));
                int height = Integer.parseInt(StringUtils.substringAfter(resolution, "x"));
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

            if (isBrowsermobEnabled()) {
                try {
                    getBrowserProxy().enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
                    getBrowserProxy().newHar("BrowserMob");
                } catch (Exception e) {
                    LOG(false, "Failed to proxy request due to e=%s", e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to initialize a valid browser for the RemoteWebDriver", e);
        }
    }

    /**
     * Alter any RESPONSE given the target URL.
     *
     * @param targetUrl endpoint to proxy and modify
     * @param find      response text to find
     * @param replace   response text to replace
     */
    public void alterResponse(final String targetUrl, final String find, final String replace) {

        getBrowserProxy().addResponseFilter((request, contents, messageInfo) -> {
            if (StringUtils.containsIgnoreCase(messageInfo.getOriginalUrl(), targetUrl)) {
                String messageContents = contents.getTextContents();
                String updatedContents;
                boolean isRegex;
                try {
                    if (find.matches(FrameworkConstants.XPATH_REGEX)) {
                        isRegex = true;
                    } else if (StringUtils.contains(find, "*") && StringUtils.contains(find, "?") && StringUtils.contains(find, ".")) {
                        isRegex = true;
                    } else {
                        Pattern pattern = Pattern.compile(find);
                        Matcher matcher = pattern.matcher(messageContents);
                        isRegex = matcher.matches();
                    }
                } catch (Exception e) {
                    isRegex = false;
                }

                if (isRegex) {
                    updatedContents = StringUtils.replacePattern(messageContents, find, replace);
                } else {
                    updatedContents = StringUtils.replace(messageContents, find, replace);
                }
                contents.setTextContents(updatedContents);
            }
        });
    }

    /**
     * Check if Browser Stack is being used by the downstream user based on the username and password being defined.
     *
     * @return browser stack enabled | disabled flag
     */
    private boolean isBrowserStackEnabled() {

        return gridEnabled && StringUtils.isNotEmpty(browserstackUser) && StringUtils.isNotEmpty(browserstackAccesskey);
    }

    /**
     * Check if Sauce Labs is being used by the downstream user.
     *
     * @return sauce labs enabled | disabled flag
     */
    private boolean isSauceLabsEnabled() {

        return gridEnabled && (StringUtils.isNotEmpty(sauceLabsAccesskey) && StringUtils.isNotEmpty(sauceLabsUser))
                        || (StringUtils.isNotEmpty(System.getenv(FrameworkConstants.SAUCE_LABS_BROWSER_PROPERTY)) && StringUtils.isNotEmpty(System.getenv(FrameworkConstants.BROWSER_VERSION_PROPERTY))
                                        && StringUtils.isNotEmpty(System.getenv(FrameworkConstants.PLATFORM_NAME_PROPERTY)));
    }

    /**
     * Check if Experitest / Digital.ai is being used by the downstream user.
     *
     * @return Experitest enabled | disabled flag
     */
    private boolean isDigitalAIEnabled() {

        return gridEnabled && StringUtils.isNotEmpty(experitestAccesskey);
    }

    /**
     * Create base driver option object.
     *
     * @param driverOptions any type of Selenium Driver options type
     * @return base driver options set to defaults
     */
    public AbstractDriverOptions getBrowser(AbstractDriverOptions driverOptions) {

        AbstractDriverOptions abstractDriverOptions = driverOptions;
        try {

            setDriverExecutable();

            constructTestName();
            Map<String, Object> cloudOptionsList = new HashMap<>();
            cloudOptionsList.put(FrameworkConstants.AUTOMATION_BUILD_PROPERTY, this.currentTestName);
            cloudOptionsList.put(FrameworkConstants.AUTOMATION_NAME_PROPERTY, this.currentTestName);
            abstractDriverOptions.setCapability(FrameworkConstants.CLOUD_OPTIONS_PROPERTY, cloudOptionsList);

            if (isDigitalAIEnabled()) {

                abstractDriverOptions.setCapability(FrameworkConstants.ACCESS_KEY_PROPERTY, experitestAccesskey);
                abstractDriverOptions.setCapability(FrameworkConstants.TEST_NAME_PROPERTY, this.currentTestName);

                LOG(true, "Initiating Digital.ai test execution with browser='%s'", StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)));

            } else if (isSauceLabsEnabled()) {

                Map<String, Object> sauceOptions = new HashMap<>();
                sauceOptions.put(FrameworkConstants.AUTOMATION_BUILD_PROPERTY, this.currentTestName);
                sauceOptions.put(FrameworkConstants.AUTOMATION_NAME_PROPERTY, this.currentTestName);
                sauceOptions.put(FrameworkConstants.SAUCE_LABS_USERNAME_PROPERTY, sauceLabsUser);
                sauceOptions.put(FrameworkConstants.ACCESS_KEY_PROPERTY, sauceLabsAccesskey);

                if (StringUtils.isNotEmpty(System.getProperty(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY))) {
                    sauceOptions.put(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY, System.getProperty(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY));
                }
                if (StringUtils.isNotEmpty(System.getProperty(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY))) {
                    sauceOptions.put(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY, System.getProperty(FrameworkConstants.AUTOMATION_ORIENTATION_PROPERTY));
                }
                if (!StringUtils.isEmpty(System.getenv(FrameworkConstants.SAUCE_ON_DEMAND_JENKINS_PLUGIN_TUNNEL_IDENTIFIER_PROPERTY))) {
                    sauceOptions.put(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, System.getenv(FrameworkConstants.SAUCE_ON_DEMAND_JENKINS_PLUGIN_TUNNEL_IDENTIFIER_PROPERTY));
                } else if (StringUtils.isNotEmpty(System.getProperty(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY))) {
                    sauceOptions.put(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, System.getProperty(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY));
                } else if (StringUtils.isNotEmpty(System.getenv(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY))) {
                    sauceOptions.put(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, System.getenv(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY));
                }
                abstractDriverOptions.setCapability(FrameworkConstants.SAUCE_LABS_OPTIONS_PROPERTY, sauceOptions);

                LOG(true, "Initiating Sauce Labs test execution with browser='%s'", System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));

            } else if (isBrowserStackEnabled()) {

                GridUtils.initBrowserType();

                abstractDriverOptions.setCapability(FrameworkConstants.BROWSER_STACK_BROWSER_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)));
                abstractDriverOptions.setCapability(FrameworkConstants.BROWSER_STACK_BROWSER_VERSION_PROPERTY,
                                StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_BROWSER_VERSION_PROPERTY)));

                HashMap<String, Object> browserstackOptions = new HashMap<>();
                if (StringUtils.isEmpty(System.getProperty(FrameworkConstants.BROWSER_STACK_OS_PROPERTY))) {
                    browserstackOptions.put(FrameworkConstants.BROWSER_STACK_OS_PROPERTY, WINDOWS_OS);
                } else {
                    browserstackOptions.put(FrameworkConstants.BROWSER_STACK_OS_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_OS_PROPERTY)));
                }
                browserstackOptions.put(FrameworkConstants.BROWSER_STACK_PROJECT_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.BROWSER_STACK_PROJECT_NAME_PROPERTY)));
                browserstackOptions.put(FrameworkConstants.BROWSER_STACK_BUILD_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BUILD_PROPERTY)));
                browserstackOptions.put(FrameworkConstants.BROWSER_STACK_SESSION_NAME_PROPERTY, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_NAME_PROPERTY)));
                browserstackOptions.put(FrameworkConstants.BROWSER_STACK_PROPERTY, browserstackOptions);

                abstractDriverOptions.setCapability(FrameworkConstants.BROWSER_STACK_OPTIONS_PROPERTY, browserstackOptions);

                LOG(true, "Initiating BrowserStack test execution with browser='%s'", StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)));

            } else {
                LOG(true, "Initiating Local test execution with browser='%s'", StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)));
                GridUtils.initBrowserType();
            }

            if (StringUtils.isNotEmpty(System.getProperty(FrameworkConstants.PLATFORM_NAME_PROPERTY))) {
                abstractDriverOptions.setPlatformName(System.getProperty(FrameworkConstants.PLATFORM_NAME_PROPERTY));
            }

            LoggingPreferences loggingPreferences = new LoggingPreferences();
            loggingPreferences.enable(LogType.PERFORMANCE, Level.ALL);
            loggingPreferences.enable(LogType.BROWSER, Level.ALL);

        } catch (Exception e) {
            LOG(true, "Failed to initiate Selenium Driver due to e=%s", e);
        }
        return abstractDriverOptions;
    }

    /**
     * Get Chrome Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getChromeBrowser() {

        LOG("Starting Chrome browser");
        ChromeOptions browserOptions = (ChromeOptions) getBrowser(new ChromeOptions());
        browserOptions.setBrowserVersion(StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.BROWSER_VERSION_PROPERTY), "latest"));
        browserOptions.setAcceptInsecureCerts(true);
        browserOptions.addArguments(SeleniumArgument.START_MAXIMIZED.getValue());
        browserOptions.addArguments(SeleniumArgument.DISABLE_SHM.getValue());
        browserOptions.addArguments(SeleniumArgument.DISABLE_WEB_SECURITY.getValue());
        browserOptions.addArguments(SeleniumArgument.IGNORE_CERTIFICATE_ERRORS.getValue());
        browserOptions.addArguments(SeleniumArgument.ALLOW_INSECURE_CONTENT.getValue());
        browserOptions.addArguments(FrameworkConstants.AUTOMATION_VIDEO_PROPERTY, "True");

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceChromeDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Chrome driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Headless Chrome Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getHeadlessChromeBrowser() {

        LOG("Starting Headless Chrome browser");
        ChromeOptions browserOptions = (ChromeOptions) getBrowser(new ChromeOptions());
        browserOptions.setBrowserVersion(StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.BROWSER_VERSION_PROPERTY), "latest"));
        browserOptions.setHeadless(true);
        browserOptions.addArguments("window-size=1920,1080");
        browserOptions.addArguments(SeleniumArgument.DISABLE_SHM.getValue());
        browserOptions.addArguments(SeleniumArgument.DISABLE_WEB_SECURITY.getValue());
        browserOptions.addArguments(SeleniumArgument.IGNORE_CERTIFICATE_ERRORS.getValue());
        browserOptions.addArguments(SeleniumArgument.ALLOW_INSECURE_CONTENT.getValue());
        browserOptions.addArguments(FrameworkConstants.AUTOMATION_VIDEO_PROPERTY, "True");

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceChromeDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Headless Chrome driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Microsoft Edge Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getEdgeBrowser() {

        LOG("Starting Microsoft Edge browser");
        EdgeOptions browserOptions = (EdgeOptions) getBrowser(new EdgeOptions());
        browserOptions.setBrowserVersion(StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.BROWSER_VERSION_PROPERTY), "latest"));
        browserOptions.addArguments(SeleniumArgument.START_MAXIMIZED.getValue());

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceEdgeDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Edge driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Microsoft Edge Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getHeadlessEdgeBrowser() {

        LOG("Starting Headless Microsoft Edge browser");
        EdgeOptions browserOptions = (EdgeOptions) getBrowser(new EdgeOptions());
        browserOptions.setHeadless(true);
        browserOptions.addArguments("--headless");

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceEdgeDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Edge driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Internet Explorer Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getInternetExplorerBrowser() {

        LOG("Starting Internet Explorer browser");
        InternetExplorerOptions browserOptions = (InternetExplorerOptions) getBrowser(new InternetExplorerOptions());
        browserOptions.setBrowserVersion(StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.BROWSER_VERSION_PROPERTY), "latest"));

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceInternetExplorerDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Internet Explorer driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Safari or IOS Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getSafariBrowser() {

        LOG("Starting Safari browser");
        SafariOptions browserOptions = (SafariOptions) getBrowser(new SafariOptions());

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceSafariDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Safari driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Firefox Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getFirefoxBrowser() {

        LOG("Starting Firefox browser");
        FirefoxOptions browserOptions = (FirefoxOptions) getBrowser(new FirefoxOptions());

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceFirefoxDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Firefox driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;
    }

    /**
     * Get Headless Firefox Web Driver for local or RemoteWebDriver capability.
     *
     * @return MicroserviceWebDriver instance
     */
    public MicroserviceWebDriver getHeadlessFirefoxBrowser() {

        LOG("Starting Headless Firefox browser");
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions browserOptions = (FirefoxOptions) getBrowser(new FirefoxOptions());
        browserOptions.setBinary(firefoxBinary);

        MicroserviceRemoteWebDriver microserviceRemoteWebDriver = null;
        try {
            if (browsermobEnabled) {
                browserOptions.setCapability(CapabilityType.PROXY, setupProxy());
            }
            if (gridEnabled) {
                if (null == this.remoteWebDriver) {
                    microserviceRemoteWebDriver = new MicroserviceRemoteWebDriver(new URL(gridUrl), browserOptions);
                    microserviceRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
            } else {
                if (null == this.remoteWebDriver) {
                    return (new MicroserviceFirefoxDriver(browserOptions));
                }
            }
        } catch (Exception e) {
            LOG(true, "Failed to initiate Headless Firefox driver due to exception=%s", e);
        }
        return microserviceRemoteWebDriver;

    }

    /**
     * Set the resources path to the WebDriver executable depending on the ENV the scripts.
     * are running on
     */
    protected void setDriverExecutable() {

        final String DESIRED_BROWSER = StringUtils.defaultIfBlank(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.CHROME_BROWSER_MODE);
        File executable;

        if (StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.FIREFOX_BROWSER_MODE) || StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.HEADLESS_FIREFOX_BROWSER_MODE)) {
            if (isWindowsOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver.exe");
            } else if (isLinuxOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver_linux_64");
            } else {
                executable = PropertiesUtils.getFirstFileFromTestResources("geckodriver_mac");
            }
            System.setProperty(FrameworkConstants.WEB_DRIVER_GECKO, PropertiesUtils.getPath(executable));
        } else if (StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE)) {
            executable = StringUtils.equals(System.getProperty(FrameworkConstants.SYSTEM_JVM_TYPE), "32") ? PropertiesUtils.getFirstFileFromTestResources("ie_win32.exe")
                            : PropertiesUtils.getFirstFileFromTestResources("ie_win64.exe");
            System.setProperty(FrameworkConstants.WEB_DRIVER_IE, PropertiesUtils.getPath(executable));
        } else if (StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.EDGE_BROWSER_MODE)
                        || StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.HEADLESS_EDGE_BROWSER_MODE)) {
            if (isWindowsOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("edge_win.exe");
            } else {
                executable = PropertiesUtils.getFirstFileFromTestResources("edge_mac");
            }
            System.setProperty(FrameworkConstants.WEB_DRIVER_EDGE, PropertiesUtils.getPath(executable));
        } else if (StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.HEADLESS_CHROME_BROWSER_MODE)) {
            if (isWindowsOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("chrome_win.exe");
            } else if (isLinuxOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("chrome_linux_64");
            } else {
                executable = PropertiesUtils.getFirstFileFromTestResources("chrome_mac");
            }
            System.setProperty(FrameworkConstants.WEB_DRIVER_CHROME, PropertiesUtils.getPath(executable));
        } else if (StringUtils.equalsIgnoreCase(DESIRED_BROWSER, FrameworkConstants.CHROME_BROWSER_MODE) || StringUtils.isEmpty(DESIRED_BROWSER)) {
            if (isWindowsOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("chrome_win.exe");
            } else if (isLinuxOperatingSystem()) {
                executable = PropertiesUtils.getFirstFileFromTestResources("chrome_linux_64");
            } else {
                executable = PropertiesUtils.getFirstFileFromTestResources("chrome_mac");
            }
            System.setProperty(FrameworkConstants.WEB_DRIVER_CHROME, PropertiesUtils.getPath(executable));
            System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
        }
    }

    /**
     * Check if the current operating system is Windows.
     *
     * @return flag true if Windows OS
     */
    protected boolean isWindowsOperatingSystem() {
        return StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), WINDOWS_OS);
    }

    /**
     * Check if the current operating system is Linux.
     *
     * @return flag true if Linux OS
     */
    protected boolean isLinuxOperatingSystem() {
        return StringUtils.containsIgnoreCase(System.getProperty(FrameworkConstants.SYSTEM_OS_NAME), LINUX_OS);
    }

    /**
     * Core web element action sorting logic.
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
            stopWatch.stop();
            return stopWatch.getTotalTimeMillis();
        } else if (KeyboardActivityProcessor.applies(webElement)) {
            KeyboardActivityProcessor.getInstance().webAction(webElement, webElementValue);
        } else if (ViewActivityProcessor.applies(webElement)) {
            ViewActivityProcessor.getInstance().webAction(webElement, webElementValue);
        }
        if (videoCaptureEnabled) {
            DebuggingUtils.takeScreenShot(remoteWebDriver);
        }
        return 0L;
    }

    /**
     * Build current test name from TestNG Reporter.
     */
    private void constructTestName() {

        try {
            if (Reporter.getCurrentTestResult() != null) {
                this.currentTestName = StringUtils.substringAfterLast(Reporter.getCurrentTestResult().getTestClass().getName(), ".");
            } else {
                this.currentTestName = StringUtils.appendIfMissing(StringUtils.removeStart(currentTestName, "test"), "Test");
            }
        } catch (Exception e) {
            this.currentTestName = "";
        }
    }

    /**
     * Quit the Selenium browser.
     */
    public void performQuit() {

        try {
            this.remoteWebDriver.quit();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Setup BrowserMob proxy connection.
     *
     * @return proxy connection
     */
    private Proxy setupProxy() {

        Proxy seleniumProxy = null;
        try {
            browserProxy = new BrowserMobProxyServer();
            browserProxy.setTrustAllServers(true);
            browserProxy.start();
            seleniumProxy = ClientUtil.createSeleniumProxy(browserProxy);
            final String HOST_IP = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setProxyType(Proxy.ProxyType.MANUAL);
            seleniumProxy.setHttpProxy(HOST_IP + ":" + browserProxy.getPort());
            seleniumProxy.setSslProxy(HOST_IP + ":" + browserProxy.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return seleniumProxy;
    }

    public BrowserMobProxy getBrowserProxy() {
        return browserProxy;
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
