package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.driver.MicroserviceWebDriver;
import com.pwc.logging.LoggerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.util.Collection;

import static com.pwc.logging.LoggerService.LOG;

public class DebuggingUtils {

    /**
     * Capture and store the active screen under test and store in '.../target/test-classes/screenshots' directory
     */
    public static void takeScreenShot(MicroserviceWebDriver webDriver) {

        try {

            File tempScreenShotFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            URL url = ClassLoader.getSystemClassLoader().getResource(FrameworkConstants.SYSTEM_TEST_ENVIRONMENT_DIR + "../");

            StringBuilder localHostScreenCapPath = new StringBuilder();
            StringBuilder remoteHostScreenCapPath = new StringBuilder();

            remoteHostScreenCapPath.append("http://");
            localHostScreenCapPath.append(url.getPath());
            remoteHostScreenCapPath.append(InetAddress.getLocalHost().getHostName());
            localHostScreenCapPath.append("screenshots/");
            remoteHostScreenCapPath.append("/screenshots/");
            localHostScreenCapPath.append(LoggerHelper.getClassName(Reporter.getCurrentTestResult()));
            remoteHostScreenCapPath.append(LoggerHelper.getClassName(Reporter.getCurrentTestResult()));
            localHostScreenCapPath.append("_");
            remoteHostScreenCapPath.append("_");
            localHostScreenCapPath.append(tempScreenShotFile.getName());
            remoteHostScreenCapPath.append(tempScreenShotFile.getName());
            File targetScreenCap = new File(StringUtils.replace(localHostScreenCapPath.toString(), "%20", " "));

            if (!doesSimilarScreenShotExist(targetScreenCap)) {
                FileUtils.copyFile(tempScreenShotFile, targetScreenCap);
                if (!StringUtils.containsIgnoreCase(targetScreenCap.getAbsolutePath(), "jenkins")) {
                    LOG(false, String.format("<p><a href='file:///%s'> LOCAL <img src='%s' height='100' width='100'/></a></p>", targetScreenCap.getAbsolutePath(), targetScreenCap.getAbsolutePath()));
                } else {
                    LOG(false, String.format("<p><a href='%s'> REMOTE <img src='%s' height='100' width='100'/></a></p>", remoteHostScreenCapPath.toString(), remoteHostScreenCapPath.toString()));
                }
            }

        } catch (Exception e) {
            LOG(String.format("Unable to take ScreenShot due to exception='%s'", e.getMessage()));
        }
    }

    /**
     * Check if there is a previous ScreenShot for this test and not take or save another image
     * Due to space concerns on Jenkins
     *
     * @param targetScreenShot Screen shot file to look for
     * @return flag if it found a similar PNG file for the test
     */
    public static boolean doesSimilarScreenShotExist(File targetScreenShot) {
        boolean previousScreenShotExists = false;
        try {
            File dir = new File(targetScreenShot.getParent());
            Collection<File> activeScreenShots = FileUtils.listFiles(dir, new String[]{"png"}, true);
            for (File activeScreenShot : activeScreenShots) {
                previousScreenShotExists = activeScreenShot.getName().matches(StringUtils.substringBefore(targetScreenShot.getName(), "_") + "_screenshot.*?\\.png$");
                if (previousScreenShotExists) {
                    break;
                }
            }
            return previousScreenShotExists;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get debug information for the calling method to decorate it's LOG statement as
     * to what page the Assertion failure occurred on
     *
     * @return formatted debug message including the page title of failure
     */
    public static String getDebugInfo(MicroserviceWebDriver webDriver) {
        return String.format(" {Page Title: '%s'}", webDriver.getTitle());
    }

}
