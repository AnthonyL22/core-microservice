package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class TextInputElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return ((StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)
                        && !(StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.CHECKBOX.type)
                                        || StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.RADIO.type))));
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        LOG(true, "Enter INPUT :: value='%s'", attributeValue);
        try {
            webElement.clear();
            webElement.sendKeys(attributeValue.toString());
        } catch (InvalidElementStateException e) {
            webElement.sendKeys(attributeValue.toString());
        } catch (Exception e) {
            assertFail("Failed to Enter INPUT due to exception=%s", e.getMessage());
        }
    }
}
