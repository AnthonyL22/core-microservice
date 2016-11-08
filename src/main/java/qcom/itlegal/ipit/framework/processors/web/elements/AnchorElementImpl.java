package qcom.itlegal.ipit.framework.processors.web.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;

import static com.qualcomm.qherkin.QherkinLoggerService.LOG;
import static com.qualcomm.qssert.QssertService.assertFail;

public class AnchorElementImpl implements QualcommWebElement {

    public static boolean applies(WebElement element) {

        if (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.SELECT.type) ||
                StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.INPUT.type)) {
            return false;
        } else {
            return (StringUtils.equalsIgnoreCase(element.getTagName(), WebElementType.ANCHOR.type) ||
                    StringUtils.isNotBlank(element.getAttribute(WebElementAttribute.HREF.attribute)));
        }

    }

    public void webAction(final WebElement webElement, final Object attributeValue) {
        webAction(webElement);
    }

    public void webAction(final WebElement webElement) {
        try {
            LOG(true, "Click ANCHOR %s", !StringUtils.isEmpty(webElement.getText()) ? String.format(":: text='%s'", webElement.getText()) : "");
            try {
                webElement.sendKeys("");
            } catch (Exception eatMsg) {
                eatMsg.getMessage();
            }
            webElement.click();
        } catch (Exception e) {
            assertFail("Failed to click ANCHOR due to exception=%s", e.getMessage());
        }
    }
}
