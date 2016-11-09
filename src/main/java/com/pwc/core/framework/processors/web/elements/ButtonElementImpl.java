package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.logging.LoggerService.LOG;
import static com.pwc.assertion.AssertService.assertFail;

public class ButtonElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.BUTTON.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        webAction(webElement);
    }

    public void webAction(final WebElement webElement) {
        try {
            LOG(true, "Click BUTTON %s", getButtonText(webElement));
            webElement.click();
        } catch (Exception e) {
            assertFail("Failed to Click BUTTON due to exception=%s", e.getMessage());
        }
    }

    public String getButtonText(WebElement webElement) {
        String buttonText = "";
        if (!StringUtils.isEmpty(webElement.getText())) {
            buttonText = String.format(":: text='%s'", webElement.getText());
        } else if (!StringUtils.isEmpty(webElement.getAttribute(WebElementAttribute.VALUE.attribute))) {
            buttonText = String.format(":: value='%s'", webElement.getAttribute(WebElementAttribute.VALUE.attribute));
        } else if (!StringUtils.isEmpty(webElement.getAttribute(WebElementAttribute.ID.attribute))) {
            buttonText = String.format(":: id='%s'", webElement.getAttribute(WebElementAttribute.ID.attribute));
        }
        return buttonText;
    }

}
