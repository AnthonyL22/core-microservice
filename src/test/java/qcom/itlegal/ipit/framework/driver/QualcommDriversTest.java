package qcom.itlegal.ipit.framework.driver;

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
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;

@RunWith(MockitoJUnitRunner.class)
public class QualcommDriversTest {

    private DesiredCapabilities desiredCapabilities;
    private RemoteWebDriver driver;
    private DesiredCapabilities requiredCapabilities;
    private EdgeOptions edgeOptions;
    private SafariOptions safariOptions;

    @Before
    public void setUp() {
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setVersion("10");

        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setJavascriptEnabled(true);

        edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy("fast");

        safariOptions = new SafariOptions();
        safariOptions.setPort(8080);

    }

    @Test(expected = Exception.class)
    public void qualcommSafariDriverTest() {
        QualcommSafariDriver driver = new QualcommSafariDriver(desiredCapabilities);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void qualcommSafariOptionsSafariDriverTest() {
        QualcommSafariDriver driver = new QualcommSafariDriver(safariOptions);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void qualcommEdgeDriverTest() {
        QualcommEdgeDriver driver = new QualcommEdgeDriver(desiredCapabilities);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void qualcommEdgeDriverEdgeOptionsTest() {
        QualcommEdgeDriver driver = new QualcommEdgeDriver(edgeOptions);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void qualcommAndroidDriverTest() {
        QualcommAndroidDriver driver = new QualcommAndroidDriver(desiredCapabilities);
        Assert.assertNotNull(driver);
    }

    @Test(expected = Exception.class)
    public void qualcommChromeDriverTest() {
        driver = new QualcommChromeDriver();
    }

    @Test(expected = Exception.class)
    public void qualcommChromeDriverChromeDriverServiceTest() {
        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        driver = new QualcommChromeDriver(chromeDriverService);
    }

    @Test(expected = Exception.class)
    public void qualcommChromeDriverDesiredCapabilitiesTest() {
        driver = new QualcommChromeDriver(desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void qualcommChromeDriverOptionsTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("foo", "bar");
        driver = new QualcommChromeDriver(chromeOptions);
    }

    @Test(expected = Exception.class)
    public void qualcommChromeDriverChromeDriverServiceAndOptionsTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("foo", "bar");
        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        driver = new QualcommChromeDriver(chromeDriverService, chromeOptions);
    }

    @Test(expected = Exception.class)
    public void qualcommChromeDriverChromeDriverServiceAndCapabilitiesTest() {
        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        driver = new QualcommChromeDriver(chromeDriverService, desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void qualcommInternetExplorerDriverTest() {
        driver = new QualcommInternetExplorerDriver(desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void qualcommInternetExplorerDriverWithPortTest() {
        driver = new QualcommInternetExplorerDriver(4444);
    }

    @Test(expected = Exception.class)
    public void qualcommInternetExplorerDriverServiceTest() {
        InternetExplorerDriverService internetExplorerDriverService = InternetExplorerDriverService.createDefaultService();
        driver = new QualcommInternetExplorerDriver(internetExplorerDriverService);
    }

    @Test(expected = Exception.class)
    public void qualcommInternetExplorerDriverServiceAndCapabilitiesTest() {
        InternetExplorerDriverService internetExplorerDriverService = InternetExplorerDriverService.createDefaultService();
        driver = new QualcommInternetExplorerDriver(internetExplorerDriverService, desiredCapabilities);
    }

    @Test(expected = Exception.class)
    public void qualcommPhantomJsDriverTest() {
        driver = new QualcommPhantomJsDriver();
    }

    @Test(expected = Exception.class)
    public void qualcommPhantomJsDriverWithServiceTest() {
        PhantomJSDriverService phantomJSDriverService = PhantomJSDriverService.createDefaultService();
        driver = new QualcommPhantomJsDriver(phantomJSDriverService, requiredCapabilities);
    }

}
