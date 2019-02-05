package com.pwc.core.framework.processors.web;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MouseActivityProcessorTest extends WebElementBaseTest {

    private static final String ATTRIBUTE = "my user";
    private MouseActivityProcessor mouseActivityProcessor;
    private WebElement mockWebElement;
    private List<WebElement> mockWebElementOptionList;

    @Before
    public void setUp() {
        mouseActivityProcessor = MouseActivityProcessor.getInstance();
        mockWebElement = mock(WebElement.class);
        WebElement mockWebElementOptionOne = mock(WebElement.class);
        WebElement mockWebElementOptionTwo = mock(WebElement.class);
        mockWebElementOptionList = Arrays.asList(mockWebElementOptionOne, mockWebElementOptionTwo);
    }

    @Test
    public void webActionSpanTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.SPAN.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.SPAN.type);
        mouseActivityProcessor.webAction(mockWebElement, null);
        verify(mockWebElement, times(16)).getTagName();
    }

    @Test
    public void webActionDivContainingMatOptionsTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.CLASS.attribute)).thenReturn("green " + WebElementType.MAT_SELECT.type + "-highlighted");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebElement.findElements(By.xpath(".//mat-option[normalize-space(.) = " + Quotes.escape(ATTRIBUTE) + "]"))).thenReturn(mockWebElementOptionList);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(12)).getTagName();
    }

    @Test
    public void webActionPDropDownWithOptionsTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.P_DROP_DOWN.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.P_DROP_DOWN.type);
        when(mockWebElement.findElements(By.xpath(".//li[normalize-space(.) = " + Quotes.escape(ATTRIBUTE) + "]"))).thenReturn(mockWebElementOptionList);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(20)).getTagName();
    }

    @Test(expected = AssertionError.class)
    public void webActionPDropDownWithoutOptionsTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.P_DROP_DOWN.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.P_DROP_DOWN.type);
        when(mockWebElement.findElements(By.xpath(".//li[normalize-space(.) = " + Quotes.escape(ATTRIBUTE) + "]"))).thenReturn(null);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(19)).getTagName();
    }

    @Test
    public void webActionListTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.LI.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.LI.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(14)).getTagName();
    }

    @Test
    public void webActionComboboxTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.COMBOBOX.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(19)).getTagName();
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
    public void webActionButtonWithImageTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.IMAGE.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(3)).getTagName();
    }

    @Test
    public void webActionAnchorTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.ANCHOR.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(9)).getTagName();
    }

    @Test
    public void webActionImageTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.IMG.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(9)).getTagName();
    }

    @Test
    public void webActionHeadingTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.HEADER.type);
        mouseActivityProcessor.webAction(mockWebElement, null);
        verify(mockWebElement, times(19)).getTagName();
    }

    @Test(expected = AssertionError.class)
    public void webActionSelectableTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(6)).getTagName();
    }

    @Test(expected = AssertionError.class)
    public void webActionPDropDownTestFail() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.P_DROP_DOWN.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(6)).getTagName();
    }

    @Test
    public void webActionIconTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.ICON.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(12)).getTagName();
    }

    @Test
    public void webActionNotAppliesTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        mouseActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(18)).getTagName();
    }

}
