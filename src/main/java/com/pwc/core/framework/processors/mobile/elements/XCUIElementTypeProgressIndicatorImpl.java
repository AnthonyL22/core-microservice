package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.XCUIElementType;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeProgressIndicatorImpl implements MicroserviceMobileElement {

    public static boolean applies(MobileElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), XCUIElementType.PROGRESS_INDICATOR.type));
    }

    public void mobileAction(final MobileElement mobileElement, final Object attributeValue) {
        try {
            if (attributeValue == null) {
                LOG(true, "Click PROGRESS INDICATOR %s", getElementText(mobileElement));
                mobileElement.click();
            } else {
                LOG(true, "Verify PROGRESS INDICATOR :: value='%s'", attributeValue);
                Assert.assertEquals(mobileElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed PROGRESS INDICATOR action due to exception=%s", e.getMessage());
        }
    }

    public String getElementText(MobileElement mobileElement) {

        String elementText = "";
        if (!StringUtils.isEmpty(mobileElement.getText())) {
            elementText = String.format(":: text='%s'", mobileElement.getText());
        } else if (!StringUtils.isEmpty(mobileElement.getAttribute(WebElementAttribute.VALUE.attribute))) {
            elementText = String.format(":: value='%s'", mobileElement.getAttribute(WebElementAttribute.VALUE.attribute));
        } else if (!StringUtils.isEmpty(mobileElement.getAttribute(WebElementAttribute.ID.attribute))) {
            elementText = String.format(":: id='%s'", mobileElement.getAttribute(WebElementAttribute.ID.attribute));
        }
        return elementText;
    }

}
