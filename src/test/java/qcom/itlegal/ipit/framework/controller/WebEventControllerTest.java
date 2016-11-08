package qcom.itlegal.ipit.framework.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import qcom.itlegal.ipit.framework.FrameworkConstants;
import qcom.itlegal.ipit.framework.JavascriptConstants;
import qcom.itlegal.ipit.framework.data.Credentials;
import qcom.itlegal.ipit.framework.driver.QualcommWebDriver;
import qcom.itlegal.ipit.framework.service.WebEventService;
import qcom.itlegal.ipit.framework.util.PropertiesUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebEventControllerTest {

    public static final String APPLICATION_WEB_URL = "http://my-web-application.mywebsite.com";
    public static final String GRID_URL = "http://localhost:4444/wd/hub";
    private static final String UNIT_TEST_TUNNEL_IDENTIFIER = "unit-test-tunnel";

    WebEventController webEventController;
    WebEventService webEventService;

    private WebElement mockWebElement;
    private QualcommWebDriver mockWebDriverService;
    private Map<String, String> sauceEnvVariableMap;
    private DesiredCapabilities mockDesiredCapabilities;
    private Credentials credentials;

    @Before
    public void setUp() throws SQLException {

        credentials = new Credentials("foo", "bar");

        mockWebElement = mock(WebElement.class);
        mockWebDriverService = mock(QualcommWebDriver.class);
        when(mockWebDriverService.executeScript("return document.readyState")).thenReturn("complete");

        webEventController = new WebEventController();
        webEventService = new WebEventService();

        mockDesiredCapabilities = mock(DesiredCapabilities.class);
        webEventController.setCapabilities(mockDesiredCapabilities);
        when(mockWebDriverService.getCapabilities()).thenReturn(mockDesiredCapabilities);
        webEventController.setReadableTestName("testInventionListViewExport");

        when(mockWebDriverService.executeScript(JavascriptConstants.IS_JQUERY_AJAX_REQUESTS_ACTIVE)).thenReturn(true);

    }

    @Test
    public void setBrowserDefaultsTunnelIdentifierTest() {

        resetSauceLabsEnvVariableMap();

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "Firefox");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY, "44");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY, "Windows 10");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.SAUCELABS_TUNNEL_IDENTIFIER_PROPERTY, UNIT_TEST_TUNNEL_IDENTIFIER);
        PropertiesUtils.setEnv(envVariable);

        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.setDefaultDesiredCapabilities();
        DesiredCapabilities actualDesiredCapabilities = webEventController.getCapabilities();
        Assert.assertEquals(actualDesiredCapabilities.getCapability(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY), UNIT_TEST_TUNNEL_IDENTIFIER);

    }

    @Test
    public void setBrowserDefaultsEmptyTunnelIdentifierTest() {

        resetSauceLabsEnvVariableMap();

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCELABS_TUNNEL_IDENTIFIER_PROPERTY, "");
        PropertiesUtils.setEnv(envVariable);

        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.setDefaultDesiredCapabilities();
        DesiredCapabilities actualDesiredCapabilities = webEventController.getCapabilities();
        Assert.assertNull(actualDesiredCapabilities.getCapability(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY));

    }

    //@Test
    public void navigateToUrlMicrosoftEdgeSegmentTest() {
        when(mockDesiredCapabilities.getBrowserName()).thenReturn("MicrosoftEdge");
        when(mockWebDriverService.getCurrentUrl()).thenReturn(APPLICATION_WEB_URL);
        webEventService.setQualcommWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(APPLICATION_WEB_URL);
    }

//    @Test
//    public void navigateToUrlAndMaximizedBrowserTest() {
//        WebDriver.Window mockWindow = mock(WebDriver.Window.class);
//        WebDriver.Options mockOptions = mock(WebDriver.Options.class);
//        when(mockOptions.window()).thenReturn(mockWindow);
//        when(mockWebDriverService.manage()).thenReturn(mockOptions);
//
//        when(mockDesiredCapabilities.getBrowserName()).thenReturn("Chrome");
//        when(mockWebDriverService.getCurrentUrl()).thenReturn(APPLICATION_WEB_URL);
//        webEventService.setQualcommWebDriver(mockWebDriverService);
//        long duration = webEventService.redirectToUrl(APPLICATION_WEB_URL);
//        Assert.assertTrue(duration > 0 && duration < 1000);
//    }

    @Test
    public void setDefaultDesiredCapabilitiesSauceLabsAllFieldsTest() {

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "Firefox");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY, "44");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY, "Windows 10");
        PropertiesUtils.setEnv(envVariable);

        webEventController.setDefaultDesiredCapabilities();

        DesiredCapabilities actualDesiredCapabilities = webEventController.getCapabilities();
        Assert.assertTrue(actualDesiredCapabilities.isJavascriptEnabled());
        Assert.assertTrue((Boolean) actualDesiredCapabilities.getCapability("takesScreenshot"));

        Assert.assertNotEquals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), "");
        Assert.assertNotEquals(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM), "");
        Assert.assertNotEquals(webEventController.getCapabilities().getCapability(CapabilityType.VERSION), "");

        Assert.assertNotEquals(System.getenv(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY), "");
        Assert.assertNotEquals(System.getenv(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY), "");
        Assert.assertNotEquals(System.getenv(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY), "");
    }

    @Test
    public void setDefaultDesiredCapabilitiesSauceLabsPartialFieldsTest() {

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "Firefox");
        PropertiesUtils.setEnv(envVariable);

        webEventController.setDefaultDesiredCapabilities();

        DesiredCapabilities actualDesiredCapabilities = webEventController.getCapabilities();
        Assert.assertTrue(actualDesiredCapabilities.isJavascriptEnabled());
        Assert.assertTrue((Boolean) actualDesiredCapabilities.getCapability("takesScreenshot"));

        Assert.assertNotNull(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM));
        Assert.assertNotNull(webEventController.getCapabilities().getCapability(CapabilityType.VERSION));

    }

    @Test
    public void setBrowserDefaultsTest() {
        resetSauceLabsEnvVariableMap();
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.setDefaultDesiredCapabilities();
        DesiredCapabilities actualDesiredCapabilities = webEventController.getCapabilities();
        Assert.assertTrue(actualDesiredCapabilities.isJavascriptEnabled());
        Assert.assertTrue((Boolean) actualDesiredCapabilities.getCapability("takesScreenshot"));
        Assert.assertEquals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY),
                FrameworkConstants.CHROME_BROWSER_MODE);
    }

    @Test
    public void setPlatformNoPlatformSpecifiedTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertEquals(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM), Platform.ANY);
    }

    @Test
    public void setPlatformWindowsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertEquals(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM), Platform.XP);
    }

    @Test
    public void setPlatformLinuxTest() {

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "");
        PropertiesUtils.setEnv(envVariable);

        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "linux");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertEquals(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM), Platform.LINUX);
    }

    @Test
    public void setPlatformMacTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "mac");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertNotNull(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM));
    }

    @Test
    public void setPlatformOsxTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "osx");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertEquals(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM), Platform.EL_CAPITAN);
    }

    @Test
    public void setPlatformOsxWithVersionTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "osx 10.9");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertEquals(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM), Platform.MAVERICKS);
    }

    @Test
    public void setPlatformOsxWithSpacesTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "os x 10.11");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertNotNull(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM).toString());
    }

    @Test
    public void setPlatformDefaultPlatformAutomaticallyAssignedTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "bad platform name");
        webEventController.setDefaultDesiredCapabilities();
        Assert.assertNotNull(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM));
    }

    @Test(expected = AssertionError.class)
    public void setBrowserDefaultsNoBrowserTypeTest() {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.initiateBrowser(null);
        Assert.assertEquals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY),
                FrameworkConstants.FIREFOX_BROWSER_MODE);
    }

    @Test(expected = AssertionError.class)
    public void siteMinderTest() {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.setSiteMinderEnabled(true);
        webEventController.setWebUrl(APPLICATION_WEB_URL);
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.initiateBrowser(credentials);
    }

    @Test(expected = AssertionError.class)
    public void initiateBrowserNullBrowserPropertyTest() {
        sauceEnvVariableMap = new HashMap<>();
        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "chrome");
        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY, "26");
        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY, "Windows 2003");
        PropertiesUtils.setEnv(sauceEnvVariableMap);

        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.initiateBrowser(null);
    }

    @Test(expected = AssertionError.class)
    public void initiateBrowserInvalidChromeBrowserDriverLocationTest() {
        Assert.assertNull(System.getProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY));
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
        webEventController.initiateBrowser(null);
        Assert.assertEquals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY),
                FrameworkConstants.CHROME_BROWSER_MODE);
        Assert.assertNotNull(System.getProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY));
        Assert.assertNotNull(webEventController.getCapabilities().getCapability(CapabilityType.PLATFORM));
        Assert.assertTrue(webEventController.getCapabilities().isJavascriptEnabled());
        Assert.assertEquals(webEventController.getCapabilities().getCapability("takesScreenshot"), true);
    }

    @Test(expected = AssertionError.class)
    public void initiateBrowserInvalidInternetExploreBrowserDriverLocationTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE);
        webEventController.initiateBrowser(null);
    }

    @Test(expected = AssertionError.class)
    public void initiateBrowserInvalidPhantomJsBrowserDriverLocationTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.PHANTOMJS_BROWSER_MODE);
        webEventController.initiateBrowser(null);
    }

    @Test(expected = AssertionError.class)
    public void initiateBrowserInvalidDefaultFirefoxBrowserDriverLocationTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "use my default browser");
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.initiateBrowser(null);
    }

    @Test(expected = AssertionError.class)
    public void unableToNavigateToUrlTest() {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.initiateBrowser(null);
    }

    @Test(expected = AssertionError.class)
    public void initiateInternalUrlTest() {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setWebUrl(APPLICATION_WEB_URL);
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.FIREFOX_BROWSER_MODE);
        webEventController.initiateBrowser(null);
    }

    @Test
    public void navigateToUrlSegmentTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(APPLICATION_WEB_URL);
        webEventService.setQualcommWebDriver(mockWebDriverService);
        long duration = webEventService.redirectToUrl("/foobar/ipit");
        Assert.assertNotNull(duration);
    }

    @Test
    public void navigateToFullUrlTest() {
        webEventService.setQualcommWebDriver(mockWebDriverService);
        long duration = webEventService.redirectToUrl(APPLICATION_WEB_URL);
        Assert.assertNotNull(duration);
    }

    @Test()
    public void getIOSBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getIOSBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getFirefoxBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getFirefoxBrowser();
        Assert.assertNull(result);
    }

    @Test(expected = org.openqa.selenium.WebDriverException.class)
    public void getIOSBrowserGridNoExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getIOSBrowser();
    }

    @Test(expected = org.openqa.selenium.WebDriverException.class)
    public void getFirefoxBrowserGridNoExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getFirefoxBrowser();
    }

    @Test()
    public void getFirefoxBrowserNoGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getFirefoxBrowser();
        QualcommWebDriver result = webEventController.getFirefoxBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getIOSBrowserNoGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getFirefoxBrowser();
        QualcommWebDriver result = webEventController.getIOSBrowser();
        Assert.assertNull(result);
    }

    @Test
    public void setDriverExecutableTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        String driverPath = webEventController.setDriverExecutable();
        Assert.assertEquals(driverPath, "");
    }

    @Test
    public void setDriverExecutableChromeDriverTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
        String driverPath = webEventController.setDriverExecutable();
        Assert.assertEquals(driverPath, "");
        Assert.assertEquals(System.getProperty(FrameworkConstants.WEB_DRIVER_CHROME), "");
    }

    @Test
    public void setDriverExecutableInternetExplorerDriverTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE);
        String driverPath = webEventController.setDriverExecutable();
        Assert.assertEquals(driverPath, "");
        Assert.assertEquals(System.getProperty(FrameworkConstants.WEB_DRIVER_IE), "");
    }

    @Test
    public void setDriverExecutablePhantomJsDriverTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.PHANTOMJS_BROWSER_MODE);
        webEventController.setDriverExecutable();
    }

    @Test()
    public void getPhantomJsBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getPhantomJsBrowser();
        Assert.assertNull(result);
    }

    @Test(expected = org.openqa.selenium.WebDriverException.class)
    public void getPhantomJsBrowserGridNoExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getPhantomJsBrowser();
    }

    @Test()
    public void getPhantomJsBrowserNoGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getPhantomJsBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getEdgeBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getEdgeBrowser();
        Assert.assertNull(result);
    }

    @Test(expected = org.openqa.selenium.WebDriverException.class)
    public void getEdgeBrowserNoExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getEdgeBrowser();
    }

    @Test()
    public void getSafariBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getSafariBrowser();
        Assert.assertNull(result);
    }

    @Test(expected = org.openqa.selenium.WebDriverException.class)
    public void getSafariGridNoExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getSafariBrowser();
    }

    @Test()
    public void getSafariBrowserNoGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getSafariBrowser();
        QualcommWebDriver result = webEventController.getSafariBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getInternetExplorerBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getInternetExplorerBrowser();
        Assert.assertNull(result);
    }

    @Test(expected = org.openqa.selenium.WebDriverException.class)
    public void getInternetExplorerBrowserGridNoExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getInternetExplorerBrowser();
    }

    @Test()
    public void getInternetExplorerBrowserNoGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        QualcommWebDriver result = webEventController.getInternetExplorerBrowser();
        Assert.assertNull(result);
    }

    @Test
    public void webActionNullTagNameTest() {
        webEventController.webAction(mockWebElement, "foobar");

    }

    private void resetSauceLabsEnvVariableMap() {
        sauceEnvVariableMap = new HashMap<>();
        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "");
        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_BROWSER_VERSION_PROPERTY, "");
        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_PLATFORM_PROPERTY, "");
        PropertiesUtils.setEnv(sauceEnvVariableMap);
    }
}
