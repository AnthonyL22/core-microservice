package com.pwc.core.framework.service;

import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.driver.MicroserviceMobileDriver;
import io.appium.java_client.MobileElement;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
     * Find WebElement by either XPath or CSS Selector.
     *
     * @param elementIdentifier unique element identifying string
     * @return MobileElement to then be used to interact with the AUT
     */
    public MobileElement findWebElement(final String elementIdentifier) {

        MobileElement mobileElement = null;
        if (StringUtils.startsWith(elementIdentifier, "//") && elementIdentifier.matches(REGEX_XPATH_FINDER)) {
            mobileElement = findElementByXPath(elementIdentifier);
        } else if (elementIdentifier.matches(PREDICATE_XPATH_FINDER)) {
            mobileElement = findElementByIosNsPredicate(Arrays.asList(elementIdentifier));
        } else {

            List<String> elementXPathLocators = new ArrayList<>();
            elementXPathLocators.add(elementIdentifier);
            elementXPathLocators.add("type == 'XCUIElementTypeStaticText' and name == '" + elementIdentifier + "'");
            elementXPathLocators.add("type == 'XCUIElementTypeApplication' and name == '" + elementIdentifier + "'");
            elementXPathLocators.add("elementId == '" + elementIdentifier + "'");
            elementXPathLocators.add("type == '" + elementIdentifier + "'");
            elementXPathLocators.add("name == '" + elementIdentifier + "'");
            elementXPathLocators.add("label == '" + elementIdentifier + "'");
            elementXPathLocators.add("enabled == '" + elementIdentifier + "'");
            elementXPathLocators.add("visible == '" + elementIdentifier + "'");
            mobileElement = findElementByIosNsPredicate(elementXPathLocators);
        }
        return mobileElement;
    }

    /**
     * Find WebElement by IosNsPredicate
     *
     * @param idsNsPredicateIdentifiers list of Predicate paths to search for element with
     * @return MobileElement to then be used to interact with the AUT
     */
    private MobileElement findElementByIosNsPredicate(List<String> idsNsPredicateIdentifiers) {

        AtomicReference<MobileElement> mobileElement = null;

        idsNsPredicateIdentifiers.forEach(identifier -> {
            MobileElement x = (MobileElement) this.microserviceMobileDriver.findElementByIosNsPredicate(identifier);
            System.out.println();

        });

        idsNsPredicateIdentifiers.stream().fil(identifier -> {
            MobileElement x = (MobileElement) this.microserviceMobileDriver.findElementByIosNsPredicate(identifier);
            System.out.println();

        });

        try {
            idsNsPredicateIdentifiers.forEach(identifier -> mobileElement.set((MobileElement) this.microserviceMobileDriver.findElementByIosNsPredicate(identifier)));
        } catch (Exception e) {
            LOG(false, "Unable to find element by iOSNsPredicate due to exception %s", e.getCause());
        }
        return mobileElement.get();
    }

    /**
     * Backup way of getting a <code>WebElement</code> which uses Selenium's parser
     *
     * @param elementIdentifier xpath to search for element with
     * @return MobileElement to then be used to interact with the AUT
     */
    private MobileElement findElementByXPath(final String elementIdentifier) {

        MobileElement mobileElement = null;
        try {
//            mobileElement = this.microserviceMobileDriver.findElementByXPath(elementIdentifier);
        } catch (Exception e) {
            LOG(false, "Unable to find element '%s' by xPath", elementIdentifier);
        }
        return mobileElement;
    }

}
