package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LabelElementTest extends WebElementBaseTest {

    public static final String UNIT_TEST_LABEL = "Unit test label";
    LabelElementImpl labelElement;

    @Before
    public void setUp() {
        labelElement = new LabelElementImpl();
    }

    @Test
    public void labelElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.LABEL, true);
        boolean result = LabelElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void labelElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = LabelElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.LABEL.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_LABEL);
        labelElement.webAction(mockDivElement, UNIT_TEST_LABEL);
    }

    @Test
    public void webActionClickableDivTest() {
        WebElement mockDivElement = mock(WebElement.class);
        when(mockDivElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockDivElement.getTagName()).thenReturn(WebElementType.LABEL.type);
        when(mockDivElement.getText()).thenReturn(UNIT_TEST_LABEL);
        labelElement.webAction(mockDivElement, UNIT_TEST_LABEL);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        labelElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
