package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.logging.LoggerService.LOG;
import static com.pwc.assertion.AssertService.assertFail;

public class ImageElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.IMG.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        webAction(webElement);
    }

    public void webAction(final WebElement webElement) {
        try {
            LOG(true, "Click %s", "IMAGE");
            webElement.click();
        } catch (Exception e) {
            assertFail("Failed to Click IMAGE due to exception=%s", e.getMessage());
        }
    }
}
