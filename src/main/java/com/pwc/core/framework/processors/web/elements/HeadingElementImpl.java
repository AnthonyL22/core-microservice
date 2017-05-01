package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertEquals;
import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class HeadingElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return !StringUtils.isBlank(element.getTagName()) && element.getTagName().matches(WebElementType.HEADER.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            if (StringUtils.isEmpty((CharSequence) attributeValue)) {
                LOG(true, "Click HEADING %s", !StringUtils.isEmpty(webElement.getText()) ? String.format(":: text='%s'", webElement.getText()) : "");
                webElement.click();
            } else {
                LOG(true, "Verify HEADING :: value='%s'", attributeValue);
                assertEquals(webElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed HEADING validation due to exception=%s", e.getMessage());
        }
    }
}
