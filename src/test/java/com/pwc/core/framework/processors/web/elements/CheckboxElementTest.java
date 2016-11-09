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
public class CheckboxElementTest extends WebElementBaseTest {

    CheckboxElementImpl checkboxElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        checkboxElement = new CheckboxElementImpl();
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
    }

    @Test
    public void checkboxElementAppliesTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.CHECKBOX.type);
        boolean result = CheckboxElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void checkboxElementAppliesTagNameOnlyTest() {
        boolean result = CheckboxElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void checkboxElementAppliesAttributeOnlyTest() {
        boolean result = CheckboxElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void checkboxElementAppliesNoMatchTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        boolean result = CheckboxElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.CHECKBOX, true);
        checkboxElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionAttemptToCheckAnAlreadyCheckedCheckboxTest() {
        WebElement mockCheckedCheckboxElement = mock(WebElement.class);
        when(mockCheckedCheckboxElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockCheckedCheckboxElement.getTagName()).thenReturn(WebElementType.CHECKBOX.type);
        when(mockCheckedCheckboxElement.isSelected()).thenReturn(true);
        checkboxElement.webAction(mockCheckedCheckboxElement, true);
    }

    @Test
    public void webActionCheckACheckboxTest() {
        WebElement mockCheckedCheckboxElement = mock(WebElement.class);
        when(mockCheckedCheckboxElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockCheckedCheckboxElement.getTagName()).thenReturn(WebElementType.CHECKBOX.type);
        when(mockCheckedCheckboxElement.isSelected()).thenReturn(false);
        checkboxElement.webAction(mockCheckedCheckboxElement, true);
    }

    @Test
    public void webActionDeselectCheckboxTest() {
        WebElement mockCheckedCheckboxElement = mock(WebElement.class);
        when(mockCheckedCheckboxElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockCheckedCheckboxElement.getTagName()).thenReturn(WebElementType.CHECKBOX.type);
        when(mockCheckedCheckboxElement.isSelected()).thenReturn(true);
        checkboxElement.webAction(mockCheckedCheckboxElement, false);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        checkboxElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
