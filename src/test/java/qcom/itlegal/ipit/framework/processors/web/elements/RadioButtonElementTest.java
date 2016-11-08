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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RadioButtonElementTest extends WebElementBaseTest {

    RadioButtonElementImpl radioButtonElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        radioButtonElement = new RadioButtonElementImpl();
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
    }

    @Test
    public void radioButtonElementAppliesTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.RADIO.type);
        boolean result = RadioButtonElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void radioButtonElementAppliesTagNameOnlyTest() {
        boolean result = RadioButtonElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void radioButtonElementAppliesAttributeOnlyTest() {
        boolean result = RadioButtonElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void radioButtonElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        boolean result = RadioButtonElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.CHECKBOX, true);
        radioButtonElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionLoggingRadioTest() {
        WebElement mockSelectedRadioButtonElement = mock(WebElement.class);
        when(mockSelectedRadioButtonElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSelectedRadioButtonElement.getTagName()).thenReturn(WebElementType.RADIO.type);
        when(mockSelectedRadioButtonElement.isSelected()).thenReturn(true);
        radioButtonElement.webAction(mockSelectedRadioButtonElement, true);
    }

    @Test
    public void webActionAttemptToSelectAnAlreadySelectedRadioTest() {
        WebElement mockSelectedRadioButtonElement = mock(WebElement.class);
        when(mockSelectedRadioButtonElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSelectedRadioButtonElement.getTagName()).thenReturn(WebElementType.RADIO.type);
        when(mockSelectedRadioButtonElement.isSelected()).thenReturn(true);
        radioButtonElement.webAction(mockSelectedRadioButtonElement, true);
    }

    @Test
    public void webActionCheckARadioTest() {
        WebElement mockCheckedCheckboxElement = mock(WebElement.class);
        when(mockCheckedCheckboxElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockCheckedCheckboxElement.getTagName()).thenReturn(WebElementType.RADIO.type);
        when(mockCheckedCheckboxElement.isSelected()).thenReturn(false);
        radioButtonElement.webAction(mockCheckedCheckboxElement, true);
    }

    @Test
    public void webActionDeselectRadioTest() {
        WebElement mockCheckedCheckboxElement = mock(WebElement.class);
        when(mockCheckedCheckboxElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockCheckedCheckboxElement.getTagName()).thenReturn(WebElementType.RADIO.type);
        when(mockCheckedCheckboxElement.isSelected()).thenReturn(true);
        radioButtonElement.webAction(mockCheckedCheckboxElement, false);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        radioButtonElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
