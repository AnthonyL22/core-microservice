package qcom.itlegal.ipit.framework.processors.web.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LegendElementTest extends WebElementBaseTest {

    public static final String UNIT_TEST_LEGEND_LABEL = "Unit test legend";
    LegendElementImpl legendElement;

    @Before
    public void setUp() {
        legendElement = new LegendElementImpl();
    }

    @Test
    public void legendElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.LEGEND, true);
        boolean result = LegendElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void legendElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = LegendElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.LEGEND.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_LEGEND_LABEL);
        legendElement.webAction(mockDivElement, UNIT_TEST_LEGEND_LABEL);
    }

    @Test(expected = AssertionError.class)
    public void webActionNullAttributeTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.LEGEND.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_LEGEND_LABEL);
        legendElement.webAction(mockDivElement, null);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        legendElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
