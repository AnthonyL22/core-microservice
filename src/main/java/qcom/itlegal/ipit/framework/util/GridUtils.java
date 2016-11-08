package qcom.itlegal.ipit.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import qcom.itlegal.ipit.framework.FrameworkConstants;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridUtils {

    private static final Pattern FIREFOX_REGEX = Pattern.compile("^.*?(ff|firefox)+?");
    private static final Pattern CHROME_REGEX = Pattern.compile("^.*?(ch|chrome)+?");
    private static final Pattern ANDROID_REGEX = Pattern.compile("^.*?(droid|appium|android|google)+?");
    private static final Pattern IOS_REGEX = Pattern.compile("^.*?(iphone|ipad)+?");
    private static final Pattern INTERNET_EXPLORER_REGEX = Pattern.compile("^.*?(ie|internet\\sexplorer|explorer)+?");
    private static final Pattern EDGE_EXPLORER_REGEX = Pattern.compile("^.*?(ie|ms\\sedge|edge|msedge)+?");
    private static final Pattern SAFARI_REGEX = Pattern.compile("^.*?(safari|sf)+?");
    private static final Pattern PHANTOM_JS_REGEX = Pattern.compile("^.*?(ph|phantom|phantomjs|phantom\\sjs)+?");
    private static final Pattern NO_BROWSER_REGEX = Pattern.compile("^.*?(none)+?");

    private static final Pattern VERSION_IDEAL_REGEX = Pattern.compile("^\\d{2}\\.\\d{1}$");
    private static final Pattern VERSION_MISSING_MINOR_WITH_PERIOD_REGEX = Pattern.compile("^\\d{2}\\.$");
    private static final Pattern VERSION_MISSING_MINOR_WITHOUT_PERIOD_REGEX = Pattern.compile("^\\d{2}$");

    private static final Pattern WINDOWS_PLATFORMS_REGEX = Pattern.compile("^.*?[Ww]in.*?|xp|XP$");
    private static final Pattern LINUX_PLATFORMS_REGEX = Pattern.compile("^.*?(linux)+?");
    private static final Pattern MAC_PLATFORMS_REGEX = Pattern.compile("^.*?(mac|osx|os\\sx)+(\\s\\d+)?");
    private static final Pattern IOS_PLATFORMS_REGEX = Pattern.compile("^.*?(ios|iphone|ipad)+(\\s\\d+)?");
    public static final String DEFAULT_OSX_PLATFORM_STRING = "OS X 10.11";

    /**
     * Get and set the browser type potentially defined by the user
     */
    public static void initBrowserType() {

        Matcher browserMatcher;
        String property = StringUtils.trim(StringUtils.lowerCase(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY)));

        if (StringUtils.isEmpty(property) || StringUtils.containsIgnoreCase(property, Platform.ANY.toString())) {
            System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
            return;
        } else {
            browserMatcher = ANDROID_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.ANDROID_MODE);
                return;
            }
            browserMatcher = IOS_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.IOS_MODE);
                return;
            }
            browserMatcher = FIREFOX_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.FIREFOX_BROWSER_MODE);
                return;
            }
            browserMatcher = CHROME_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
                return;
            }
            browserMatcher = INTERNET_EXPLORER_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.INTERNET_EXPLORER_BROWSER_MODE);
                return;
            }
            browserMatcher = EDGE_EXPLORER_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.EDGE_BROWSER_MODE);
                return;
            }
            browserMatcher = SAFARI_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.SAFARI_BROWSER_MODE);
                return;
            }
            browserMatcher = PHANTOM_JS_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.PHANTOMJS_BROWSER_MODE);
                return;
            }
            browserMatcher = NO_BROWSER_REGEX.matcher(property);
            if (browserMatcher.find()) {
                System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.NONE_BROWSER_MODE);
                return;
            }
        }
        System.setProperty(FrameworkConstants.AUTOMATION_BROWSER_PROPERTY, FrameworkConstants.CHROME_BROWSER_MODE);
    }

    /**
     * Get the Browser version in which the user would like to run against
     *
     * @return cleaned version number
     */
    public static String initBrowserVersion() {

        Matcher versionMatcher;
        String property = StringUtils.trim(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY));
        if (StringUtils.isEmpty(property)) {
            return "";
        }

        if (!StringUtils.isEmpty(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_VERSION_PROPERTY))) {
            versionMatcher = VERSION_IDEAL_REGEX.matcher(property);
            if (versionMatcher.find()) {
                return versionMatcher.group(0);
            }
            versionMatcher = VERSION_MISSING_MINOR_WITH_PERIOD_REGEX.matcher(property);
            if (versionMatcher.find()) {
                return versionMatcher.group(0) + "0";
            }
            versionMatcher = VERSION_MISSING_MINOR_WITHOUT_PERIOD_REGEX.matcher(property);
            if (versionMatcher.find()) {
                return versionMatcher.group(0) + ".0";
            }

        }
        return property;
    }

    /**
     * Get the Platform name in which the user would like to run on
     *
     * @return Platform name
     */
    public static String initPlatformType() {

        Matcher platformMatcher;
        String property = StringUtils.trim(StringUtils.lowerCase(System.getProperty(FrameworkConstants.AUTOMATION_PLATFORM_PROPERTY)));

        if (StringUtils.isEmpty(property)) {
            return Platform.ANY.toString();
        } else if (StringUtils.containsIgnoreCase(property, Platform.ANY.toString())) {
            return Platform.ANY.toString();
        } else {
            platformMatcher = WINDOWS_PLATFORMS_REGEX.matcher(property);
            if (platformMatcher.find()) {
                try {
                    if (Platform.fromString(property) != null) {
                        return Platform.fromString(property).toString();
                    } else {
                        return property;
                    }
                } catch (Exception e) {
                    return property;
                }
            }

            /**
             * if osx is set then use 'os x VERSION here"
             */

            platformMatcher = LINUX_PLATFORMS_REGEX.matcher(property);
            if (platformMatcher.find()) {
                try {
                    if (Platform.fromString(property) != null) {
                        return Platform.fromString(property).toString();
                    } else {
                        return property;
                    }
                } catch (Exception e) {
                    return property;
                }
            }

            platformMatcher = MAC_PLATFORMS_REGEX.matcher(property);
            if (platformMatcher.find()) {
                try {
                    if (Platform.fromString(property) != null) {
                        if (property.matches("^.+?\\-?\\d+\\.\\d+$")) {
                            return property.toUpperCase();
                        } else {
                            return DEFAULT_OSX_PLATFORM_STRING;
                        }
                    } else {
                        return property;
                    }
                } catch (Exception e) {
                    if (StringUtils.containsIgnoreCase(property, "osx")) {
                        if (StringUtils.equalsIgnoreCase(property, "osx")) {
                            return DEFAULT_OSX_PLATFORM_STRING;
                        } else {
                            return StringUtils.replaceOnce(property, "osx", "OS X");
                        }
                    } else if (StringUtils.containsIgnoreCase(property, "mac")) {
                        return StringUtils.replaceOnce(property, "mac", "OS X");
                    } else {
                        return property.toUpperCase();
                    }
                }
            }
            platformMatcher = IOS_PLATFORMS_REGEX.matcher(property);
            if (platformMatcher.find()) {
                try {
                    if (Platform.fromString(property) != null) {
                        return Platform.fromString(property).toString();
                    } else {
                        return property;
                    }
                } catch (Exception e) {
                    return property;
                }
            }
        }
        return Platform.ANY.toString();
    }

    /**
     * Get the Browser resolution in which the user would like to run against based on
     * seven common resolutions currently supported
     * <p/>
     * Default resolution is 1024 x 768
     *
     * @return matching browser resolution
     */
    public static String initBrowserResolution() {
        String property = StringUtils.remove(StringUtils.trim(StringUtils.lowerCase(System.getProperty(FrameworkConstants.AUTOMATION_BROWSER_RESOLUTION_PROPERTY))), " ");
        if (StringUtils.isEmpty(property)) {
            return FrameworkConstants.BROWSER_RESOLUTION_DEFAULT;
        }

        switch (property) {
            case (FrameworkConstants.BROWSER_RESOLUTION_800X600): {
                return FrameworkConstants.BROWSER_RESOLUTION_800X600;
            }
            case (FrameworkConstants.BROWSER_RESOLUTION_1024X768): {
                return FrameworkConstants.BROWSER_RESOLUTION_1024X768;
            }
            case (FrameworkConstants.BROWSER_RESOLUTION_1152X864): {
                return FrameworkConstants.BROWSER_RESOLUTION_1152X864;
            }
            case (FrameworkConstants.BROWSER_RESOLUTION_1280X800): {
                return FrameworkConstants.BROWSER_RESOLUTION_1280X800;
            }
            case (FrameworkConstants.BROWSER_RESOLUTION_1280X960): {
                return FrameworkConstants.BROWSER_RESOLUTION_1280X960;
            }
            case (FrameworkConstants.BROWSER_RESOLUTION_1280X1024): {
                return FrameworkConstants.BROWSER_RESOLUTION_1280X1024;
            }
            case (FrameworkConstants.BROWSER_RESOLUTION_NONE): {
                return "";
            }
            default: {
                return FrameworkConstants.BROWSER_RESOLUTION_DEFAULT;
            }
        }
    }

    /**
     * Get the Timezone in which the user would like to run tests in on the Sauce Labs VMs
     * <p/>
     * Default resolution is "Los Angeles"
     *
     * @return matching browser resolution
     */
    public static String initTimeZone() {
        String property = System.getProperty(FrameworkConstants.AUTOMATION_TIME_ZONE_ENVIRONMENT);
        if (isValidTimeZone(property)) {
            property = StringUtils.replace(StringUtils.trim(property), "_", " ");
            if (StringUtils.containsAny(property, "/")) {
                property = StringUtils.substringAfterLast(property, "/");
            }
            return property;
        } else {
            return FrameworkConstants.TIME_ZONE_DEFAULT;
        }
    }

    /**
     * Validate if the timezone entered is valid
     *
     * @param id time zone entered
     * @return true/false is valid
     */
    private static boolean isValidTimeZone(String id) {
        for (String timeZoneId : TimeZone.getAvailableIDs()) {
            if (timeZoneId.equals(id))
                return true;
        }
        return false;
    }

}
