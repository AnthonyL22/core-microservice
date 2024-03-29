package com.pwc.core.framework;

import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.logging.service.LoggerService;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public abstract class DeclarativeTestCase extends MicroserviceTestSuite {

    /**
     * Verify the number of rows in a table.
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedRowCount  expected number of rows
     */
    protected void tableRowCountEquals(final String elementIdentifier, int expectedRowCount) {
        webEventController.getWebEventService().tableRowCount(elementIdentifier, expectedRowCount);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it.
     *
     * @param elementIdentifier WebElement to find
     * @param expectedText      WebElements text value
     */
    protected void tableContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, true);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based.
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void tableContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based.
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void tableNotContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it.
     *
     * @param elementIdentifier WebElement to find
     * @param expectedText      WebElements text value
     */
    protected void tableNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, false);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based.
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void tableEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based.
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowNumber         row number of table
     * @param columnNumber      column number of table
     */
    protected void tableNotEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    /**
     * Execute Javascript via Selenium interface.
     *
     * @param javascriptSnippet js String to execute
     * @return resulting object
     */
    protected Object runJavascript(final String javascriptSnippet) {
        return webEventController.getWebEventService().executeJavascript(javascriptSnippet);
    }

    /**
     * Execute Javascript via Selenium interface.
     *
     * @param javascriptSnippet js String to execute
     * @param args              optional replaceable Javascript arguments
     * @return resulting object
     */
    protected Object runJavascript(final String javascriptSnippet, final Object... args) {
        return webEventController.getWebEventService().executeJavascript(combine(javascriptSnippet, args));
    }

    /**
     * Navigate directly to a particular URL.
     *
     * @param url  base well-formed web URL or partial URL
     * @param args Optional url arguments to concatenate to base url
     * @return duration took to perform page redirect
     */
    protected long redirect(final String url, Object... args) {

        long duration;
        if (null != args) {
            duration = webEventController.getWebEventService().redirectToUrl(combine(url, args));
        } else {
            duration = webEventController.getWebEventService().redirectToUrl(url);
        }
        waitForBrowserToLoad();
        return duration;
    }

    /**
     * Verify a CSS value Equals for this element based on the given css property.
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void cssPropertyEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    /**
     * Verify a CSS value Not Equals for this element based on the given css property.
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void cssPropertyNotEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    /**
     * Verify a CSS value contains for this element based on the given css property.
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void cssPropertyContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    /**
     * Verify a CSS value not contains for this element based on the given css property.
     *
     * @param elementIdentifier       WebElement to find via xpath or unique identifier
     * @param cssProperty             WebElement's CSS property to validate
     * @param expectedCssPropertyText Expected CSS property value
     */
    protected void cssPropertyNotContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    /**
     * Check current page is the expected page based on the page title.
     *
     * @param expectedPageTitle expected page title
     */
    protected void expectedPage(final String expectedPageTitle) {
        webEventController.getWebEventService().pageTitleEquals(expectedPageTitle);
    }

    /**
     * Verify alert windows and their msg attributes then click the appropriate button.
     *
     * @param expectedAlertText Alert msg to verify
     * @param buttonTextToClick Alert button to select
     */
    protected void alertEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, true);
    }

    /**
     * Verify alert windows and their msg attributes not equals then click the appropriate button.
     *
     * @param expectedAlertText Alert msg to verify
     * @param buttonTextToClick Alert button to select
     */
    protected void alertNotEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, false);
    }

    /**
     * Verify alert window contains text and their msg attributes then click the appropriate button.
     *
     * @param expectedAlertText  Alert msg to verify it contains
     * @param expectedButtonText Alert button to select
     */
    protected void alertContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, true);
    }

    /**
     * Verify alert window not contains text and their msg attributes then click the appropriate button.
     *
     * @param expectedAlertText  Alert msg to verify it contains
     * @param expectedButtonText Alert button to select
     */
    protected void alertNotContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, false);
    }

    /**
     * Check if the current page source contains the expectedText anywhere regardless of the
     * case.
     *
     * @param expected expected text to locate on current page
     */
    protected void textExists(final String expected) {
        webEventController.getWebEventService().elementTextExists(expected, true);
    }

    /**
     * Check if the current page source not contains the expectedText anywhere regardless of the
     * case.
     *
     * @param notExpected expected text to not locate on current page
     */
    protected void textNotExists(final String notExpected) {
        webEventController.getWebEventService().elementTextExists(notExpected, false);
    }

    /**
     * Check if the current page contains an WebElement with the expectedText displayed in it.
     *
     * @param elementIdentifier WebElement to find
     * @param expected          WebElements text value
     */
    protected void elementTextEquals(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, expected, true);
    }

    /**
     * Check if the current page not contains an WebElement with the expectedText displayed in it.
     *
     * @param elementIdentifier WebElement to find
     * @param notExpected       WebElement's text value to not find
     */
    protected void elementTextNotEquals(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, notExpected, false);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it.
     *
     * @param elementIdentifier WebElement to find
     * @param expected          WebElements text value
     */
    protected void elementTextContains(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expected, true);
    }

    /**
     * Check if the current page not contains an element with the expectedText displayed in it.
     *
     * @param elementIdentifier WebElement to find
     * @param notExpected       WebElements text value to not find
     */
    protected void elementTextNotContains(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, notExpected, false);
    }

    /**
     * Check if the current page contains a visible element.
     *
     * @param elementIdentifier WebElement to find
     */
    protected void visible(final String elementIdentifier) {
        webEventController.getWebEventService().elementVisible(elementIdentifier);
    }

    /**
     * Check if an element is displayed in the current browser viewport.
     *
     * @param elementIdentifier element identifier
     * @return true | false flag if visible to a real browser user
     */
    protected boolean visibleInViewport(final String elementIdentifier) {
        return webEventController.getWebEventService().isWebElementInViewport(elementIdentifier);
    }

    /**
     * Check if the current page not contains a visible element.
     *
     * @param elementIdentifier WebElement to not find
     */
    protected void notVisible(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotVisible(elementIdentifier);
    }

    /**
     * Check if the current page contains an element.
     *
     * @param elementIdentifier WebElement to find
     */
    protected void exists(final String elementIdentifier) {
        webEventController.getWebEventService().elementExists(elementIdentifier);
    }

    /**
     * Check if the current page not contains an element.
     *
     * @param elementIdentifier WebElement to not find
     */
    protected void notExists(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotExists(elementIdentifier);
    }

    /**
     * Check if the current page contains an element.
     *
     * @param elementIdentifier      WebElement to find
     * @param attributeIdentifier    element property to validate
     * @param expectedAttributeValue expected property textual value
     */
    protected void attributeEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier, final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier, expectedAttributeValue, true);
    }

    /**
     * Check if the current page not contains an element.
     *
     * @param elementIdentifier         WebElement to find
     * @param attributeIdentifier       element property to validate
     * @param notExpectedAttributeValue expected property textual value to not find
     */
    protected void attributeNotEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier, final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier, notExpectedAttributeValue, false);
    }

    /**
     * Check if the current WebElement's property contains a text.
     *
     * @param elementIdentifier      WebElement to find
     * @param attributeIdentifier    WebElement attribute to interrogate
     * @param expectedAttributeValue expected WebElement attribute
     */
    protected void attributeContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier, final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier, expectedAttributeValue, true);
    }

    /**
     * Check if the current WebElement's property not contains a text.
     *
     * @param elementIdentifier         WebElement to find
     * @param attributeIdentifier       WebElement attribute to interrogate
     * @param notExpectedAttributeValue expected WebElement attribute to not find
     */
    protected void attributeNotContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier, final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier, notExpectedAttributeValue, false);
    }

    /**
     * Verify hover text contains text when hovering over an element.
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedText      expected hover text
     */
    protected void hoverTextContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, true);
    }

    /**
     * Verify hover text not contains text when hovering over an element.
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedText      expected hover text to not find
     */
    protected void hoverNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, false);
    }

    /**
     * Measure the performance duration of how long it takes for a WebElement to appear in the browser to the user.
     *
     * @param elementIdentifier element to find visibility
     * @return duration in seconds for the element to display
     */
    protected int durationToAppear(final String elementIdentifier) {
        return webEventController.getWebEventService().measureDurationForElementToAppear(elementIdentifier);
    }

    /**
     * Measure the performance duration of how long it takes for a WebElement to disappear from the browser.
     *
     * @param elementIdentifier element to find to be not visible
     * @return duration in seconds for the element to disappear
     */
    protected int durationToDisappear(final String elementIdentifier) {
        return webEventController.getWebEventService().measureDurationForElementToDisappear(elementIdentifier);
    }

    /**
     * Wait for Element to be enabled in the browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for becoming enabled is going to surly
     * become enabled.
     *
     * @param elementIdentifier element to find to be enabled
     */
    protected void waitToBeEnabled(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToBecomeEnabled(elementIdentifier);
    }

    /**
     * Wait for Element to be disabled in the browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for becoming disabled is going to surly
     * become disabled.
     *
     * @param elementIdentifier element to find to be disabled
     */
    protected void waitToBeDisabled(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToBecomeDisabled(elementIdentifier);
    }

    /**
     * Wait for Element to disappear in the browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to NOT display is going to surly
     * disappear.
     *
     * @param elementIdentifier element to find visibly not displayed
     */
    protected void waitToDisappear(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisappear(elementIdentifier);
    }

    /**
     * Wait for Element to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to display is going to surly
     * display.
     *
     * @param elementIdentifier element to find visibly
     */
    protected void waitToDisplay(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisplay(elementIdentifier);
    }

    /**
     * Wait for Text to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your textToWaitToDisplay to wait for to display is going to surly
     * display.
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
    protected void waitElementContainsText(final String elementIdentifier, final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForElementToDisplayContainingText(elementIdentifier, textToWaitToDisplay);
    }

    /**
     * Wait for Text to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your textToWaitToDisplay to wait for to display is going to surly
     * display.
     *
     * @param textToWaitToDisappear text to wait for to disappear
     */
    protected void waitForTextToDisappear(final String textToWaitToDisappear) {
        webEventController.getWebEventService().waitForTextToDisappear(textToWaitToDisappear);
    }

    /**
     * Wait for all active requests to complete before proceeding.  Will timeout after a specified number of seconds and allow test to continue.
     */
    protected void waitForBrowserToLoad() {
        webEventController.getWebEventService().waitForBrowserToLoad();
    }

    /**
     * Refresh the currently displayed browser.
     */
    protected void refreshPage() {
        webEventController.getWebEventService().refreshBrowser();
    }

    /**
     * Execute blur Event on a given element via xPath or by ID.
     *
     * @param elementIdentifier element identifier (xPath or explicite Element ID)
     */
    protected void blur(final String elementIdentifier) {
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
     * Get the property of this element, including sub-elements.
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
     * Get a list of the current Page's console log entries.
     *
     * @return List of console log entries
     */
    protected List<LogEntry> getConsoleRequests() {
        return webEventController.getWebEventService().getConsoleRequests();
    }

    /**
     * Check if the current page's Console does contains a sub-string message.
     *
     * @param consoleIdentifier case-insensitive snippet of console log output to find
     */
    protected void consoleContains(final String consoleIdentifier) {
        webEventController.getWebEventService().webConsoleRequestContains(consoleIdentifier, true);
    }

    /**
     * Check if the current page's Console does not contains a sub-string message.
     *
     * @param consoleIdentifier case-insensitive snippet of console log output to find
     */
    protected void consoleNotContains(final String consoleIdentifier) {
        webEventController.getWebEventService().webConsoleRequestContains(consoleIdentifier, false);
    }

    /**
     * Check if the current page contains Console errors at a given log level or below.
     *
     * @param targetLogLevel target log java.util.Level
     */
    protected void consoleLevelBelow(final Level targetLogLevel) {
        webEventController.getWebEventService().webConsoleRequestLevel(targetLogLevel);
    }

    /**
     * Check if the Console contains entries greater than or equal to the allowable Level.  This is a filtered list
     * for this specific project.
     *
     * @param elementIdentifier WebElement to wait for to display before reading Console tab data
     * @param level             {@link Level} the level to filter the log entries
     * @param requestIgnoreSet  Set of Console requests to ignore from assertion
     */
    protected void consoleLevelGreaterThanOrEqual(final String elementIdentifier, final Level level, final Set<String> requestIgnoreSet) {
        webEventController.getWebEventService().webConsoleRequestGreaterThanOrEqual(elementIdentifier, level, requestIgnoreSet);
    }

    /**
     * Get current Network requests that contain a particular request identifier and verify occurrence count.
     *
     * @param requestIdentifier       target request identifier to do a case-insensitive match against
     * @param matchingOccurrenceCount expected number of request occurrences
     */
    protected void networkRequestCountEquals(final String requestIdentifier, final int matchingOccurrenceCount) {
        webEventController.getWebEventService().webNetworkRequestCount(requestIdentifier, matchingOccurrenceCount);
    }

    /**
     * Get Set of current Network requests Set that contain a particular request identifier.
     *
     * @param requestIdentifier target request identifier to do a case-insensitive match against
     * @return unique Set of matching network tab requests
     */
    protected Set<String> networkRequestMatch(final String requestIdentifier) {
        return webEventController.getWebEventService().webNetworkRequestMatch(requestIdentifier);
    }

    /**
     * Get a list of the current Network requests.
     *
     * @return unique List of network tab requests
     */
    protected List<String> getNetworkRequests() {
        return webEventController.getWebEventService().getPageRequests();
    }

    /**
     * Get a list of all Network log entry requests.
     *
     * @return unique List of network log entry requests
     */
    protected List<LogEntry> getNetworkLogEntries() {

        List<LogEntry> logEntries = new ArrayList<>();
        try {
            logEntries.addAll(webEventController.getWebEventService().getMicroserviceWebDriver().manage().logs().get(LogType.BROWSER).getAll());
            logEntries.addAll(webEventController.getWebEventService().getMicroserviceWebDriver().manage().logs().get(LogType.PERFORMANCE).getAll());
        } catch (Exception e) {
            LoggerService.LOG(false, "Unable to get all network requests due to e=%s", e);
        }
        return logEntries;
    }

}
