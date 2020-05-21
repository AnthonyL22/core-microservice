package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.processors.web.elements.custom.PDropDown;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PDropDownElementTest extends WebElementBaseTest {

    private static final String DROP_DOWN_VALUE = "Unit Test Name";
    private static final String P_DROP_DOWN_MULTI_SELECT_VALUE = "A, B, C, D";
    private static PDropDownElementImpl pDropDownElement;

    @Before
    public void setUp() {
        pDropDownElement = new PDropDownElementImpl();
    }

    @Test
    public void dropDownElementAppliesTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        boolean result = PDropDownElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void dropDownElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        boolean result = PDropDownElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void webActionLoggingTest() {
        WebElement mockElement = mock(WebElement.class);
        PDropDown mockPDropDownElement = mock(PDropDown.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("7777");
        when(mockElement.getTagName()).thenReturn(WebElementType.P_DROP_DOWN.type);
        doNothing().when(mockPDropDownElement).selectByVisibleText(DROP_DOWN_VALUE);
        pDropDownElement.webAction(mockElement, DROP_DOWN_VALUE);
    }

    @Test(expected = AssertionError.class)
    public void webActionMultiSelectTest() {
        WebElement mockElement = mock(WebElement.class);
        PDropDown mockPDropDownElement = mock(PDropDown.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getTagName()).thenReturn(WebElementType.P_DROP_DOWN.type);
        doNothing().when(mockPDropDownElement).selectByVisibleText("A");
        pDropDownElement.webAction(mockElement, P_DROP_DOWN_MULTI_SELECT_VALUE);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        pDropDownElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
