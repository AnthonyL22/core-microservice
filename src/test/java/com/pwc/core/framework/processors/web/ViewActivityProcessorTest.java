package com.pwc.core.framework.processors.web;

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
public class ViewActivityProcessorTest extends WebElementBaseTest {

    public static final String ATTRIBUTE = "my user";
    protected ViewActivityProcessor viewActivityProcessor;
    private WebElement mockWebElement;

    @Before
    public void setUp() {
        viewActivityProcessor = ViewActivityProcessor.getInstance();
        mockWebElement = mock(WebElement.class);
    }

    @Test(expected = AssertionError.class)
    public void webActionSpanTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.SPAN.type);
        viewActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(2)).getTagName();
    }

    @Test
    public void webActionLabelTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.LABEL.type);
        viewActivityProcessor.webAction(mockWebElement, null);
        verify(mockWebElement, times(2)).getTagName();
    }

    @Test
    public void webActionLegendTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.LEGEND.type);
        viewActivityProcessor.webAction(mockWebElement, null);
        verify(mockWebElement, times(3)).getTagName();
    }

    @Test
    public void webActionDivTest() {
        when(mockWebElement.getText()).thenReturn(ATTRIBUTE);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        viewActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(1)).getTagName();
    }

    @Test
    public void webActionNotAppliesTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        viewActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(3)).getTagName();
    }

}
