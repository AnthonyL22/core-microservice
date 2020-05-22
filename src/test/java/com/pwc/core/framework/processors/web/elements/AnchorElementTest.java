package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnchorElementTest extends WebElementBaseTest {

    AnchorElementImpl anchorElement;

    @Before
    public void setUp() {
        anchorElement = new AnchorElementImpl();
    }

    @Test
    public void anchorElementAppliesAnchorMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.ANCHOR, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void anchorElementAppliesHrefMatchTest() {
        createMockElement("http://www.mywebsite.com", WebElementAttribute.HREF, WebElementType.INPUT, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void anchorElementAppliesOnlyTypeMatchTest() {
        createMockElement("http://www.mywebsite.com", WebElementAttribute.ID, WebElementType.ANCHOR, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void anchorElementAppliesOnlyHrefNotInputOrSelectMatchTest() {
        createMockElement("http://www.mywebsite.com", WebElementAttribute.HREF, WebElementType.SPAN, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void anchorElementAppliesHrefSelectMatchTest() {
        createMockElement("http://www.mywebsite.com", WebElementAttribute.HREF, WebElementType.SELECT, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void anchorElementAppliesHrefInputMatchTest() {
        createMockElement("http://www.mywebsite.com", WebElementAttribute.HREF, WebElementType.INPUT, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void anchorElementAppliesBothMatchTest() {
        createMockElement("http://www.mywebsite.com", WebElementAttribute.HREF, WebElementType.ANCHOR, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void anchorElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        boolean result = AnchorElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        anchorElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionWebElementOnlyTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        anchorElement.webAction(getMockWebElement());
    }

    @Test
    public void webActionAnchorCannotFocusUponTest() {
        WebElement mockWebElement = mock(WebElement.class);
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.ANCHOR.type);
        when(mockWebElement.getText()).thenReturn("click here");
        doThrow(new WebDriverException("unknown error: cannot focus element")).when(mockWebElement).sendKeys("");
        anchorElement.webAction(mockWebElement);
    }

    @Test
    public void webActionInputWithHrefDueToMobileBrowserTest() {
        WebElement mockWebElement = mock(WebElement.class);
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = AnchorElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void webActionWithHrefDueToMobileBrowserTest() {
        WebElement mockWebElement = mock(WebElement.class);
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn("http://www.mywebsite.com");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.ANCHOR.type);
        boolean result = AnchorElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void webSelectElementWithHrefDueToMobileBrowserTest() {
        WebElement mockWebElement = mock(WebElement.class);
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        boolean result = AnchorElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        anchorElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
