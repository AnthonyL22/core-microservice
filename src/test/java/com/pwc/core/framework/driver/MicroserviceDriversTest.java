package com.pwc.core.framework.driver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceDriversTest {

    private DesiredCapabilities desiredCapabilities;
    private RemoteWebDriver driver;
    private DesiredCapabilities requiredCapabilities;
    private EdgeOptions edgeOptions;

    @Before
    public void setUp() {
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setVersion("10");

        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setJavascriptEnabled(true);

        edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy("fast");

    }

    @Test(expected = Exception.class)
    public void edgeDriverTest() {
        MicroserviceEdgeDriver driver = new MicroserviceEdgeDriver(desiredCapabilities);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void edgeDriverEdgeOptionsTest() {
        MicroserviceEdgeDriver driver = new MicroserviceEdgeDriver(edgeOptions);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void androidDriverTest() {
        MicroserviceAndroidDriver driver = new MicroserviceAndroidDriver(desiredCapabilities);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void chromeDriverTest() {
        driver = new MicroserviceChromeDriver();
    }

    @Test(expected = Exception.class)
    public void chromeDriverChromeDriverServiceTest() {
        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        driver = new MicroserviceChromeDriver(chromeDriverService);
    }

    @Test(expected = Exception.class)
    public void chromeDriverDesiredCapabilitiesTest() {
        driver = new MicroserviceChromeDriver(desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void chromeDriverOptionsTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("foo", "bar");
        driver = new MicroserviceChromeDriver(chromeOptions);
    }

    @Test(expected = Exception.class)
    public void chromeDriverChromeDriverServiceAndOptionsTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("foo", "bar");
        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        driver = new MicroserviceChromeDriver(chromeDriverService, chromeOptions);
    }

    @Test(expected = Exception.class)
    public void chromeDriverChromeDriverServiceAndCapabilitiesTest() {
        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        driver = new MicroserviceChromeDriver(chromeDriverService, desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void internetExplorerDriverTest() {
        driver = new MicroserviceInternetExplorerDriver(desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void internetExplorerDriverWithPortTest() {
        driver = new MicroserviceInternetExplorerDriver(4444);
    }

    @Test(expected = Exception.class)
    public void internetExplorerDriverServiceTest() {
        InternetExplorerDriverService internetExplorerDriverService = InternetExplorerDriverService.createDefaultService();
        driver = new MicroserviceInternetExplorerDriver(internetExplorerDriverService);
    }

    @Test(expected = Exception.class)
    public void internetExplorerDriverServiceAndCapabilitiesTest() {
        InternetExplorerDriverService internetExplorerDriverService = InternetExplorerDriverService.createDefaultService();
        driver = new MicroserviceInternetExplorerDriver(internetExplorerDriverService, desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void phantomJsDriverTest() {
        driver = new MicroservicePhantomJsDriver();
    }

    @Test(expected = Exception.class)
    public void phantomJsDriverWithServiceTest() {
        PhantomJSDriverService phantomJSDriverService = PhantomJSDriverService.createDefaultService();
        driver = new MicroservicePhantomJsDriver(phantomJSDriverService, requiredCapabilities);
    }

}
