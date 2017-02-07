package com.pwc.core.framework;

import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;

public abstract class DeclarativeTestCase extends MicroserviceTestSuite {

    protected void tableRowCountEquals(final String elementIdentifier, int expectedRowCount) {
        webEventController.getWebEventService().tableRowCount(elementIdentifier, expectedRowCount);
    }

    protected void tableContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, true);
    }

    protected void tableNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expectedText, false);
    }

    protected void tableContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    protected void tableNotContains(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextContains(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    protected void tableEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, true);
    }

    protected void tableNotEquals(final String elementIdentifier, final String expectedText, int rowNumber, int columnNumber) {
        webEventController.getWebEventService().tableTextEquals(elementIdentifier, expectedText, rowNumber, columnNumber, false);
    }

    protected Object runJavascript(final String javascriptSnippet) {
        return webEventController.getWebEventService().executeJavascript(javascriptSnippet);
    }

    protected long go(final String url) {
        return webEventController.getWebEventService().redirectToUrl(url);
    }

    protected void cssPropertyEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    protected void cssPropertyNotEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyEquals(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    protected void cssPropertyContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, true);
    }

    protected void cssPropertyNotContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedCssPropertyText) {
        webEventController.getWebEventService().elementCssPropertyContains(elementIdentifier, cssProperty, expectedCssPropertyText, false);
    }

    protected void expectedPage(final String expectedPageTitle) {
        webEventController.getWebEventService().pageTitleEquals(expectedPageTitle);
    }

    protected void alertEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, true);
    }

    protected void alertNotEquals(final String expectedAlertText, String buttonTextToClick) {
        webEventController.getWebEventService().alertTextEquals(expectedAlertText, buttonTextToClick, false);
    }

    protected void alertContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, true);
    }

    protected void alertNotContains(final String expectedAlertText, String expectedButtonText) {
        webEventController.getWebEventService().alertTextContains(expectedAlertText, expectedButtonText, false);
    }

    protected void textExists(final String expected) {
        webEventController.getWebEventService().elementTextExists(expected, true);
    }

    protected void textNotExists(final String notExpected) {
        webEventController.getWebEventService().elementTextExists(notExpected, false);
    }

    protected void elementTextEquals(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, expected, true);
    }

    protected void elementTextNotEquals(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextExists(elementIdentifier, notExpected, false);
    }

    protected void elementTextContains(final String elementIdentifier, final String expected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, expected, true);
    }

    protected void elementTextNotContains(final String elementIdentifier, final String notExpected) {
        webEventController.getWebEventService().elementTextContains(elementIdentifier, notExpected, false);
    }

    protected void visible(final String elementIdentifier) {
        webEventController.getWebEventService().elementVisible(elementIdentifier);
    }

    protected void notVisible(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotVisible(elementIdentifier);
    }

    protected void exists(final String elementIdentifier) {
        webEventController.getWebEventService().elementExists(elementIdentifier);
    }

    protected void notExists(final String elementIdentifier) {
        webEventController.getWebEventService().elementNotExists(elementIdentifier);
    }

    protected void attributeEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                   final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier,
                expectedAttributeValue, true);
    }

    protected void attributeNotEquals(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                      final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttribute(elementIdentifier, attributeIdentifier,
                notExpectedAttributeValue, false);
    }

    protected void attributeContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                     final String expectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier,
                expectedAttributeValue, true);
    }

    protected void attributeNotContains(final String elementIdentifier, final WebElementAttribute attributeIdentifier,
                                        final String notExpectedAttributeValue) {
        webEventController.getWebEventService().elementAttributeContains(elementIdentifier, attributeIdentifier,
                notExpectedAttributeValue, false);
    }

    protected void hoverTextContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, true);
    }

    protected void hoverNotContains(final String elementIdentifier, final String expectedText) {
        webEventController.getWebEventService().hoverContains(elementIdentifier, expectedText, false);
    }

    protected void waitToDisappear(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisappear(elementIdentifier);
    }

    protected void waitToDisplay(final String elementIdentifier) {
        webEventController.getWebEventService().waitForElementToDisplay(elementIdentifier);
    }

    protected void waitElementContainsText(final String elementIdentifier, final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForElementToDisplayContainingText(elementIdentifier, textToWaitToDisplay);
    }

    protected void waitForTextToDisplay(final String textToWaitToDisplay) {
        webEventController.getWebEventService().waitForTextToDisplay(textToWaitToDisplay);
    }

    protected void waitForTextToDisappear(final String textToWaitToDisappear) {
        webEventController.getWebEventService().waitForTextToDisappear(textToWaitToDisappear);
    }

    protected void waitForBrowserToLoad() {
        webEventController.getWebEventService().waitForBrowserToLoad();
    }

    protected void refreshPage() {
        webEventController.getWebEventService().refreshBrowser();
    }

    protected void blur(final String elementIdentifier) {
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
