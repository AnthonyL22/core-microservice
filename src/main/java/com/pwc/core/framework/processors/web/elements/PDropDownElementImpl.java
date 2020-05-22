package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.processors.web.elements.custom.PDropDown;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.util.stream.IntStream;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class PDropDownElementImpl implements MicroserviceWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.P_DROP_DOWN.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {

        try {
            LOG(true, "Choose P-DROPDOWN %s", attributeValue != null ? String.format(":: value='%s'", attributeValue) : "");
            PDropDown dropDownElement = new PDropDown(webElement);

            if (StringUtils.containsAny(String.valueOf(attributeValue), ",")) {
                String[] itemsToSelect = StringUtils.split(String.valueOf(attributeValue), ",");
                IntStream.range(0, itemsToSelect.length).forEach(item -> dropDownElement.selectByVisibleText(String.valueOf(item).trim()));
            } else {
                dropDownElement.selectByVisibleText(String.valueOf(attributeValue));
            }

        } catch (Exception e) {
            assertFail("Failed to Choose from P-DropDown due to exception=%s", e.getMessage());
        }
    }

}
