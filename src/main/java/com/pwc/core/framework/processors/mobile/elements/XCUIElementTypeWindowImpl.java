package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.IOSElementType;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeWindowImpl implements MicroserviceMobileElement {

    public static boolean applies(MobileElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), IOSElementType.WINDOW.type);
    }

    public void mobileAction(final MobileElement mobileElement, final Object attributeValue) {
        try {
            if (attributeValue == null) {
                LOG(true, "Click WINDOW %s", !StringUtils.isEmpty(mobileElement.getText()) ? String.format(":: text='%s'", mobileElement.getText()) : "");
                mobileElement.click();
            } else {
                LOG(true, "Verify WINDOW :: value='%s'", attributeValue);
                Assert.assertEquals(mobileElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed WINDOW action due to exception=%s", e.getMessage());
        }
    }

}
