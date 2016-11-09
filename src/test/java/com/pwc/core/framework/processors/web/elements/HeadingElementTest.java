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
public class HeadingElementTest extends WebElementBaseTest {

    public static final String MY_UT_HEADING = "My UT Heading";
    HeadingElementImpl headingElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        headingElement = new HeadingElementImpl();
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn("h1");
    }

    @Test
    public void headingElementAppliesH1Test() {
        boolean result = HeadingElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void headingElementAppliesH3Test() {
        when(mockWebElement.getTagName()).thenReturn("h3");
        boolean result = HeadingElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void headingElementAppliesUpperCaseHeaderTagTest() {
        when(mockWebElement.getTagName()).thenReturn("H4");
        boolean result = HeadingElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void headingElementAppliesBlankTagNameTest() {
        when(mockWebElement.getTagName()).thenReturn("");
        boolean result = HeadingElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        WebElement mockHeadingElement = mock(WebElement.class);
        when(mockHeadingElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockHeadingElement.getTagName()).thenReturn("H3");
        when(mockHeadingElement.getText()).thenReturn(MY_UT_HEADING);
        headingElement.webAction(mockHeadingElement, MY_UT_HEADING);
    }

    @Test
    public void webActionNullLoggingTest() {
        WebElement mockHeadingElement = mock(WebElement.class);
        when(mockHeadingElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockHeadingElement.getTagName()).thenReturn("H3");
        when(mockHeadingElement.getText()).thenReturn(MY_UT_HEADING);
        headingElement.webAction(mockHeadingElement, null);
    }

    @Test
    public void webActionClickableHeadingTest() {
        WebElement mockHeadingElement = mock(WebElement.class);
        when(mockHeadingElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockHeadingElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockHeadingElement.getText()).thenReturn(MY_UT_HEADING);
        headingElement.webAction(mockHeadingElement, null);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        headingElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
