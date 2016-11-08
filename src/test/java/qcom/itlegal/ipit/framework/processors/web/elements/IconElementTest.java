package qcom.itlegal.ipit.framework.processors.web.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;

@RunWith(MockitoJUnitRunner.class)
public class IconElementTest extends WebElementBaseTest {

    IconElementImpl iconElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        iconElement = new IconElementImpl();
    }

    @Test
    public void iconElementAppliesTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.ICON, true);
        boolean result = IconElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void iconElementNotAppliesTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.DIV, true);
        boolean result = IconElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.ICON, true);
        iconElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        iconElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
