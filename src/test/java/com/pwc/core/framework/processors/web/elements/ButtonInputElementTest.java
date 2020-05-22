package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ButtonInputElementTest extends WebElementBaseTest {

    ButtonInputElementImpl buttonInputElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        buttonInputElement = new ButtonInputElementImpl();
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
    }

    @Test
    public void buttonInputElementAppliesButtonTypeTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.BUTTON.type);
        boolean result = ButtonInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void buttonInputElementAppliesSubmitTypeTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SUBMIT.type);
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        boolean result = ButtonInputElementImpl.applies(mockSubmitInputElement);
        Assert.assertTrue(result);
    }

    @Test
    public void buttonInputElementWebActionSubmitTypeWithValueTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SUBMIT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("APPLY");
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonInputElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonInputElementWebActionSubmitTypeNoAttributeTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SUBMIT.type);
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonInputElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonInputElementWebActionSubmitTypeTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SUBMIT.type);
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonInputElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonInputElementWebActionCheckboxTypeTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.CHECKBOX.type);
        when(mockSubmitInputElement.isSelected()).thenReturn(true);
        buttonInputElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonInputElementWebActionImageInputTypeTest() {
        WebElement mockSubmitInputElement = mock(WebElement.class);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSubmitInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockSubmitInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.IMAGE.type);
        buttonInputElement.webAction(mockSubmitInputElement);
    }

    @Test
    public void buttonInputElementAppliesFileTypeTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.FILE.type);
        boolean result = ButtonInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void buttonInputElementAppliesImageTypeTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.IMAGE.type);
        boolean result = ButtonInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void buttonInputElementAppliesInputTypeOnlyTest() {
        boolean result = ButtonInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void buttonInputElementAppliesButtonTypeOnlyTest() {
        when(mockWebElement.getTagName()).thenReturn(null);
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.BUTTON.type);
        boolean result = ButtonInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void buttonInputElementAppliesFileTypeOnlyTest() {
        when(mockWebElement.getTagName()).thenReturn(null);
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.FILE.type);
        boolean result = ButtonInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        buttonInputElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionWebElementOnlyTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        buttonInputElement.webAction(getMockWebElement());
    }

    @Test
    public void webActionWebElementWithTextTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.BUTTON, true);
        when(getMockWebElement().getText()).thenReturn("Cancel Button");
        buttonInputElement.webAction(getMockWebElement());
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        buttonInputElement.webAction(null);
    }

}
