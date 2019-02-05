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
import org.openqa.selenium.support.ui.Select;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MatSelectableElementTest extends WebElementBaseTest {

    private static final String DROP_DOWN_VALUE = "Unit Test Name";
    private static final String MULTI_SELECT_DROP_DOWN_VALUE = "A, B, C, D";
    MatSelectableElementImpl matSelectableElement;

    @Before
    public void setUp() {
        matSelectableElement = new MatSelectableElementImpl();
    }

    @Test
    public void selectableElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.MAT_SELECT, true);
        boolean result = MatSelectableElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void selectableElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = MatSelectableElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void webActionLoggingTest() {
        WebElement mockElement = mock(WebElement.class);
        Select mockSelectElement = mock(Select.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("7777");
        when(mockElement.getTagName()).thenReturn(WebElementType.MAT_SELECT.type);
        doNothing().when(mockSelectElement).selectByVisibleText(DROP_DOWN_VALUE);
        matSelectableElement.webAction(mockElement, DROP_DOWN_VALUE);
    }

    @Test(expected = AssertionError.class)
    public void webActionMultiSelectTest() {
        WebElement mockElement = mock(WebElement.class);
        Select mockSelectElement = mock(Select.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getTagName()).thenReturn(WebElementType.MAT_SELECT.type);
        doNothing().when(mockSelectElement).selectByVisibleText("A");
        matSelectableElement.webAction(mockElement, MULTI_SELECT_DROP_DOWN_VALUE);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        matSelectableElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
