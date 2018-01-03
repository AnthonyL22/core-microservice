package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Platform;

public class GridUtilsTest {

    @Test
    public void initBrowserTypesNullTest() {
        System.clearProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesFoobarTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "bad");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesAnyTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, Platform.ANY.toString());
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesBlankTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesSafariTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.SAFARI_BROWSER_MODE);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.SAFARI_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesSFTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "sf");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.SAFARI_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesNotSpecifiedDefaultToChromeTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "notUseDefaultBrowser");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesEmptySystemPropertyTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesHeadlessChromeTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.HEADLESS_CHROME_BROWSER_MODE);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.HEADLESS_CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesCHTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesChromeTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "Chrome");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesChromeCapsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "CHROME");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesCHSpaceTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "CH ");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.CHROME_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesAndroidTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "android");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.ANDROID_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesDroidTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "droid");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.ANDROID_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesAppiumTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "appium");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.ANDROID_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesGoogleTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "google");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.ANDROID_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesFFTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.FIREFOX_BROWSER_MODE);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.FIREFOX_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesFirefoxTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "firefox");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.FIREFOX_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesFirefoxCamelCaseTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "fireFox");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.FIREFOX_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesIETest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "IE");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesInternetExplorerTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "internet explorer");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesInternetExplorerTogetherTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "internetexplorer");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesExplorerOnlyTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "explorer");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesInternetExplorerTypoTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "interner explorer");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesPhantomJsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.PHANTOMJS_BROWSER_MODE);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.PHANTOMJS_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesPHTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "ph");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.PHANTOMJS_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesPhantomTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "phantom");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.PHANTOMJS_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesPhantomSpaceJsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "phantom js");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.PHANTOMJS_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesPhantomJsCapsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, "phantomJS");
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.PHANTOMJS_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserTypesNoBrowserTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.NONE_BROWSER_MODE);
        GridUtils.initBrowserType();
        Assert.assertEquals(FrameworkConstants.NONE_BROWSER_MODE, System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY));
    }

    @Test
    public void initBrowserVersionNullTest() {
        System.clearProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY);
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("", actualBrowserVersion);
    }

    @Test
    public void initBrowserVersionBlankTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY, "");
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("", actualBrowserVersion);
    }

    @Test
    public void initBrowserVersionPerfectTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY, "37.0");
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("37.0", actualBrowserVersion);
    }

    @Test
    public void initBrowserVersion2DigitWithPeriodTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY, "37.");
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("37.0", actualBrowserVersion);
    }

    @Test
    public void initBrowserVersion2DigitWithoutPeriodTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY, "37");
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("37.0", actualBrowserVersion);
    }

    @Test
    public void initBrowserVersionTryWhateverUserEnteredTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY, "1000");
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("1000", actualBrowserVersion);
    }

    @Test
    public void initBrowserVersionSingleDigitMajorTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY, "2.0");
        String actualBrowserVersion = GridUtils.initBrowserVersion();
        Assert.assertEquals("2.0", actualBrowserVersion);
    }

    @Test
    public void initPlatformTypeWindowsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.WINDOWS.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeWin10Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "win10");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.WIN10.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeWin7Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "win7");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.VISTA.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeLowercaseWindows8Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows 8");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.WIN8.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeCapitalWindows8Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "Windows 8");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.WIN8.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeWindows10Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows 10");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("WIN10", actualPlatform);
    }

    @Test
    public void initPlatformTypeCapitalWindows10Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "Windows 10");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("WIN10", actualPlatform);
    }

    @Test
    public void initPlatformTypeWindows8Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows 8");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.WIN8.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeWindows81Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows 8.1");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.WIN8_1.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeWindows7Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "windows 7");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.VISTA.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeCapitalWindows7Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "Windows 7");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.VISTA.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeCapitalXPTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "XP");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.XP.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeXPTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "xp");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.XP.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeLinuxTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "linux");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.LINUX.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeOsxPerfectTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "os x 10.11");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeOsxWithoutOSVersionNumberTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "os x");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeOsxWithoutOSVersionNumberWithoutSpaceTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "osx");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeOsxWithoutOSVersionNumberWithoutSpaceCapsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "OSX");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeOsxWithVersionNumberTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "os x 10.9");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.9", actualPlatform);
    }

    @Test
    public void initPlatformTypeOsWithoutSpaceXWithVersionNumberTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "osx 10.9");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.9", actualPlatform);
    }

    @Test
    public void initPlatformTypeMacWithVersionNumberTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "mac 10.9");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.9", actualPlatform);
    }

    @Test
    public void initPlatformTypeMacWithVersionDefaultTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "mac 10.11");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeMacWithoutVersionNumberTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "mac");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeMacWithoutVersionNumberCapsTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "MAC");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals("OS X 10.11", actualPlatform);
    }

    @Test
    public void initPlatformTypeNullTest() {
        System.clearProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY);
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.ANY.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeBlankTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, "");
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.ANY.toString(), actualPlatform);
    }

    @Test
    public void initPlatformTypeAnyTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY, Platform.ANY.toString());
        String actualPlatform = GridUtils.initPlatformType();
        Assert.assertEquals(Platform.ANY.toString(), actualPlatform);
    }

    @Test
    public void initBrowserResolutionDefaultResTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, "400x400");
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_DEFAULT, actualResolution);
    }

    @Test
    public void initBrowserResolution800X600Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, FrameworkConstants.BROWSER_RESOLUTION_800X600);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_800X600, actualResolution);
    }

    @Test
    public void initBrowserResolution1024x768Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, FrameworkConstants.BROWSER_RESOLUTION_DEFAULT);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_DEFAULT, actualResolution);
    }

    @Test
    public void initBrowserResolution1152X864Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, FrameworkConstants.BROWSER_RESOLUTION_1152X864);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1152X864, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X800Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, FrameworkConstants.BROWSER_RESOLUTION_1280X800);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1280X800, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X960Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, FrameworkConstants.BROWSER_RESOLUTION_1280X960);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1280X960, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X1024Test() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, FrameworkConstants.BROWSER_RESOLUTION_1280X1024);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1280X1024, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X1024OffByOneNumberTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, "1280x1023");
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_DEFAULT, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X1024SpacesInResTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, "1280 x 1024");
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1280X1024, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X1024MultiSpacesInResTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, " 1280 x 1024 ");
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1280X1024, actualResolution);
    }

    @Test
    public void initBrowserResolution1280X1024SingleSpaceInResTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, "1280x 1024");
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_1280X1024, actualResolution);
    }

    @Test
    public void initBrowserResolutionNullTest() {
        System.clearProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY);
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_DEFAULT, actualResolution);
    }

    @Test
    public void initBrowserResolutionNoneTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY, "");
        String actualResolution = GridUtils.initBrowserResolution();
        Assert.assertEquals(FrameworkConstants.BROWSER_RESOLUTION_DEFAULT, actualResolution);
    }

    @Test
    public void initTimeZoneDefaultTimeZoneTest() {
        System.clearProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT);
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals(FrameworkConstants.TIME_ZONE_DEFAULT, actualTimeZone);
    }

    @Test
    public void initTimeZoneUserDefinedExactTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT, "Los Angeles");
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals("Los Angeles", actualTimeZone);
    }

    @Test
    public void initTimeZoneUserDefinedWithSpacesTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT, "America/Los_Angeles");
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals("Los Angeles", actualTimeZone);
    }

    @Test
    public void initTimeZoneUserDefinedWithManySpacesTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT, "/ Los_Angeles ");
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals("Los Angeles", actualTimeZone);
    }

    @Test
    public void initTimeZoneUserDefinedWithoutSpacesTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT, "America/Anchorage");
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals("Anchorage", actualTimeZone);
    }

    @Test
    public void initTimeZoneUserDefinedMultiWordTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT, "America/North_Dakota/New_Salem");
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals("New Salem", actualTimeZone);
    }

    @Test
    public void initTimeZoneInvalidUserDefinedTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT, "World/NotARealTimeZone_Here");
        String actualTimeZone = GridUtils.initTimeZone();
        Assert.assertEquals(FrameworkConstants.TIME_ZONE_DEFAULT, actualTimeZone);
    }

}
