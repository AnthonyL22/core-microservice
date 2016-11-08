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
public class LabelElementTest extends WebElementBaseTest {

    public static final String UNIT_TEST_LABEL = "Unit test label";
    LabelElementImpl labelElement;

    @Before
    public void setUp() {
        labelElement = new LabelElementImpl();
    }

    @Test
    public void labelElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.LABEL, true);
        boolean result = LabelElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void labelElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = LabelElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.LABEL.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_LABEL);
        labelElement.webAction(mockDivElement, UNIT_TEST_LABEL);
    }

    @Test
    public void webActionClickableDivTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.LABEL.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_LABEL);
        labelElement.webAction(mockDivElement, UNIT_TEST_LABEL);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        labelElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
