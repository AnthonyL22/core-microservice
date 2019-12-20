package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.IOSElementType;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import static com.pwc.assertion.AssertService.assertEquals;
import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementWindowImpl implements MicroserviceMobileElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), IOSElementType.APPLICATION.type);
    }

    public void mobileAction(final MobileElement element, final Object attributeValue) {
        try {
            if (StringUtils.isEmpty((CharSequence) attributeValue)) {
                LOG(true, "Click APPLICATION %s", !StringUtils.isEmpty(element.getText()) ? String.format(":: text='%s'", element.getText()) : "");
                element.click();
            } else {
                LOG(true, "Verify APPLICATION :: value='%s'", attributeValue);
                assertEquals(element.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed APPLICATION validation due to exception=%s", e.getMessage());
        }
    }
}
