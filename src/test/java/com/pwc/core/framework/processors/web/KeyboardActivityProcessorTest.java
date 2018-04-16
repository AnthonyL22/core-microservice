package com.pwc.core.framework.processors.web;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardActivityProcessorTest extends WebElementBaseTest {

    public static final String ATTRIBUTE = "my user";
    protected KeyboardActivityProcessor keyboardActivityProcessor;
    private WebElement mockWebElement;

    @Before
    public void setUp() {
        keyboardActivityProcessor = KeyboardActivityProcessor.getInstance();
        mockWebElement = mock(WebElement.class);
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXT.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
    }

    @Test
    public void webActionTest() {
        keyboardActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(1)).getTagName();
    }

    @Test
    public void webActionNotAppliesTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        keyboardActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(2)).getTagName();
    }

}
