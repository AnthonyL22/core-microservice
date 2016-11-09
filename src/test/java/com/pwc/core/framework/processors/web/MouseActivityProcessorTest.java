package com.pwc.core.framework.processors.web;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MouseActivityProcessorTest extends WebElementBaseTest {

    public static final String ATTRIBUTE = "my user";
    protected MouseActivityProcessor mouseActivityProcessor;
    private WebElement mockWebElement;

    @Before
    public void setUp() {
        mouseActivityProcessor = MouseActivityProcessor.getInstance();
        mockWebElement = mock(WebElement.class);
    }

    @Test
    public void webActionSpanTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SPAN.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.SPAN.type);
        mouseActivityProcessor.webAction(mockWebElement, null);
        verify(mockWebElement, times(14)).getTagName();
    }

    @Test
    public void webActionListTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.LI.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.LI.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(12)).getTagName();
    }

    @Test
    public void webActionComboboxTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.COMBOBOX.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(16)).getTagName();
    }

    @Test
    public void webActionCheckboxTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.CHECKBOX.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(1)).getTagName();
    }

    @Test
    public void webActionRadioButtonTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.RADIO.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(2)).getTagName();
    }

    @Test
    public void webActionButtonTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(3)).getTagName();
    }

    @Test
    public void webActionAnchorTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.ANCHOR.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(8)).getTagName();
    }

    @Test
    public void webActionImageTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.IMG.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(8)).getTagName();
    }

    @Test
    public void webActionHeadingTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.HEADER.type);
        mouseActivityProcessor.webAction(mockWebElement, null);
        verify(mockWebElement, times(16)).getTagName();
    }

    @Test(expected = AssertionError.class)
    public void webActionSelectableTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(6)).getTagName();
    }

    @Test
    public void webActionIconTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.ICON.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(10)).getTagName();
    }

    @Test
    public void webActionNotAppliesTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(15)).getTagName();
    }

}
