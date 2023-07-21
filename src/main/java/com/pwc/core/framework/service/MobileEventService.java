package com.pwc.core.framework.service;

import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.data.MobileGesture;
import com.pwc.core.framework.data.XCUIElementAttribute;
import com.pwc.core.framework.data.XCUIElementType;
import com.pwc.core.framework.driver.MicroserviceMobileDriver;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.pwc.logging.service.LoggerService.LOG;

@Data
@Component
public class MobileEventService extends WebEventController {

    private MicroserviceMobileDriver microserviceMobileDriver;
    private long timeOutInSeconds;
    private long sleepInMillis;
    private long pageTimeoutInSeconds;
    private boolean videoCaptureEnabled;
    private boolean waitForAjaxRequestsEnabled;
    private String url;

    private static final String REGEX_XPATH_FINDER = ".*[\\[@'].*";
    private static final String PREDICATE_XPATH_FINDER = ".*[=='].*";

    public MobileEventService() {
    }

    public MobileEventService(MicroserviceMobileDriver driver) {

        this.microserviceMobileDriver = driver;
    }

    /**
     * Find WebElement by either XPath or IosNsPredicate.
     *
     * @param elementIdentifier unique element identifying locator
     * @return WebElement to then be used to interact with the AUT
     */
    public WebElement findMobileElement(final String elementIdentifier) {

        final String VARIABLE_ELEMENT_TYPE_PATH = "type == '%s' and (name == '%s' or label == '%s' or elementId == '%s')";
        final String VARIABLE_ELEMENT_ATTRIBUTE_PATH = "%s == '%s'";

        WebElement webElement;
        if (StringUtils.startsWith(elementIdentifier, "//") && elementIdentifier.matches(REGEX_XPATH_FINDER)) {

            webElement = findElementByXPath(elementIdentifier);

        } else if (elementIdentifier.matches(PREDICATE_XPATH_FINDER)) {

            webElement = findElementByIosNsPredicate(Arrays.asList(elementIdentifier));

        } else {

            List<String> predicateLocatorList = new ArrayList<>();
            Stream.of(XCUIElementAttribute.values())
                            .forEach(elementAttribute -> predicateLocatorList.add(String.format(VARIABLE_ELEMENT_ATTRIBUTE_PATH, elementAttribute.attribute, elementIdentifier)));
            Stream.of(XCUIElementType.values())
                            .forEach(elementType -> predicateLocatorList.add(String.format(VARIABLE_ELEMENT_TYPE_PATH, elementType.type, elementIdentifier, elementIdentifier, elementIdentifier)));
            webElement = findElementByIosNsPredicate(predicateLocatorList);

        }
        return webElement;
    }

    /**
     * Find WebElement by IosNsPredicate based on a given list of possible matches.
     *
     * @param idsNsPredicateIdentifiers list of Predicate paths to search for element with
     * @return WebElement to then be used to interact with the AUT
     */
    private WebElement findElementByIosNsPredicate(List<String> idsNsPredicateIdentifiers) {

        String elementIdentifier = "";
        WebElement element = null;
        try {
            for (String identifier : idsNsPredicateIdentifiers) {
                try {
                    element = (WebElement) this.microserviceMobileDriver.findElement(By.id(identifier));
                } catch (Exception elementNotFound) {
                    elementNotFound.getMessage();
                }
                if (null != element) {
                    return element;
                }
            }
        } catch (Exception e) {
            LOG(false, "Unable to find element '%s' by Predicate", elementIdentifier);
        }
        return element;
    }

    /**
     * Backup way of getting a <code>WebElement</code> which uses Selenium's parser.
     *
     * @param elementIdentifier xpath to search for element with
     * @return WebElement to then be used to interact with the AUT
     */
    private WebElement findElementByXPath(final String elementIdentifier) {

        WebElement webElement = null;
        try {
            webElement = this.microserviceMobileDriver.findElement(By.xpath(elementIdentifier));
        } catch (Exception e) {
            LOG(false, "Unable to find element '%s' by xPath", elementIdentifier);
        }
        return webElement;
    }

    /**
     * Executes JSONWP command and returns a response.
     *
     * @param elementIdentifier unique element identifying locator
     * @param mobileGesture     Mobile Gesture to perform
     * @param parameters        gesture parameters to execute
     * @return a result response
     */
    public Object executeJavascript(final String elementIdentifier, MobileGesture mobileGesture, Map<String, Object> parameters) {

        Object response = null;
        try {
            WebElement webElement = findMobileElement(elementIdentifier);
            parameters.put("element", webElement);
            response = executeJavascript(mobileGesture, parameters);
        } catch (Exception e) {
            e.getMessage();
        }
        return response;
    }

    /**
     * Executes JSONWP command and returns a response.
     *
     * @param mobileGesture Mobile Gesture to perform
     * @param parameters    gesture parameters to execute
     * @return a result response
     */
    public Object executeJavascript(MobileGesture mobileGesture, Map<String, Object> parameters) {

        Object response = null;
        try {
            JavascriptExecutor js = (JavascriptExecutor) this.microserviceMobileDriver;
            response = js.executeScript("mobile: " + mobileGesture.gesture, parameters);
        } catch (Exception e) {
            e.getMessage();
        }
        return response;
    }

    /**
     * Activates the given app if it installed, but not running or if it is running in the
     * background.
     *
     * @param bundleId the bundle identifier (or app id) of the app to activate.
     */
    public void activateApp(final String bundleId) {

        //this.microserviceMobileDriver.activateApp(bundleId);
    }

    /**
     * Resets the currently running app together with the session.
     */
    public void resetApp() {

        //this.microserviceMobileDriver.resetApp();
    }

    /**
     * Remove the specified app from the device (uninstall).
     *
     * @param bundleId the bundle identifier (or app id) of the app to remove.
     */
    public void removeApp(final String bundleId) {

        //this.microserviceMobileDriver.removeApp(bundleId);
    }

}
