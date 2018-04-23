package com.pwc.core.framework;

public final class FrameworkConstants {

    // Automation Properties
    public static final String AUTOMATION_TEST_ENVIRONMENT = "test.env";
    public static final String AUTOMATION_TIME_ZONE_ENVIRONMENT = "time.zone";
    public static final String AUTOMATION_BROWSER_PROPERTY = "browser";
    public static final String SAUCELABS_BROWSER_PROPERTY = "SELENIUM_BROWSER";
    public static final String AUTOMATION_BROWSER_VERSION_PROPERTY = "browser.version";
    public static final String SAUCELABS_BROWSER_VERSION_PROPERTY = "SELENIUM_VERSION";
    public static final String SAUCELABS_TUNNEL_IDENTIFIER_PROPERTY = "TUNNEL_IDENTIFIER";
    public static final String SAUCELABS_ONDEMAND_BROWSERS_PROPERTY = "SAUCE_ONDEMAND_BROWSERS";
    public static final String AUTOMATION_BROWSER_RESOLUTION_PROPERTY = "browser.resolution";
    public static final String AUTOMATION_PLATFORM_PROPERTY = "platform";
    public static final String AUTOMATION_OS_PROPERTY = "os";
    public static final String AUTOMATION_LONG_NAME_PROPERTY = "longName";
    public static final String AUTOMATION_LONG_VERSION_PROPERTY = "longVersion";
    public static final String AUTOMATION_DEVICE_NAME_PROPERTY = "deviceName";
    public static final String AUTOMATION_ORIENTATION_PROPERTY = "deviceOrientation";
    public static final String SAUCELABS_PLATFORM_PROPERTY = "SELENIUM_PLATFORM";

    // System Properties Supported
    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String AUTOMATION_CONTEXT_XML = "automation-context.xml";
    public static final String SYSTEM_OS_NAME = "os.name";
    public static final String MAC_SYSTEM_OS_NAME = "sw_vers";
    public static final String SYSTEM_TEST_ENVIRONMENT_DIR = "config/";
    public static final String SYSTEM_JVM_TYPE = "sun.arch.data.model";
    public static final String SYSTEM_DEFAULT_TIMEZONE = "PST";
    public static final String SYSTEM_USER_TIMEZONE = "user.timezone";
    public static final String DEV_ENVIRONMENT_KEY = "dev";

    // Default Script Execution Modes
    public static final String FIREFOX_BROWSER_MODE = "ff";
    public static final String CHROME_BROWSER_MODE = "ch";
    public static final String HEADLESS_CHROME_BROWSER_MODE = "headless";
    public static final String ANDROID_MODE = "an";
    public static final String IOS_MODE = "ios";
    public static final String INTERNET_EXPLORER_BROWSER_MODE = "ie";
    public static final String EDGE_BROWSER_MODE = "edge";
    public static final String SAFARI_BROWSER_MODE = "safari";
    public static final String PHANTOMJS_BROWSER_MODE = "phantomjs";
    public static final String NONE_BROWSER_MODE = "none";

    // Selenium Drivers
    public static final String WEB_DRIVER_IE = "webdriver.ie.driver";
    public static final String WEB_DRIVER_CHROME = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_GECKO = "webdriver.gecko.driver";
    public static final String WEB_DRIVER_EDGE = "webdriver.edge.driver";

    // DEFAULT Date Patterns
    public static final String DATETIME_LOGGER_DATETIME_PATTER = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String SYSTEM_DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String SHORT_DATE_PATTERN = "MM/dd/yyyy";

    // Default Capability Name
    public static final String SCRIPT_NAME_CAPABILITY = "name";
    public static final String TUNNEL_IDENTIFIER_CAPABILITY = "tunnelIdentifier";
    public static final String TIME_ZONE_CAPABILITY = "timeZone";
    public static final String SCREEN_RESOLUTION_CAPABILITY = "screenResolution";
    public static final String TIME_ZONE_DEFAULT = "Los Angeles";
    public static final String BROWSER_RESOLUTION_DEFAULT = "1024x768";
    public static final String BROWSER_RESOLUTION_800X600 = "800x600";
    public static final String BROWSER_RESOLUTION_1024X768 = "1024x768";
    public static final String BROWSER_RESOLUTION_1152X864 = "1152x864";
    public static final String BROWSER_RESOLUTION_1280X800 = "1280x800";
    public static final String BROWSER_RESOLUTION_1280X960 = "1280x960";
    public static final String BROWSER_RESOLUTION_1280X1024 = "1280x1024";
    public static final String BROWSER_RESOLUTION_NONE = "none";

    // Default Timing Settings
    public static final long DEFAULT_POLLING_DURATION = 2000;

    // WS Request Types
    public static final String POST_REQUEST = "post";
    public static final String PUT_REQUEST = "put";
    public static final String GET_REQUEST = "get";
    public static final String DELETE_REQUEST = "delete";

    // HttpClient Request Mappings
    public static final String HTTP_STATUS_VALUE_KEY = "httpStatusValue";
    public static final String HTTP_HEADERS_KEY = "httpHeaders";
    public static final String HTTP_STATUS_REASON_PHRASE_KEY = "httpStatusReasonPhrase";
    public static final String HTTP_ENTITY_KEY = "httpEntity";
    public static final String HTTP_RESPONSE_TIME_KEY = "httpResponseTime";
    public static final String STATUS_KEY = "STATUS";
    public static final String ENTITY_KEY = "ENTITY";

    /**
     * @deprecated use JavascriptConstants fields instead
     */
    @Deprecated
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; element.click();";
    public static final String JAVASCRIPT_ALERT_ELEMENT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; alert(element.textContent);";
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_ID = "document.getElementById('%s').click();";
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_AND_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].value == '%s') elements[i].click();}";
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_NODE_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.nodeValue == '%s') elements[i].click();}";
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_TYPE_AND_EQUALS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent == '%s') elements[i].click()}";
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_TYPE_AND_CONTAINS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    public static final String JAVASCRIPT_CLICK_ELEMENT_BY_CLASS_NAME_AND_CONTAINS_VALUE = "var elements = document.getElementsByClassName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    public static final String JAVASCRIPT_CLICK_LAST_ELEMENT_BY_CLASS_NAME = "var elements = document.getElementsByClassName('%s'); elements[elements.length - 1].click();";

    private FrameworkConstants() {
    }

}
