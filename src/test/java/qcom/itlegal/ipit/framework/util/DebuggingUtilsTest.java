package qcom.itlegal.ipit.framework.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import qcom.itlegal.ipit.framework.FrameworkConstants;
import qcom.itlegal.ipit.framework.driver.QualcommWebDriver;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;
import qcom.itlegal.ipit.framework.service.WebEventService;

import java.io.File;
import java.sql.SQLException;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class DebuggingUtilsTest extends WebElementBaseTest {

    WebEventService webEventService;

    @Mock
    WebEventService mockWebEventService;

    @Mock
    QualcommWebDriver mockWebDriverService;

    private File screenshotFile;
    private TakesScreenshot mockTakesScreenShot;

    @Before
    public void setUp() throws SQLException {

        webEventService = new WebEventService();
        webEventService.setQualcommWebDriver(mockWebDriverService);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        when(mockWebDriverService.getCapabilities()).thenReturn(capabilities);

        screenshotFile = PropertiesUtils.getFileFromResources("screenshots/UniqueTest_screenshot1.png");
        mockTakesScreenShot = mock(TakesScreenshot.class);
    }

    @Test
    public void takeScreenShotBecauseVisibleBrowserTest() {
        DesiredCapabilities mockDesiredCapabilities = mock(DesiredCapabilities.class);
        QualcommWebDriver mockWebDriverService = mock(QualcommWebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
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
        QualcommWebDriver mockQWebDriver = mock(QualcommWebDriver.class);
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
