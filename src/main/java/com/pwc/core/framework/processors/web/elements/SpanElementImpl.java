package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import static com.pwc.logging.LoggerService.LOG;
import static com.pwc.assertion.AssertService.assertFail;

public class SpanElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.SPAN.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            if (StringUtils.isEmpty((CharSequence) attributeValue)) {
                LOG(true, "Click SPAN %s", !StringUtils.isEmpty(webElement.getText()) ? String.format(":: text='%s'", webElement.getText()) : "");
                webElement.click();
            } else {
                LOG(true, "Verify SPAN :: value='%s'", attributeValue);
                Assert.assertEquals(webElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed SPAN action due to exception=%s", e.getMessage());
        }
    }
}
