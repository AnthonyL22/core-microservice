package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.processors.web.elements.custom.MatSelect;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class MatSelectableElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.MAT_SELECT.type)
                        || StringUtils.containsIgnoreCase(element.getAttribute(WebElementAttribute.CLASS.attribute), WebElementType.MAT_SELECT.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Choose MAT-SELECT %s", attributeValue != null ? String.format(":: value='%s'", attributeValue) : "");
            MatSelect matSelectDropDown = new MatSelect(webElement);
            try {
                matSelectDropDown.deselectAll();
            } catch (Exception eatMessage) {
                eatMessage.getMessage();
            }

            if (StringUtils.containsAny(attributeValue.toString(), ",")) {
                String[] itemsToSelect = StringUtils.split(attributeValue.toString(), ",");
                for (Object item : itemsToSelect) {
                    matSelectDropDown.selectByVisibleText(item.toString().trim());
                }
            } else {
                matSelectDropDown.selectByVisibleText(attributeValue.toString());
            }
        } catch (Exception e) {
            assertFail("Failed to choose from MAT-SELECT due to exception=%s", e.getMessage());
        }
    }
}
