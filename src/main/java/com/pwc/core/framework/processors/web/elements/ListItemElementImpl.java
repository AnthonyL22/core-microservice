package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.logging.LoggerService.LOG;
import static com.pwc.assertion.AssertService.assertFail;

public class ListItemElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.LI.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        webAction(webElement);
    }

    public void webAction(final WebElement webElement) {
        try {
            LOG(true, "Select LISTITEM %s", !StringUtils.isEmpty(webElement.getText()) ? String.format(":: text='%s'", webElement.getText()) : "");
            webElement.click();
        } catch (Exception e) {
            assertFail("Failed to Select LISTITEM due to exception=%s", e.getMessage());
        }
    }
}
