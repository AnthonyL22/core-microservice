package qcom.itlegal.ipit.framework.processors.web.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.CssProperty;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DivElementTest extends WebElementBaseTest {

    public static final String UNIT_TEST_DIV_LABEL = "Unit test div label";
    DivElementImpl divElement;

    @Before
    public void setUp() {
        divElement = new DivElementImpl();
    }

    @Test
    public void divElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        boolean result = DivElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void divElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = DivElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_DIV_LABEL);
        divElement.webAction(mockDivElement, UNIT_TEST_DIV_LABEL);
    }

    @Test
    public void webActionClickableDivTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_DIV_LABEL);
        when(mockDivElement.getCssValue(CssProperty.BACKGROUND_IMAGE.property)).thenReturn("checkbox.gif");
        divElement.webAction(mockDivElement, UNIT_TEST_DIV_LABEL);
    }

    @Test
    public void webActionNullLoggingTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_DIV_LABEL);
        when(mockDivElement.getCssValue(CssProperty.BACKGROUND_COLOR.property)).thenReturn("red");
        divElement.webAction(mockDivElement, null);
    }

    @Test
    public void webActionDivNotCheckboxTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_DIV_LABEL);
        when(mockDivElement.getCssValue(CssProperty.BACKGROUND_COLOR.property)).thenReturn("red");
        divElement.webAction(mockDivElement, UNIT_TEST_DIV_LABEL);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        divElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
