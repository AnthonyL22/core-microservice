package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.XCUIElementType;
import com.pwc.core.framework.data.WebElementAttribute;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeCellImpl implements MicroserviceMobileElement {

    public static boolean applies(MobileElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), XCUIElementType.CELL.type);
    }

    public void mobileAction(final MobileElement webElement, final Object attributeValue) {
        mobileAction(webElement);
    }

    public void mobileAction(final MobileElement mobileElement) {
        try {
            LOG(true, "Click CELL %s", getElementText(mobileElement));
            mobileElement.click();
        } catch (Exception e) {
            assertFail("Failed to Click CELL due to exception=%s", e.getMessage());
        }
    }

    public String getElementText(MobileElement mobileElement) {
        String buttonText = "";
        if (!StringUtils.isEmpty(mobileElement.getText())) {
            buttonText = String.format(":: text='%s'", mobileElement.getText());
        } else if (!StringUtils.isEmpty(mobileElement.getAttribute(WebElementAttribute.VALUE.attribute))) {
            buttonText = String.format(":: value='%s'", mobileElement.getAttribute(WebElementAttribute.VALUE.attribute));
        } else if (!StringUtils.isEmpty(mobileElement.getAttribute(WebElementAttribute.ID.attribute))) {
            buttonText = String.format(":: id='%s'", mobileElement.getAttribute(WebElementAttribute.ID.attribute));
        }
        return buttonText;
    }
}
