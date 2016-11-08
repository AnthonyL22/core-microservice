package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertFail;

public class RadioButtonElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type) &&
                StringUtils.equalsIgnoreCase(element.getAttribute(WebElementAttribute.TYPE.attribute), WebElementType.RADIO.type);
    }

    public void webAction(final WebElement webElement, final Object state) {
        try {
            LOG(true, "Select RADIO :: text='%s'", Boolean.valueOf(state.toString()));
            if (webElement.isSelected() != Boolean.valueOf(state.toString())) {
                webElement.click();
            }
        } catch (Exception e) {
            assertFail("Failed to Select RADIO due to exception=%s", e.getMessage());
        }
    }
}
