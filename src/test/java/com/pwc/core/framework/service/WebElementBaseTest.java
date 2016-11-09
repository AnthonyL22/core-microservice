package com.pwc.core.framework.service;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import org.mockito.Mock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.when;

public class WebElementBaseTest {

    @Mock
    private WebDriver mockDriver;

    @Mock
    private WebElement mockWebElement;

    /**
     * Create visible anchor WebElement with the element's ID set
     *
     * @param elementIdentifier Unique element identifier
     */
    protected void createMockElement(final String elementIdentifier) {
        createMockElement(elementIdentifier, WebElementAttribute.ID, WebElementType.ANCHOR, true);
    }

    /**
     * Create visible anchor WebElement with a given WebElement property set
     *
     * @param elementIdentifier   Unique element identifier
     * @param attributeIdentifier WebElement property identifier
     */
    protected void createMockElement(final String elementIdentifier, final WebElementAttribute attributeIdentifier) {
        createMockElement(elementIdentifier, attributeIdentifier, WebElementType.ANCHOR, true);
    }

    /**
     * Create a visible WebElement with a given WebElement property as well as WebElement type set
     *
     * @param elementIdentifier   Unique element identifier
     * @param attributeIdentifier WebElement property identifier
     * @param elementType         WebElement type identifier
     */
    protected void createMockElement(final String elementIdentifier, final WebElementAttribute attributeIdentifier, final WebElementType elementType) {
        createMockElement(elementIdentifier, attributeIdentifier, elementType, true);
    }

    /**
     * Create a WebElement with given property, type, and visibility set
     *
     * @param elementIdentifier   Unique element identifier
     * @param attributeIdentifier WebElement property identifier
     * @param elementType         WebElement type identifier
     * @param displayed           WebElement isDisplayed flag
     */
    protected void createMockElement(final String elementIdentifier, final WebElementAttribute attributeIdentifier, final WebElementType elementType, final boolean displayed) {
        when(mockDriver.findElement(By.id(elementIdentifier))).thenReturn(mockWebElement);
        when(mockWebElement.getAttribute(attributeIdentifier.attribute)).thenReturn(elementIdentifier);
        when(mockWebElement.getTagName()).thenReturn(elementType.type);
        when(mockWebElement.isDisplayed()).thenReturn(displayed);
    }

    public WebDriver getMockDriver() {
        return mockDriver;
    }

    public void setMockDriver(WebDriver mockDriver) {
        this.mockDriver = mockDriver;
    }

    public WebElement getMockWebElement() {
        return mockWebElement;
    }

    public void setMockWebElement(WebElement mockWebElement) {
        this.mockWebElement = mockWebElement;
    }

}
