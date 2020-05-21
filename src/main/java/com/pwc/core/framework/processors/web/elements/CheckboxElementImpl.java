package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class CheckboxElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)
                        && StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.CHECKBOX.type);
    }

    public void webAction(final WebElement webElement, final Object state) {
        try {
            LOG(true, "Select CHECKBOX :: text='%s'", Boolean.valueOf(state.toString()));
            if (webElement.isSelected() != Boolean.valueOf(state.toString())) {
                webElement.click();
            }
        } catch (Exception e) {
            assertFail("Failed to Select CHECKBOX due to exception=%s", e.getMessage());
        }
    }
}
