package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class SelectableElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.SELECT.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Choose SELECT %s", attributeValue != null ? String.format(":: value='%s'", attributeValue) : "");
            Select dropDownList = new Select(webElement);
            try {
                dropDownList.deselectAll();
            } catch (Exception eatMessage) {
                eatMessage.getMessage();
            }

            if (StringUtils.containsAny(attributeValue.toString(), ",")) {
                String[] itemsToSelect = StringUtils.split(attributeValue.toString(), ",");
                for (Object item : itemsToSelect) {
                    dropDownList.selectByVisibleText(item.toString().trim());
                }
            } else {
                dropDownList.selectByVisibleText(attributeValue.toString());
            }
        } catch (Exception e) {
            assertFail("Failed to choose from SELECT due to exception=%s", e.getMessage());
        }
    }
}
