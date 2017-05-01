package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertEquals;
import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class LegendElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.LEGEND.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Verify LEGEND :: value='%s'", attributeValue);
            assertEquals(webElement.getText(), attributeValue);
        } catch (Exception e) {
            assertFail("Failed LEGEND validation due to exception=%s", e.getMessage());
        }
    }
}
