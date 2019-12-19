package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.processors.web.elements.MicroserviceWebElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeStaticTextElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {

        if (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.SELECT.type) ||
                StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)) {
            return false;
        } else {
            return (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.ANCHOR.type) ||
                    StringUtils.isNotBlank(element.getAttribute(WebElementAttribute.HREF.attribute)));
        }

    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        webAction(webElement);
    }

    public void webAction(final WebElement webElement) {
        try {
            LOG(true, "Click ANCHOR %s", !StringUtils.isEmpty(webElement.getText()) ? String.format(":: text='%s'", webElement.getText()) : "");
            try {
                webElement.sendKeys("");
            } catch (Exception eatMsg) {
                eatMsg.getMessage();
            }
            webElement.click();
        } catch (Exception e) {
            assertFail("Failed to click ANCHOR due to exception=%s", e.getMessage());
        }
    }
}
