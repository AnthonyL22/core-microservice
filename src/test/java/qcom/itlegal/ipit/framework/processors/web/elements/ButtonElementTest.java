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
public class ButtonElementTest extends WebElementBaseTest {

    ButtonElementImpl buttonElement;

    @Before
    public void setUp() {
        buttonElement = new ButtonElementImpl();
    }

    @Test
    public void buttonElementWebActionGetTextTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(mockSubmitInputElement.getText()).thenReturn("APPLY");
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonElementWebActionGetAttributeValueTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("APPLY");
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonElementWebActionGetAttributeIDTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonElementWebActionGetNoAttributeIDTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonElementAppliesTypeMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        boolean result = ButtonElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void buttonElementAppliesTypeCloseTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = ButtonElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void buttonElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        boolean result = ButtonElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        buttonElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionWebElementOnlyTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        buttonElement.webAction(getMockWebElement());
    }

    @Test
    public void webActionWebElementWithTextTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        when(getMockWebElement().getText()).thenReturn("OK");
        buttonElement.webAction(getMockWebElement());
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        buttonElement.webAction(null);
    }


}
