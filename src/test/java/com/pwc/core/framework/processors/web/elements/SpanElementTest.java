package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpanElementTest extends WebElementBaseTest {

    public static final String SPAN_TEXT = "My Span";
    SpanElementImpl spanElement;

    private WebElement mockPrecedingSiblingWebElement;
    private WebElement mockSpanWebElement;

    @Before
    public void setUp() {
        spanElement = new SpanElementImpl();
        mockPrecedingSiblingWebElement = mock(WebElement.class);
        mockSpanWebElement = mock(WebElement.class);

        when(mockPrecedingSiblingWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("111");
        when(mockPrecedingSiblingWebElement.getTagName()).thenReturn(WebElementType.ANCHOR.type);
        when(mockPrecedingSiblingWebElement.isDisplayed()).thenReturn(true);

        when(mockSpanWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockSpanWebElement.getTagName()).thenReturn(WebElementType.SPAN.type);
        when(mockSpanWebElement.getText()).thenReturn(SPAN_TEXT);
        when(mockSpanWebElement.isDisplayed()).thenReturn(true);

        when(mockSpanWebElement.findElement(By.xpath("//a/preceding-sibling::a"))).thenReturn(mockPrecedingSiblingWebElement);

    }

    @Test
    public void spanElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.SPAN, true);
        boolean result = SpanElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void spanElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.SELECT, true);
        boolean result = SpanElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionClickableSpanMatchingTextTest() {
        when(mockPrecedingSiblingWebElement.isDisplayed()).thenReturn(true);
        spanElement.webAction(mockSpanWebElement, SPAN_TEXT);
    }

    @Test
    public void webActionNullAttributeTest() {
        when(mockPrecedingSiblingWebElement.isDisplayed()).thenReturn(true);
        spanElement.webAction(mockSpanWebElement, null);
    }

    @Test
    public void webActionNonClickableSpanMatchingTextTest() {
        when(mockPrecedingSiblingWebElement.isDisplayed()).thenReturn(false);
        spanElement.webAction(mockSpanWebElement, SPAN_TEXT);
    }

    @Test(expected = AssertionError.class)
    public void webActionNonClickableSpanNonMatchingTextTest() {
        when(mockPrecedingSiblingWebElement.isDisplayed()).thenReturn(false);
        spanElement.webAction(mockSpanWebElement, SPAN_TEXT + "foo");
    }

    @Test
    public void webActionLinkToElementTest() {
        when(mockSpanWebElement.getAttribute(WebElementAttribute.LINKTO.attribute)).thenReturn("12344");
        spanElement.webAction(mockSpanWebElement, SPAN_TEXT);
    }

}
