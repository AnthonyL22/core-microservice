package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertFail;

public class TextAreaElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.TEXTAREA.type));
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Enter TEXTAREA :: value='%s'", attributeValue);
            webElement.clear();
            webElement.sendKeys(attributeValue.toString());
        } catch (Exception e) {
            assertFail("Failed to Enter into TEXTAREA due to exception=%s", e.getMessage());
        }
    }
}
