package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.XCUIElementType;
import com.pwc.core.framework.data.WebElementAttribute;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeButtonElementImpl implements MicroserviceMobileElement {

    public static boolean applies(MobileElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), XCUIElementType.BUTTON.type));
    }

    public void mobileAction(final MobileElement element, final Object attributeValue) {
        mobileAction(element);
    }

    public void mobileAction(final MobileElement element) {
        try {
            LOG(true, "Click BUTTON %s", getButtonText(element));
            element.click();
        } catch (Exception e) {
            assertFail("Failed to Select BUTTON due to exception=%s", e.getMessage());
        }
    }

    public String getButtonText(MobileElement element) {

        String buttonText = "";
        if (!StringUtils.isEmpty(element.getText())) {
            buttonText = String.format(":: text='%s'", element.getText());
        } else if (!StringUtils.isEmpty(element.getAttribute(WebElementAttribute.VALUE.attribute))) {
            buttonText = String.format(":: value='%s'", element.getAttribute(WebElementAttribute.VALUE.attribute));
        } else if (!StringUtils.isEmpty(element.getAttribute(WebElementAttribute.ID.attribute))) {
            buttonText = String.format(":: id='%s'", element.getAttribute(WebElementAttribute.ID.attribute));
        }
        return buttonText;
    }
}
