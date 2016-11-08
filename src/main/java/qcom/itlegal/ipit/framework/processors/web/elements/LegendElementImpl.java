package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertEquals;
import static com.qualcomm.qssert.QssertService.assertFail;

public class LegendElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {
        return StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.LEGEND.type);
    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        try {
            LOG(true, "Verify LEGEND :: value='%s'", attributeValue);
            assertEquals(webElement.getText(), attributeValue);
        } catch (Exception e) {
            assertFail("Failed LEGEND validation due to exception=%s", e.getMessage());
        }
    }
}
