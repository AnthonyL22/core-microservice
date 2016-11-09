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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TextAreaElementTest extends WebElementBaseTest {

    TextAreaElementImpl textAreaElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        textAreaElement = new TextAreaElementImpl();
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXTAREA.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.TEXTAREA.type);
        when(mockWebElement.isDisplayed()).thenReturn(true);
    }

    @Test
    public void textAreaElementAppliesTagMatchTest() {
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textAreaElementAppliesPasswordAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.PASSWORD.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.TEXTAREA.type);
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textAreaElementAppliesSearchAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SEARCH.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.TEXTAREA.type);
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textAreaElementAppliesTextAttributeOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXT.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.TEXTAREA.type);
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void textAreaElementAppliesTextAttributeOnlyShouldFailTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXTAREA.type);
        when(mockWebElement.getTagName()).thenReturn(null);
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textAreaElementAppliesSearchAttributeOnlyShouldFailTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SEARCH.type);
        when(mockWebElement.getTagName()).thenReturn(null);
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textAreaElementAppliesWrongAttributeTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.getTagName()).thenReturn(null);
        boolean result = TextAreaElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void textAreaElementAppliesTagNameOnlyTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.TEXTAREA, true);
        boolean result = TextAreaElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.TEXTAREA, true);
        textAreaElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionLoggingTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.TEXTAREA, true);
        textAreaElement.webAction(getMockWebElement(), "Go Home Option");
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        textAreaElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
