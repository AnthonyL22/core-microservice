package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertFail;

public class SelectableElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.SELECT.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Choose SELECT %s", attributeValue != null ? String.format(":: value='%s'", attributeValue) : "");
            Select dropDownList = new Select(webElement);
            try {
                dropDownList.deselectAll();
            } catch (Exception eatMessage) {
                eatMessage.getMessage();
            }

            if (StringUtils.containsAny(attributeValue.toString(), ",")) {
                String[] itemsToSelect = StringUtils.split(attributeValue.toString(), ",");
                for (Object item : itemsToSelect) {
                    dropDownList.selectByVisibleText(item.toString().trim());
                }
            } else {
                dropDownList.selectByVisibleText(attributeValue.toString());
            }
        } catch (Exception e) {
            assertFail("Failed to choose from SELECT due to exception=%s", e.getMessage());
        }
    }
}
