package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertEquals;
import static com.qualcomm.qssert.QssertService.assertFail;

public class HeadingElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return !StringUtils.isBlank(element.getTagName()) && element.getTagName().matches(WebElementType.HEADER.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            if (StringUtils.isEmpty((CharSequence) attributeValue)) {
                LOG(true, "Click HEADING %s", !StringUtils.isEmpty(webElement.getText()) ? String.format(":: text='%s'", webElement.getText()) : "");
                webElement.click();
            } else {
                LOG(true, "Verify HEADING :: value='%s'", attributeValue);
                assertEquals(webElement.getText(), attributeValue);
            }
        } catch (Exception e) {
            assertFail("Failed HEADING validation due to exception=%s", e.getMessage());
        }
    }
}
