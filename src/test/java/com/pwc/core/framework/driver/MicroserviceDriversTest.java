package com.pwc.core.framework.driver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceDriversTest {

    @Test
    public void firefoxDriverTest() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(firefoxBinary);
        MicroserviceFirefoxDriver driver = new MicroserviceFirefoxDriver(options);
        Assert.assertNotNull(driver);
    }

    @Test
    public void chromeDriverTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        MicroserviceChromeDriver driver = new MicroserviceChromeDriver(options);
        Assert.assertNotNull(driver);
    }

    @Test
    public void edgeDriverTest() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless=new");
        MicroserviceEdgeDriver driver = new MicroserviceEdgeDriver(options);
        Assert.assertNotNull(driver);
    }

}
