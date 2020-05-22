package com.pwc.core.framework.processors.web.elements.custom;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PDropDownTest extends WebElementBaseTest {

    private static final String DROP_DOWN_VALUE = "Unit Test Name";
    private List<WebElement> mockWebElementOptionList;

    @Before
    public void setUp() {

        WebElement mockWebElementOptionOne = mock(WebElement.class);
        WebElement mockWebElementOptionTwo = mock(WebElement.class);
        mockWebElementOptionList = Arrays.asList(mockWebElementOptionOne, mockWebElementOptionTwo);
    }

    @Test
    public void dropDownWithTagNameTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        Assert.assertTrue(dropDownElement instanceof PDropDown);
    }

    @Test(expected = UnexpectedTagNameException.class)
    public void dropDownNonMatchingTagNameTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.INPUT, true);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        Assert.assertTrue(dropDownElement instanceof PDropDown);
    }

    @Test
    public void dropDownSelectByVisibleTextUnorderedListTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.xpath(".//li[normalize-space(.) = " + Quotes.escape(DROP_DOWN_VALUE) + "]"))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByVisibleText(DROP_DOWN_VALUE);
        verify(getMockWebElement(), times(1)).getTagName();
    }

    @Test
    public void dropDownSelectByVisibleTextOrderedListTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.xpath(".//ol[normalize-space(.) = " + Quotes.escape(DROP_DOWN_VALUE) + "]"))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByVisibleText(DROP_DOWN_VALUE);
        verify(getMockWebElement(), times(1)).getTagName();
    }

    @Test(expected = NoSuchElementException.class)
    public void selectByIndexUnorderedListIndexGreaterThanListSizeTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByIndex(3);
        verify(getMockWebElement(), times(1)).getTagName();
    }

    @Test(expected = NoSuchElementException.class)
    public void selectByIndexUnorderedListIndexLessThanListSizeTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByIndex(-1);
        verify(getMockWebElement(), times(19)).getTagName();
    }

    @Test
    public void selectByIndexUnorderedListIndexEqualLowerBoundListSizeTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByIndex(0);
        verify(getMockWebElement(), times(1)).getTagName();
    }

    @Test(expected = NoSuchElementException.class)
    public void selectByIndexUnorderedListIndexEqualUpperBoundListSizeTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByIndex(2);
        verify(getMockWebElement(), times(1)).getTagName();
    }

    @Test
    public void selectByIndexUnorderedListIndexInRangeListSizeTest() {

        createMockElement("777", WebElementAttribute.ID, WebElementType.P_DROP_DOWN, true);
        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(mockWebElementOptionList);
        PDropDown dropDownElement = new PDropDown(getMockWebElement());
        dropDownElement.selectByIndex(1);
        verify(getMockWebElement(), times(1)).getTagName();
    }

}
