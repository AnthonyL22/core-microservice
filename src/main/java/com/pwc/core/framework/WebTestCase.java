package com.pwc.core.framework;


import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;

public abstract class WebTestCase extends MicroserviceTestSuite {

    protected void deleteCookie(final String cookieIdentifier) {
        webEventController.getWebEventService().deleteCookie(cookieIdentifier);
    }

    protected void addCookie(final String cookieName, final String cookieValue, String cookieDomain, String cookiePath, Object cookieExpiryDateOffset, boolean secureCookie) {
        webEventController.getWebEventService().addCookie(cookieName, cookieValue, cookieDomain, cookiePath, cookieExpiryDateOffset, secureCookie);
    }

    protected void modifyCookie(final String cookieName, final String cookieValue, String cookieDomain, String cookiePath, int cookieExpiryDateOffset, boolean secureCookie) {
        webEventController.getWebEventService().modifyCookie(cookieName, cookieValue, cookieDomain, cookiePath, cookieExpiryDateOffset, secureCookie);
    }

    protected void webElementRowCountEquals(final String elementIdentifier, int expectedRowCount) {
        webEventController.getWebEventService().tableRowCount(elementIdentifier, expectedRowCount);
    }

    protected void webElementTableTextContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, true);
    }

    protected void webElementTableTextNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, false);
    }

    protected void webElementTableTextContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    protected void webElementTableTextNotContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    protected void webElementTableTextEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    protected void webElementTableTextNotEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    protected Object executeJavascript(final String javascriptSnippet) {
        return webEventController.getWebEventService().executeJavascript(javascriptSnippet);
    }

    protected long redirect(final String url) {
        return webEventController.getWebEventService().redirectToUrl(url);
    }

    protected void webElementCssPropertyEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    protected void webElementCssPropertyNotEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    protected void webElementCssPropertyContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    protected void webElementCssPropertyNotContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    protected void isExpectedPage(final String expectedPageTitle) {
        webEventController.getWebEventService().pageTitleEquals(expectedPageTitle);
    }

    protected void alertTextEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, true);
    }

    protected void alertTextNotEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, false);
    }

    protected void alertTextContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, true);
    }

    protected void alertTextNotContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, false);
    }

    protected void webElementTextExists(final String expected) {
        webEventController.getWebEventService().elementTextExists(expected, true);
    }

    protected void webElementTextNotExists(final String notExpected) {
        webEventController.getWebEventService().elementTextExists(notExpected, false);
    }

    protected void webElementTextEquals(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, expected, true);
    }

    protected void webElementTextNotEquals(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, notExpected, false);
    }

    protected void webElementTextContains(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expected, true);
    }

    protected void webElementTextNotContains(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, notExpected, false);
    }

    protected void webElementVisible(final String elementIdentifier) {
        webEventController.getWebEventService().elementVisible(elementIdentifier);
    }

    protected void webElementNotVisible(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotVisible(elementIdentifier);
    }

    protected void webElementExists(final String elementIdentifier) {
        webEventController.getWebEventService().elementExists(elementIdentifier);
    }

    protected void webElementNotExists(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotExists(elementIdentifier);
    }

    protected void webElementAttributeEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                             final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier,
                expectedAttributeValue, true);
    }

    protected void webElementAttributeNotEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                                final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier,
                notExpectedAttributeValue, false);
    }

    protected void webElementAttributeContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                               final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier,
                expectedAttributeValue, true);
    }

    protected void webElementAttributeNotContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                                  final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier,
                notExpectedAttributeValue, false);
    }

    protected void webElementHoverTextContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, true);
    }

    protected void webElementHoverTextNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, false);
    }

    protected void waitForElementToDisappear(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisappear(elementIdentifier);
    }

    protected void waitForElementToDisplay(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisplay(elementIdentifier);
    }

    protected void waitForTextToDisplay(final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForTextToDisplay(textToWaitToDisplay);
    }

    protected void waitForElementToDisplayContainingText(final String elementIdentifier, final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForElementToDisplayContainingText(elementIdentifier, textToWaitToDisplay);
    }

    protected void waitForTextToDisappear(final String textToWaitToDisappear) {
        webEventController.getWebEventService().waitForTextToDisappear(textToWaitToDisappear);
    }

    protected void waitForBrowserToLoad() {
        webEventController.getWebEventService().waitForBrowserToLoad();
    }

    protected void refreshBrowser() {
        webEventController.getWebEventService().refreshBrowser();
    }

    protected void webElementBlur(final String elementIdentifier) {
        webEventController.getWebEventService().elementBlur(elementIdentifier);
    }

    protected String combine(final String base, final Object... args) {
        return String.format(base, args);
    }

    protected String getWebElementAttribute(final String elementIdentifier, final WebElementAttribute attribute) {
        return webEventController.getWebEventService().getAttribute(elementIdentifier, attribute);
    }

    protected String getWebElementText(final String elementIdentifier) {
        return webEventController.getWebEventService().getText(elementIdentifier);
    }

}
