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
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TextInputElementTest extends WebElementBaseTest {

    TextInputElementImpl textInputElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        textInputElement = new TextInputElementImpl();
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SEARCH.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockWebElement.isDisplayed()).thenReturn(true);
    }

    @Test
    public void textInputElementAppliesTagMatchTest() {
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesInputOnlyTest() {
        WebElement mockWebInputElement = mock(WebElement.class);
        when(mockWebInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebInputElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesFileInputTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.FILE.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesPasswordAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.PASSWORD.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesEmailAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.EMAIL.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesDateAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.DATE.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesSearchAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SEARCH.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesCheckboxAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.CHECKBOX.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textInputElementAppliesRadioButtonAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.RADIO.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textInputElementAppliesTextAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXT.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textInputElementAppliesTextAttributeOnlyShouldFailTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXT.type);
        when(mockWebElement.getTagName()).thenReturn(null);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textInputElementAppliesSearchAttributeOnlyShouldFailTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SEARCH.type);
        when(mockWebElement.getTagName()).thenReturn(null);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textInputElementAppliesWrongAttributeTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.getTagName()).thenReturn(null);
        boolean result = TextInputElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textInputElementAppliesTagNameOnlyTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = TextInputElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        textInputElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionDateInputTest() {
        WebElement mockWebDateInputElement = mock(WebElement.class);
        when(mockWebDateInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebDateInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockWebDateInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.DATE.type);
        doThrow(InvalidElementStateException.class).when(mockWebDateInputElement).clear();
        textInputElement.webAction(mockWebDateInputElement, "12/12/2015");
    }

    @Test(expected = NullPointerException.class)
    public void webActionNullAttributeInputTest() {
        WebElement mockWebDateInputElement = mock(WebElement.class);
        when(mockWebDateInputElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebDateInputElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        when(mockWebDateInputElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.DATE.type);
        doThrow(InvalidElementStateException.class).when(mockWebDateInputElement).clear();
        textInputElement.webAction(mockWebDateInputElement, null);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        textInputElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
