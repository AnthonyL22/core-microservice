package com.pwc.core.framework.processors.mobile.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.XCUIElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class XCUIElementTypeCellImpl implements MicroserviceWebElementElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), XCUIElementType.CELL.type);
    }

    public void mobileAction(final WebElement WebElement, final Object attributeValue) {
        try {
            if (attributeValue == null) {
                LOG(true, "Click CELL %s", getElementText(WebElement));
                WebElement.click();
            } else {
                LOG(true, "Verify CELL :: value='%s'", attributeValue);
                Assert.assertEquals(WebElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed CELL action due to exception=%s", e.getMessage());
        }
    }

    public String getElementText(WebElement WebElement) {

        String elementText = "";
        if (!StringUtils.isEmpty(WebElement.getText())) {
            elementText = String.format(":: text='%s'", WebElement.getText());
        } else if (!StringUtils.isEmpty(WebElement.getAttribute(WebElementAttribute.VALUE.attribute))) {
            elementText = String.format(":: value='%s'", WebElement.getAttribute(WebElementAttribute.VALUE.attribute));
        } else if (!StringUtils.isEmpty(WebElement.getAttribute(WebElementAttribute.ID.attribute))) {
            elementText = String.format(":: id='%s'", WebElement.getAttribute(WebElementAttribute.ID.attribute));
        }
        return elementText;
    }

}
