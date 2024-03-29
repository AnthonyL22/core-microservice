package com.pwc.core.framework.controller;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.JavascriptConstants;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.driver.MicroserviceWebDriver;
import com.pwc.core.framework.service.WebEventService;
import com.pwc.core.framework.util.PropertiesUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static com.pwc.core.framework.controller.WebEventController.LINUX_OS;
import static com.pwc.core.framework.controller.WebEventController.WINDOWS_OS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebEventControllerTest {

    private static final String APPLICATION_WEB_URL = "http://my-web-application.mywebsite.com";
    private static final String GRID_URL = "http://localhost:4444/wd/hub";
    private static final String UNIT_TEST_TUNNEL_IDENTIFIER = "unit-test-tunnel";

    private WebEventController webEventController;
    private WebEventService webEventService;

    private WebElement mockWebElement;
    private MicroserviceWebDriver mockWebDriverService;
    private Map<String, String> sauceEnvVariableMap;
    private DesiredCapabilities mockDesiredCapabilities;
    private Credentials credentials;

    @Before
    public void setUp() {

        credentials = new Credentials("foo", "bar");

        mockWebElement = mock(WebElement.class);
        mockWebDriverService = mock(MicroserviceWebDriver.class);
        when(mockWebDriverService.executeScript("return document.readyState")).thenReturn("complete");

        webEventController = new WebEventController();
        webEventService = new WebEventService();

        mockDesiredCapabilities = mock(DesiredCapabilities.class);
        when(mockWebDriverService.getCapabilities()).thenReturn(mockDesiredCapabilities);
        webEventController.setReadableTestName("testInventionListViewExport");

        when(mockWebDriverService.executeScript(JavascriptConstants.IS_JQUERY_AJAX_REQUESTS_ACTIVE)).thenReturn(true);

    }

    @Test
    public void setBrowserDefaultsTunnelIdentifierTest() {

        resetSauceLabsEnvVariableMap();

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCE_LABS_BROWSER_PROPERTY, "Firefox");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.BROWSER_VERSION_PROPERTY, "44");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.PLATFORM_NAME_PROPERTY, "Windows 10");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, UNIT_TEST_TUNNEL_IDENTIFIER);
        PropertiesUtils.setEnv(envVariable);

    }

    @Test
    public void setBrowserDefaultsEmptyTunnelIdentifierTest() {

        resetSauceLabsEnvVariableMap();

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, "");
        PropertiesUtils.setEnv(envVariable);

    }

    @Test
    public void setDefaultDesiredCapabilitiesSauceLabsAllFieldsTest() {

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCE_LABS_BROWSER_PROPERTY, "Firefox");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.BROWSER_VERSION_PROPERTY, "44");
        PropertiesUtils.setEnv(envVariable);
        envVariable.put(FrameworkConstants.PLATFORM_NAME_PROPERTY, "Windows 10");
        PropertiesUtils.setEnv(envVariable);

        Assert.assertNotEquals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), "");
        Assert.assertNotEquals(System.getenv(FrameworkConstants.SAUCE_LABS_BROWSER_PROPERTY), "");
        Assert.assertNotEquals(System.getenv(FrameworkConstants.BROWSER_VERSION_PROPERTY), "");
        Assert.assertNotEquals(System.getenv(FrameworkConstants.PLATFORM_NAME_PROPERTY), "");
    }

    @Test
    public void setDefaultDesiredCapabilitiesSauceLabsPartialFieldsTest() {

        HashMap<String, String> envVariable = new HashMap();
        envVariable.put(FrameworkConstants.SAUCE_LABS_BROWSER_PROPERTY, "Firefox");
        PropertiesUtils.setEnv(envVariable);

    }

    @Test
    public void isWindowsOperatingSystemTest() {
        System.setProperty(FrameworkConstants.SYSTEM_OS_NAME, WINDOWS_OS);
        boolean answer = webEventController.isWindowsOperatingSystem();
        Assert.assertTrue(answer);
    }

    @Test
    public void isLinuxOperatingSystemTest() {
        System.setProperty(FrameworkConstants.SYSTEM_OS_NAME, LINUX_OS);
        boolean answer = webEventController.isLinuxOperatingSystem();
        Assert.assertTrue(answer);
    }

    @Test(expected = AssertionError.class)
    public void setBrowserDefaultsNoBrowserTypeTest() {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        webEventController.initiateBrowser(null);
        Assert.assertEquals(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY), FrameworkConstants.FIREFOX_BROWSER_MODE);
    }

    @Test
    public void navigateToUrlSegmentTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(APPLICATION_WEB_URL);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        long duration = webEventService.redirectToUrl("/foobar/hello");
        Assert.assertNotNull(duration);
    }

    @Test
    public void navigateToFullUrlTest() {
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        long duration = webEventService.redirectToUrl(APPLICATION_WEB_URL);
        Assert.assertNotNull(duration);
    }

    @Test()
    public void getIOSBrowserGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        WebDriver result = webEventController.getSafariBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getFirefoxBrowserGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        WebDriver result = webEventController.getFirefoxBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getFirefoxBrowserNoGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getFirefoxBrowser();
        WebDriver result = webEventController.getFirefoxBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getIOSBrowserNoGridExistingLiveRemoteDriverTest() throws Exception {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getFirefoxBrowser();
        WebDriver result = webEventController.getSafariBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getEdgeBrowserGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        WebDriver result = webEventController.getEdgeBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getSafariBrowserGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        WebDriver result = webEventController.getSafariBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getSafariBrowserNoGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        webEventController.getSafariBrowser();
        WebDriver result = webEventController.getSafariBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getInternetExplorerBrowserGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
        WebDriver result = webEventController.getInternetExplorerBrowser();
        Assert.assertNull(result);
    }

    @Test()
    public void getInternetExplorerBrowserNoGridExistingLiveRemoteDriverTest() throws MalformedURLException {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(false);
        webEventController.setGridUrl(GRID_URL);
        WebDriver result = webEventController.getInternetExplorerBrowser();
        Assert.assertNull(result);
    }

    @Test
    public void webActionNullTagNameTest() {
        webEventController.webAction(mockWebElement, "foobar");
    }

    private void resetSauceLabsEnvVariableMap() {
        sauceEnvVariableMap = new HashMap<>();
        sauceEnvVariableMap.put(FrameworkConstants.SAUCE_LABS_BROWSER_PROPERTY, "");
        sauceEnvVariableMap.put(FrameworkConstants.BROWSER_VERSION_PROPERTY, "");
        sauceEnvVariableMap.put(FrameworkConstants.PLATFORM_NAME_PROPERTY, "");
        PropertiesUtils.setEnv(sauceEnvVariableMap);
    }

    @Test()
    public void getFirefoxBrowserGridExistingLiveRemoteDriverHeadlessTest() {
        webEventController.setRemoteWebDriver(mockWebDriverService);
        webEventController.setGridEnabled(true);
        webEventController.setGridUrl(GRID_URL);
    }

}
