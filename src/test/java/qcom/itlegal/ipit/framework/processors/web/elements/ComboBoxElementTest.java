package qcom.itlegal.ipit.framework.processors.web.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComboBoxElementTest extends WebElementBaseTest {

    ComboBoxElementImpl comboBoxElement;
    public static final String DROP_DOWN_VALUE = "Unit Test Name";

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        comboBoxElement = new ComboBoxElementImpl();
    }

    @Test
    public void comboBoxElementAppliesTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.ROLE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = ComboBoxElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void comboBoxElementAppliesPartialElementTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.ROLE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        boolean result = ComboBoxElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void comboBoxElementAppliesInputOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        boolean result = ComboBoxElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void comboBoxElementAppliesRoleOnlyTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getAttribute(WebElementAttribute.ROLE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        boolean result = ComboBoxElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void comboBoxElementNotAppliesTest() {
        createMockElement("combobox-role-x", WebElementAttribute.CLASS, WebElementType.DIV, true);
        boolean result = ComboBoxElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void selectWebElementTest() {
        WebElement mockListOptionElement = mock(WebElement.class);
        when(mockListOptionElement.getText()).thenReturn(DROP_DOWN_VALUE);

        WebElement mockElement = mock(WebElement.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        when(mockElement.getAttribute("multiple")).thenReturn("false");
        List<WebElement> options = new ArrayList<>();
        options.add(mockListOptionElement);
        when(mockElement.findElements(By.xpath(".//option[normalize-space(.) = " + Quotes.escape(DROP_DOWN_VALUE) + "]"))).thenReturn(options);
        comboBoxElement.webAction(mockElement, DROP_DOWN_VALUE);
    }

    @Test(expected = AssertionError.class)
    public void selectWebElementNullWebElementListTest() {
        comboBoxElement.webAction(null, DROP_DOWN_VALUE);
    }

    @Test
    public void selectNonExtJsWebElementTest() {
        WebElement mockElement = mock(WebElement.class);
        Select mockSelectElement = mock(Select.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn(DROP_DOWN_VALUE);
        when(mockElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        doNothing().when(mockSelectElement).selectByVisibleText(DROP_DOWN_VALUE);
        comboBoxElement.webAction(mockElement, DROP_DOWN_VALUE);
    }

    @Test
    public void webActionSelectTest() {
        WebElement mockElement = mock(WebElement.class);
        Select mockSelectElement = mock(Select.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        doNothing().when(mockSelectElement).selectByVisibleText(DROP_DOWN_VALUE);
        comboBoxElement.webAction(mockElement, DROP_DOWN_VALUE);
    }

    @Test
    public void webActionExtJsTest() {
        createMockElement("combobox-role-x", WebElementAttribute.CLASS, WebElementType.INPUT, true);
        comboBoxElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test
    public void webActionClickableComboTest() {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getAttribute(WebElementAttribute.ROLE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        when(mockElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        doThrow(Exception.class).when(mockElement).sendKeys("SubTechs");
        comboBoxElement.webAction(mockElement, "SubTechs");
    }

    @Test
    public void webActionNullLoggingTest() {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockElement.getAttribute(WebElementAttribute.ROLE.attribute)).thenReturn(WebElementType.COMBOBOX.type);
        when(mockElement.getTagName()).thenReturn(WebElementType.INPUT.type);
        doThrow(Exception.class).when(mockElement).sendKeys("SubTechs");
        comboBoxElement.webAction(mockElement, null);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        comboBoxElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
