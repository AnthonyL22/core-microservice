package com.pwc.core.framework.controller;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.driver.MicroserviceAndroidMobileDriver;
import com.pwc.core.framework.driver.MicroserviceIOSMobileDriver;
import com.pwc.core.framework.driver.MicroserviceMobileDriver;
import com.pwc.core.framework.driver.MicroserviceRemoteMobileDriver;
import com.pwc.core.framework.processors.mobile.TapActivityProcessor;
import com.pwc.core.framework.processors.mobile.ViewActivityProcessor;
import com.pwc.core.framework.service.MobileEventService;
import com.pwc.core.framework.util.GridUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.StringUtils;
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

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.pwc.logging.service.LoggerService.LOG;


@Component
public class MobileEventController {

    @Value("${app.path}")
    private String appPath;

    @Value("${app.classpath}")
    private String appActivateClasspath;

    @Value("${grid.enabled:true}")
    private boolean gridEnabled;

    @Value("${grid.hub.url}")
    private String gridUrl;

    @Value("${enable.siteMinder:false}")
    private boolean siteMinderEnabled;

    @Value("${capture.video:false}")
    private boolean videoCaptureEnabled;

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

    private MicroserviceMobileDriver remoteMobileDriver;
    private MobileEventService mobileEventService;
    private DesiredCapabilities capabilities;
    private String currentTestName;
    private String currentJobId;

    /**
     * Start and configure mobile devices and Driver instance
     */
    public void initiateDevice() {
        try {

            setDefaultDesiredCapabilities();

            switch (System.getProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY)) {
                case FrameworkConstants.IOS_MODE: {
                    this.remoteMobileDriver = getIOSDriver();
                    break;
                }
                case FrameworkConstants.ANDROID_MODE: {
                    this.remoteMobileDriver = getAndroidDriver();
                    break;
                }
            }

            currentJobId = ((RemoteWebDriver) this.remoteMobileDriver).getSessionId().toString();

            mobileEventService = new MobileEventService(remoteMobileDriver);
            mobileEventService.setTimeOutInSeconds(defaultWaitForElementTimeoutInSeconds);
            mobileEventService.setSleepInMillis(defaultWaitForSleepDurationInMillis);
            mobileEventService.setPageTimeoutInSeconds(defaultWaitForPageTimeoutInSeconds);
            mobileEventService.setVideoCaptureEnabled(videoCaptureEnabled);
            mobileEventService.setWaitForAjaxRequestsEnabled(waitForAjaxRequestsEnabled);

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

            //ToDo: add correct Saucelabs capabilities here

            LOG(true, "Initiating Sauce-OnDemand test execution with browser='%s', version='%s', platform='%s'",
                    System.getenv(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY), System.getenv(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY), System.getenv(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY));


        } else if (isBrowserStackEnabled()) {

            GridUtils.initBrowserType();

            //ToDo: add correct Browserstack capabilities here

            LOG(true, "Initiating BrowserStack test execution with browser='%s', platform='%s'",
                    capabilities.getCapability(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY),
                    capabilities.getCapability(FrameworkConstants.BROWSER_STACK_OS_PROPERTY));

        } else {
            LOG(true, "Initiating User Defined test execution");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY)));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_LONG_VERSION_PROPERTY)));
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_DEVICE_NAME_PROPERTY)));
            capabilities.setCapability(MobileCapabilityType.APP, StringUtils.trim(appPath));
            capabilities.setCapability("useNewWDA", false);
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
     * @return IOSDriver instance
     * @throws java.net.MalformedURLException url exception
     */
    public MicroserviceMobileDriver getIOSDriver() throws Exception {

        LOG("starting iOS driver");
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }
        if (gridEnabled) {
            if (this.remoteMobileDriver == null) {
//                IOSDriver iosDriver = new IOSDriver(new URL(gridUrl), capabilities);
//                iosDriver.findElementByIosNsPredicate("type == 'XCUIElementTypeStaticText' and name == 'General'");

                MicroserviceRemoteMobileDriver microserviceRemoteMobileDriver = new MicroserviceRemoteMobileDriver(new URL(gridUrl), capabilities);
                microserviceRemoteMobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                return microserviceRemoteMobileDriver;

                //This works!!!
//                MicroserviceRemoteMobileDriver microserviceRemoteMobileDriver = new MicroserviceRemoteMobileDriver(new URL(gridUrl), capabilities);
//                microserviceRemoteMobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//                MobileElement element = (MobileElement) microserviceRemoteMobileDriver.findElementByIosNsPredicate("type == 'XCUIElementTypeStaticText' and name == 'General'");
//                System.out.println();
            }
        } else {
            if (this.remoteMobileDriver == null) {
                return (new MicroserviceIOSMobileDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Get Android Web Driver for local or RemoteWebDriver capability
     *
     * @return MicroserviceWebDriver instance
     * @throws java.net.MalformedURLException url exception
     */
    public MicroserviceMobileDriver getAndroidDriver() throws Exception {

        LOG("starting android driver");
        //setDriverExecutable();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.ANDROID);
        if (StringUtils.isNotEmpty(experitestAccesskey)) {
            capabilities.setCapability("accessKey", experitestAccesskey);
            capabilities.setCapability("testName", this.currentTestName);
        }
        if (gridEnabled) {
            if (this.remoteMobileDriver == null) {
                MicroserviceRemoteMobileDriver microserviceRemoteMobileDriver = new MicroserviceRemoteMobileDriver(new URL(gridUrl), capabilities);
                microserviceRemoteMobileDriver.setFileDetector(new LocalFileDetector());
                return microserviceRemoteMobileDriver;
            }
        } else {
            if (this.remoteMobileDriver == null) {
                return (new MicroserviceAndroidMobileDriver(capabilities));
            }
        }
        return null;
    }

    /**
     * Core mobile element action sorting logic
     *
     * @param mobileElement      DOM element to act upon
     * @param mobileElementValue DOM element value to alter
     * @return time in milliseconds for Mouse-specific web event to execute
     */
    public long mobileAction(final MobileElement mobileElement, final Object mobileElementValue) {

        if (TapActivityProcessor.applies(mobileElement)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            TapActivityProcessor.getInstance().mobileAction(mobileElement, mobileElementValue);
            stopWatch.stop();
            return stopWatch.getTotalTimeMillis();
        } else if (ViewActivityProcessor.applies(mobileElement)) {
            ViewActivityProcessor.getInstance().mobileAction(mobileElement, mobileElementValue);
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
     * Close the Mobile App
     */
    public void closeApp() {
        try {
            this.remoteMobileDriver.closeApp();
        } catch (Exception e) {
            e.getMessage();
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

    public MobileEventService getMobileEventService() {
        return mobileEventService;
    }

    public void setMobileEventService(MobileEventService service) {
        this.mobileEventService = service;
    }

    public void setRemoteMobileDriver(MicroserviceMobileDriver remoteMobileDriver) {
        this.remoteMobileDriver = remoteMobileDriver;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public void setAppActivateClasspath(String appActivateClasspath) {
        this.appActivateClasspath = appActivateClasspath;
    }

    public String getAppPath() {
        return appPath;
    }

    public String getAppActivateClasspath() {
        return appActivateClasspath;
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
