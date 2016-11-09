package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.logging.LoggerService.LOG;
import static com.pwc.assertion.AssertService.assertFail;

public class TextAreaElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.TEXTAREA.type));
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Enter TEXTAREA :: value='%s'", attributeValue);
            webElement.clear();
            webElement.sendKeys(attributeValue.toString());
        } catch (Exception e) {
            assertFail("Failed to Enter into TEXTAREA due to exception=%s", e.getMessage());
        }
    }
}
