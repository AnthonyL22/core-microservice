package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.XCUIElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeImageImpl implements MicroserviceMobileElement {

    public static boolean applies(WebElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), XCUIElementType.IMAGE.type));
    }

    public void mobileAction(final WebElement mobileElement, final Object attributeValue) {
        try {
            if (attributeValue == null) {
                LOG(true, "Click IMAGE %s", getElementText(mobileElement));
                mobileElement.click();
            } else {
                LOG(true, "Verify IMAGE :: value='%s'", attributeValue);
                Assert.assertEquals(mobileElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed IMAGE action due to exception=%s", e.getMessage());
        }
    }

    public String getElementText(WebElement mobileElement) {

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
