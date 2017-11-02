package com.pwc.core.framework;


import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;

import java.util.logging.Level;

public abstract class WebTestCase extends MicroserviceTestSuite {

    /**
     * Delete a cookie from the current WebDriver
     *
     * @param cookieIdentifier cookie to delete from current driver
     */
    protected void deleteCookie(final String cookieIdentifier) {
        webEventController.getWebEventService().deleteCookie(cookieIdentifier);
    }

    /**
     * Add a cookie to the current WebDriver
     *
     * @param cookieName             name of cookie
     * @param cookieValue            value of cookie
     * @param cookieDomain           domain associated with cookie
     * @param cookiePath             path of cookie
     * @param cookieExpiryDateOffset expiration date
     * @param secureCookie           is cookie secure
     */
    protected void addCookie(final String cookieName, final String cookieValue, String cookieDomain, String cookiePath, Object cookieExpiryDateOffset, boolean secureCookie) {
        webEventController.getWebEventService().addCookie(cookieName, cookieValue, cookieDomain, cookiePath, cookieExpiryDateOffset, secureCookie);
    }

    /**
     * Cookie to modify
     *
     * @param cookieName             name of cookie
     * @param cookieValue            value of cookie
     * @param cookieDomain           domain associated with cookie
     * @param cookiePath             path of cookie
     * @param cookieExpiryDateOffset expiration date
     * @param secureCookie           is cookie secure
     */
    protected void modifyCookie(final String cookieName, final String cookieValue, String cookieDomain, String cookiePath, int cookieExpiryDateOffset, boolean secureCookie) {
        webEventController.getWebEventService().modifyCookie(cookieName, cookieValue, cookieDomain, cookiePath, cookieExpiryDateOffset, secureCookie);
    }

    /**
     * Verify the number of rows in a table
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedRowCount  expected number of rows
     */
    protected void webElementRowCountEquals(final String elementIdentifier, int expectedRowCount) {
        webEventController.getWebEventService().tableRowCount(elementIdentifier, expectedRowCount);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param expectedText      WebElements text value
     */
    protected void webElementTableTextContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, true);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param expectedText      WebElements text value
     */
    protected void webElementTableTextNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, false);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void webElementTableTextContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void webElementTableTextNotContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void webElementTableTextEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void webElementTableTextNotEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    /**
     * Execute Javascript via Selenium interface
     *
     * @param javascriptSnippet js String to execute
     * @return resulting object
     */
    protected Object executeJavascript(final String javascriptSnippet) {
        return webEventController.getWebEventService().executeJavascript(javascriptSnippet);
    }

    /**
     * Navigate directly to a particular URL
     *
     * @param url well-formed web URL or partial URL
     * @return duration took to perform page redirect
     */
    protected long redirect(final String url) {
        return webEventController.getWebEventService().redirectToUrl(url);
    }

    /**
     * Verify a CSS value Equals for this element based on the given css property
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void webElementCssPropertyEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    /**
     * Verify a CSS value Not Equals for this element based on the given css property
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void webElementCssPropertyNotEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    /**
     * Verify a CSS value contains for this element based on the given css property
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void webElementCssPropertyContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    /**
     * Verify a CSS value not contains for this element based on the given css property
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void webElementCssPropertyNotContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    /**
     * Check current page is the expected page based on the page title
     *
     * @param expectedPageTitle expected page title
     */
    protected void isExpectedPage(final String expectedPageTitle) {
        webEventController.getWebEventService().pageTitleEquals(expectedPageTitle);
    }

    /**
     * Verify alert windows and their msg attributes then click the appropriate button
     *
     * @param expectedAlertText Alert msg to verify
     * @param buttonTextToClick Alert button to select
     */
    protected void alertTextEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, true);
    }

    /**
     * Verify alert windows and their msg attributes not equals then click the appropriate button
     *
     * @param expectedAlertText Alert msg to verify
     * @param buttonTextToClick Alert button to select
     */
    protected void alertTextNotEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, false);
    }

    /**
     * Verify alert window contains text and their msg attributes then click the appropriate button
     *
     * @param expectedAlertText  Alert msg to verify it contains
     * @param expectedButtonText Alert button to select
     */
    protected void alertTextContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, true);
    }

    /**
     * Verify alert window not contains text and their msg attributes then click the appropriate button
     *
     * @param expectedAlertText  Alert msg to verify it contains
     * @param expectedButtonText Alert button to select
     */
    protected void alertTextNotContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, false);
    }

    /**
     * Check if the current page source contains the expectedText anywhere regardless of the
     * case
     *
     * @param expected expected text to locate on current page
     */
    protected void webElementTextExists(final String expected) {
        webEventController.getWebEventService().elementTextExists(expected, true);
    }

    /**
     * Check if the current page source not contains the expectedText anywhere regardless of the
     * case
     *
     * @param notExpected expected text to not locate on current page
     */
    protected void webElementTextNotExists(final String notExpected) {
        webEventController.getWebEventService().elementTextExists(notExpected, false);
    }

    /**
     * Check if the current page contains an WebElement with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param expected          WebElements text value
     */
    protected void webElementTextEquals(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, expected, true);
    }

    /**
     * Check if the current page not contains an WebElement with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param notExpected       WebElement's text value to not find
     */
    protected void webElementTextNotEquals(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, notExpected, false);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param expected          WebElements text value
     */
    protected void webElementTextContains(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expected, true);
    }

    /**
     * Check if the current page not contains an element with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param notExpected       WebElements text value to not find
     */
    protected void webElementTextNotContains(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, notExpected, false);
    }

    /**
     * Check if the current page contains a visible element
     *
     * @param elementIdentifier WebElement to find
     */
    protected void webElementVisible(final String elementIdentifier) {
        webEventController.getWebEventService().elementVisible(elementIdentifier);
    }

    /**
     * Check if the current page not contains a visible element
     *
     * @param elementIdentifier WebElement to not find
     */
    protected void webElementNotVisible(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotVisible(elementIdentifier);
    }

    /**
     * Check if the current page contains an element
     *
     * @param elementIdentifier WebElement to find
     */
    protected void webElementExists(final String elementIdentifier) {
        webEventController.getWebEventService().elementExists(elementIdentifier);
    }

    /**
     * Check if the current page not contains an element
     *
     * @param elementIdentifier WebElement to not find
     */
    protected void webElementNotExists(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotExists(elementIdentifier);
    }

    /**
     * Check if the current page contains an element
     *
     * @param elementIdentifier      WebElement to find
     * @param attributeIdentifier    element property to validate
     * @param expectedAttributeValue expected property textual value
     */
    protected void webElementAttributeEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                             final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier,
                expectedAttributeValue, true);
    }

    /**
     * Check if the current page not contains an element
     *
     * @param elementIdentifier         WebElement to find
     * @param attributeIdentifier       element property to validate
     * @param notExpectedAttributeValue expected property textual value to not find
     */
    protected void webElementAttributeNotEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                                final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier,
                notExpectedAttributeValue, false);
    }

    /**
     * Check if the current WebElement's property contains a text
     *
     * @param elementIdentifier      WebElement to find
     * @param attributeIdentifier    WebElement attribute to interrogate
     * @param expectedAttributeValue expected WebElement attribute
     */
    protected void webElementAttributeContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                               final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier,
                expectedAttributeValue, true);
    }

    /**
     * Check if the current WebElement's property not contains a text
     *
     * @param elementIdentifier         WebElement to find
     * @param attributeIdentifier       WebElement attribute to interrogate
     * @param notExpectedAttributeValue expected WebElement attribute to not find
     */
    protected void webElementAttributeNotContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                                  final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier,
                notExpectedAttributeValue, false);
    }

    /**
     * Verify hover text contains text when hovering over an element
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedText      expected hover text
     */
    protected void webElementHoverTextContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, true);
    }

    /**
     * Verify hover text not contains text when hovering over an element
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedText      expected hover text to not find
     */
    protected void webElementHoverTextNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, false);
    }

    /**
     * Wait for Element to disappear in the browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to NOT display is going to surly
     * disappear
     *
     * @param elementIdentifier element to find visibly not displayed
     */
    protected void waitForElementToDisappear(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisappear(elementIdentifier);
    }

    /**
     * Wait for Element to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to display is going to surly
     * display
     *
     * @param elementIdentifier element to find visibly
     */
    protected void waitForElementToDisplay(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisplay(elementIdentifier);
    }

    /**
     * Wait for Text to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your textToWaitToDisplay to wait for to display is going to surly
     * display
     *
     * @param textToWaitToDisplay text to wait for to display
     */
    protected void waitForTextToDisplay(final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForTextToDisplay(textToWaitToDisplay);
    }

    /**
     * Wait for Element to load in browser with expected text contained in the element.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.
     *
     * @param elementIdentifier   element to find visibly
     * @param textToWaitToDisplay text to wait for to display
     */
    protected void waitForElementToDisplayContainingText(final String elementIdentifier, final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForElementToDisplayContainingText(elementIdentifier, textToWaitToDisplay);
    }

    /**
     * Wait for Text to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your textToWaitToDisplay to wait for to display is going to surly
     * display
     *
     * @param textToWaitToDisappear text to wait for to disappear
     */
    protected void waitForTextToDisappear(final String textToWaitToDisappear) {
        webEventController.getWebEventService().waitForTextToDisappear(textToWaitToDisappear);
    }

    /**
     * Wait for all active requests to complete before proceeding.
     * Will timeout after a specified number of seconds and allow test to continue.
     */
    protected void waitForBrowserToLoad() {
        webEventController.getWebEventService().waitForBrowserToLoad();
    }

    /**
     * Refresh the currently displayed browser
     */
    protected void refreshBrowser() {
        webEventController.getWebEventService().refreshBrowser();
    }

    /**
     * Execute blur Event on a given element via xPath or by ID
     *
     * @param elementIdentifier element identifier (xPath or explicite Element ID)
     */
    protected void webElementBlur(final String elementIdentifier) {
        webEventController.getWebEventService().elementBlur(elementIdentifier);
    }

    /**
     * Returns a formatted string using the specified format string and
     * arguments.
     *
     * @param base formatted <code>String</code>
     * @param args Arguments referenced by the format specifiers in the format
     *             string.
     * @return formatted String
     */
    protected String combine(final String base, final Object... args) {
        return String.format(base, args);
    }

    /**
     * Get the property of this element, including sub-elements
     *
     * @param elementIdentifier WebElement to find via xpath or unique identifier
     * @param attribute         WebElement's specific property to look for
     * @return text value of element
     */
    protected String getWebElementAttribute(final String elementIdentifier, final WebElementAttribute attribute) {
        return webEventController.getWebEventService().getAttribute(elementIdentifier, attribute);
    }

    /**
     * Get the visible (i.e. not hidden by CSS) innerText of this element, including sub-elements,
     * without any leading or trailing whitespace.
     *
     * @param elementIdentifier WebElement to find via xpath or unique identifier
     * @return text value of element
     */
    protected String getWebElementText(final String elementIdentifier) {
        return webEventController.getWebEventService().getText(elementIdentifier);
    }

    /**
     * * Check if the current page's Console does contains a sub-string message
     *
     * @param consoleIdentifier case-insensitive snippet of console log output to find
     */
    protected void webDiagnosticsConsoleContains(final String consoleIdentifier) {
        webEventController.getWebEventService().webConsoleRequestContains(consoleIdentifier, true);
    }

    /**
     * Check if the current page's Console does not contains a sub-string message
     *
     * @param consoleIdentifier case-insensitive snippet of console log output to find
     */
    protected void webDiagnosticsConsoleNotContains(final String consoleIdentifier) {
        webEventController.getWebEventService().webConsoleRequestContains(consoleIdentifier, false);
    }

    /**
     * Check if the current page contains Console errors at a given log level or below
     *
     * @param targetLogLevel target log java.util.Level
     */
    protected void webDiagnosticsConsoleLevelBelow(final Level targetLogLevel) {
        webEventController.getWebEventService().webConsoleRequestLevel(targetLogLevel);
    }

    /**
     * Get current Network requests that contain a particular request identifier and verify occurrence count
     *
     * @param requestIdentifier       target request identifier to do a case-insensitive match against
     * @param matchingOccurrenceCount expected number of request occurrences
     */
    public void webDiagnosticsRequestCountEquals(final String requestIdentifier, final int matchingOccurrenceCount) {
        webEventController.getWebEventService().webNetworkRequestCount(requestIdentifier, matchingOccurrenceCount);
    }

}
