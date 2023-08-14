package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.driver.MicroserviceWebDriver;
import com.pwc.core.framework.service.WebElementBaseTest;
import com.pwc.core.framework.service.WebEventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@RunWith(MockitoJUnitRunner.class)
public class DebuggingUtilsTest extends WebElementBaseTest {

    WebEventService webEventService;

    @Mock
    MicroserviceWebDriver mockWebDriverService;

    private File screenshotFile;
    private TakesScreenshot mockTakesScreenShot;

    @Before
    public void setUp() {

        webEventService = new WebEventService();
        webEventService.setMicroserviceWebDriver(mockWebDriverService);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        when(mockWebDriverService.getCapabilities()).thenReturn(capabilities);

        screenshotFile = PropertiesUtils.getFileFromResources("screenshots/UniqueTest_screenshot1.png");
        mockTakesScreenShot = mock(TakesScreenshot.class);
    }

    @Test
    public void takeScreenShotDisabledTest() {
        webEventService.setVideoCaptureEnabled(false);
        MicroserviceWebDriver mockQWebDriver = mock(MicroserviceWebDriver.class);
        DebuggingUtils.takeScreenShot(mockQWebDriver, false);
    }

    @Test
    public void takeScreenShotEnabledTest() {
        webEventService.setVideoCaptureEnabled(true);
        MicroserviceWebDriver mockQWebDriver = mock(MicroserviceWebDriver.class);
        DebuggingUtils.takeScreenShot(mockQWebDriver, true);
    }

    @Test
    public void takeScreenShotBecauseVisibleBrowserTest() {
        DesiredCapabilities mockDesiredCapabilities = mock(DesiredCapabilities.class);
        MicroserviceWebDriver mockWebDriverService = mock(MicroserviceWebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
        TakesScreenshot takesScreenshot = (TakesScreenshot) mockWebDriverService;
        when(takesScreenshot.getScreenshotAs(OutputType.FILE)).thenReturn(screenshotFile);
        when(mockDesiredCapabilities.getBrowserName()).thenReturn(FrameworkConstants.FIREFOX_BROWSER_MODE);
        when(mockWebDriverService.getCapabilities()).thenReturn(mockDesiredCapabilities);
        when(mockTakesScreenShot.getScreenshotAs(OutputType.FILE)).thenReturn(screenshotFile);
        DebuggingUtils.takeScreenShot(mockWebDriverService);
        Assert.assertNotNull(mockWebDriverService);
    }

    @Test
    public void takeScreenShotBecauseVisibleBrowserNullScreenshotTest() {
        MicroserviceWebDriver mockQWebDriver = mock(MicroserviceWebDriver.class);
        DebuggingUtils.takeScreenShot(mockQWebDriver);
    }

    @Test
    public void doesSimilarScreenShotExistTest() {
        File screenShot = PropertiesUtils.getFirstFileFromTestResources("Unique_screenshot");
        boolean result = DebuggingUtils.doesSimilarScreenShotExist(screenShot);
        Assert.assertFalse(result);
    }

    @Test
    public void doesSimilarScreenShotExistDuplicateScreenshotsTest() {
        File screenShot = PropertiesUtils.getFirstFileFromTestResources("WebEventServiceTest_screenshot");
        boolean result = DebuggingUtils.doesSimilarScreenShotExist(screenShot);
        Assert.assertTrue(result);
    }

    @Test
    public void doesSimilarScreenShotExistInvalidScreenshotsTest() {
        File screenShot = PropertiesUtils.getFirstFileFromTestResources("foobar");
        boolean result = DebuggingUtils.doesSimilarScreenShotExist(screenShot);
        Assert.assertFalse(result);
    }

    @Test
    public void getDebugInfoTest() {
        String debugInfo = DebuggingUtils.getDebugInfo(mockWebDriverService);
        Assert.assertNotNull(debugInfo);
    }

}
