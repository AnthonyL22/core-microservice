package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static com.pwc.logging.LoggerService.LOG;
import static com.pwc.assertion.AssertService.assertFail;

public class ComboBoxElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type) &&
                StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.ROLE.attribute), WebElementType.COMBOBOX.type));
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Choose COMBOBOX %s", attributeValue != null ? String.format(":: value='%s'", attributeValue) : "");
            if (!selectWebElement(webElement, attributeValue)) {
                if (!selectNonExtJsWebElement(webElement, attributeValue)) {
                    if (!selectExtJsWebElement(webElement, attributeValue)) {
                        webElement.click();
                    }
                }
            }
        } catch (Exception e) {
            assertFail("Failed to Choose from COMBOBOX due to exception=%s", e.getMessage());
        }
    }

    private boolean selectNonExtJsWebElement(WebElement webElement, Object attributeValue) {
        boolean validSelection = false;
        try {
            webElement.clear();
            webElement.sendKeys(attributeValue.toString());
            if (webElement.getAttribute(WebElementAttribute.VALUE.attribute).equals(attributeValue)) {
                validSelection = true;
            }
            webElement.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            return validSelection;
        }
        return validSelection;
    }

    /**
     * Last resort selection of drop down item.  Should rarely come to this and execute unless all else fails
     *
     * @param webElement     WebElement to operate against
     * @param attributeValue value to enter/select
     * @return selected?
     */
    private boolean selectExtJsWebElement(WebElement webElement, Object attributeValue) {
        try {
            webElement.clear();
            webElement.sendKeys(attributeValue.toString());
            webElement.sendKeys(Keys.ARROW_DOWN);
            webElement.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean selectWebElement(final WebElement webElement, final Object attributeValue) {
        try {
            Select dropDownList = new Select(webElement);
            dropDownList.selectByVisibleText(attributeValue.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
