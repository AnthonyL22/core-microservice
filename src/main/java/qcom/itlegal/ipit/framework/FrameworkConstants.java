package qcom.itlegal.ipit.framework;

public interface FrameworkConstants {

    // Automation Properties
    String AUTOMATION_TEST_ENVIRONMENT = "test.env";
    String AUTOMATION_TIME_ZONE_ENVIRONMENT = "time.zone";
    String AUTOMATION_BROWSER_PROPERTY = "browser";
    String SAUCELABS_BROWSER_PROPERTY = "SELENIUM_BROWSER";
    String AUTOMATION_BROWSER_VERSION_PROPERTY = "browser.version";
    String SAUCELABS_BROWSER_VERSION_PROPERTY = "SELENIUM_VERSION";
    String SAUCELABS_TUNNEL_IDENTIFIER_PROPERTY = "TUNNEL_IDENTIFIER";
    String SAUCELABS_ONDEMAND_BROWSERS_PROPERTY = "SAUCE_ONDEMAND_BROWSERS";
    String AUTOMATION_BROWSER_RESOLUTION_PROPERTY = "browser.resolution";
    String AUTOMATION_PLATFORM_PROPERTY = "platform";
    String AUTOMATION_OS_PROPERTY = "os";
    String AUTOMATION_LONG_NAME_PROPERTY = "longName";
    String AUTOMATION_LONG_VERSION_PROPERTY = "longVersion";
    String AUTOMATION_DEVICE_NAME_PROPERTY = "deviceName";
    String AUTOMATION_ORIENTATION_PROPERTY = "deviceOrientation";
    String SAUCELABS_PLATFORM_PROPERTY = "SELENIUM_PLATFORM";

    // System Properties Supported
    String SEPARATOR = System.getProperty("file.separator");
    String AUTOMATION_CONTEXT_XML = "automation-context.xml";
    String SYSTEM_OS_NAME = "os.name";
    String SYSTEM_TEST_ENVIRONMENT_DIR = "config/";
    String SYSTEM_JVM_TYPE = "sun.arch.data.model";
    String SYSTEM_DEFAULT_TIMEZONE = "PST";
    String SYSTEM_USER_TIMEZONE = "user.timezone";
    String DEV_ENVIRONMENT_KEY = "dev";

    // Default Script Execution Modes
    String FIREFOX_BROWSER_MODE = "ff";
    String CHROME_BROWSER_MODE = "ch";
    String ANDROID_MODE = "an";
    String IOS_MODE = "ios";
    String INTERNET_EXPLORER_BROWSER_MODE = "ie";
    String EDGE_BROWSER_MODE = "edge";
    String SAFARI_BROWSER_MODE = "safari";
    String PHANTOMJS_BROWSER_MODE = "phantomjs";
    String NONE_BROWSER_MODE = "none";

    // Selenium Drivers
    String WEB_DRIVER_IE = "webdriver.ie.driver";
    String WEB_DRIVER_CHROME = "webdriver.chrome.driver";
    String WEB_DRIVER_GECKO = "webdriver.gecko.driver";

    // DEFAULT Date Patterns
    String DATETIME_LOGGER_DATETIME_PATTER = "yyyy-MM-dd HH:mm:ss.SSS";
    String SYSTEM_DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String SHORT_DATE_PATTERN = "MM/dd/yyyy";

    // Default Capability Name
    String SCRIPT_NAME_CAPABILITY = "name";
    String TUNNEL_IDENTIFIER_CAPABILITY = "tunnelIdentifier";
    String TIME_ZONE_CAPABILITY = "timeZone";
    String SCREEN_RESOLUTION_CAPABILITY = "screenResolution";
    String TIME_ZONE_DEFAULT = "Los Angeles";
    String BROWSER_RESOLUTION_DEFAULT = "1024x768";
    String BROWSER_RESOLUTION_800X600 = "800x600";
    String BROWSER_RESOLUTION_1024X768 = "1024x768";
    String BROWSER_RESOLUTION_1152X864 = "1152x864";
    String BROWSER_RESOLUTION_1280X800 = "1280x800";
    String BROWSER_RESOLUTION_1280X960 = "1280x960";
    String BROWSER_RESOLUTION_1280X1024 = "1280x1024";
    String BROWSER_RESOLUTION_NONE = "none";

    // Default Timing Settings
    long DEFAULT_POLLING_DURATION = 2000;

    // WS Request Types
    String POST_REQUEST = "post";
    String PUT_REQUEST = "put";
    String GET_REQUEST = "get";
    String DELETE_REQUEST = "delete";

    // HttpClient Request Mappings
    String HTTP_STATUS_VALUE_KEY = "httpStatusValue";
    String HTTP_HEADERS_KEY = "httpHeaders";
    String HTTP_STATUS_REASON_PHRASE_KEY = "httpStatusReasonPhrase";
    String HTTP_ENTITY_KEY = "httpEntity";
    String HTTP_RESPONSE_TIME_KEY = "httpResponseTime";
    String STATUS_KEY = "STATUS";
    String ENTITY_KEY = "ENTITY";

    // Javascript Snippets (deprecated - use JavascriptConstants)
    String JAVASCRIPT_CLICK_ELEMENT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; element.click();";
    String JAVASCRIPT_ALERT_ELEMENT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; alert(element.textContent);";
    String JAVASCRIPT_CLICK_ELEMENT_BY_ID = "document.getElementById('%s').click();";
    String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_AND_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].value == '%s') elements[i].click();}";
    String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_NODE_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.nodeValue == '%s') elements[i].click();}";
    String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_TYPE_AND_EQUALS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent == '%s') elements[i].click()}";
    String JAVASCRIPT_CLICK_ELEMENT_BY_TAG_TYPE_AND_CONTAINS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    String JAVASCRIPT_CLICK_ELEMENT_BY_CLASS_NAME_AND_CONTAINS_VALUE = "var elements = document.getElementsByClassName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    String JAVASCRIPT_CLICK_LAST_ELEMENT_BY_CLASS_NAME = "var elements = document.getElementsByClassName('%s'); elements[elements.length - 1].click();";


}
