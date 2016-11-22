package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertEquals;
import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class LabelElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.LABEL.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Verify LABEL :: value='%s'", attributeValue);
            assertEquals(webElement.getText(), attributeValue);
        } catch (Exception e) {
            assertFail("Failed LABEL validation due to exception=%s", e.getMessage());
        }
    }
}
