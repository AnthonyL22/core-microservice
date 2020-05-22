package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class ButtonInputElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)
                        && StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.BUTTON.type))
                        || (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)
                                        && StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.IMAGE.type))
                        || (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)
                                        && StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.SUBMIT.type));
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        webAction(webElement);
    }

    public void webAction(final WebElement webElement) {
        try {
            if (StringUtils.equalsIgnoreCase(webElement.getTagName(), WebElementType.INPUT.type)
                            && StringUtils.equalsIgnoreCase(webElement.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.SUBMIT.type)) {
                LOG(true, "Submit BUTTON %s", getButtonText(webElement));
                webElement.submit();
            } else {
                LOG(true, "Click BUTTON %s", getButtonText(webElement));
                webElement.click();
            }
        } catch (Exception e) {
            assertFail("Failed to Select BUTTON due to exception=%s", e.getMessage());
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
