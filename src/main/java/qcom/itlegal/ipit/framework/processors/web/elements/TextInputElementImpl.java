package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertFail;

public class TextInputElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return ((StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type) &&
                !(StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.CHECKBOX.type) ||
                        StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.RADIO.type)))
        );
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        LOG(true, "Enter INPUT :: value='%s'", attributeValue);
        try {
            webElement.clear();
            webElement.sendKeys(attributeValue.toString());
        } catch (InvalidElementStateException e) {
            webElement.sendKeys(attributeValue.toString());
        } catch (Exception e) {
            assertFail("Failed to Enter INPUT due to exception=%s", e.getMessage());
        }
    }
}
