package com.pwc.core.framework;

import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.service.WebEventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebTestCaseTest extends WebTestCase {

    private static final String IDENTIFIER = "//div[@id='top-nav']";
    private static final String EXPECTED_TEXT = "foobar";

    @Before
    public void setUp() {
        WebEventController mockWebEventController = mock(WebEventController.class);
        WebEventService mockWebEventService = mock(WebEventService.class);

        when(mockWebEventController.getWebEventService()).thenReturn(mockWebEventService);
        webEventController = mockWebEventController;
        webEventController.setWebEventService(mockWebEventService);
    }

    @Test
    public void deleteCookieTest() {
        deleteCookie("MY-Cookie");
    }

    @Test
    public void addCookieTest() {
        addCookie("AAA-Cookie", "1234", "na.mywebsite.com", ".", 1, true);
    }

    @Test
    public void modifyCookieTest() {
        modifyCookie("AAA-Cookie", "1234", "na.mywebsite.com", ".", 1, true);
    }

    @Test
    public void webElementRowCountEqualsTest() {
        webElementRowCountEquals(IDENTIFIER, 1);
    }

    @Test
    public void webElementTableTextContainsTest() {
        webElementTableTextContains(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void webElementTableTextNotContainsTest() {
        webElementTableTextNotContains(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void webElementTableTextContainsSpecificColumnRowTest() {
        webElementTableTextContains(IDENTIFIER, EXPECTED_TEXT, 1, 1);
    }

    @Test
    public void webElementTableTextNotContainsSpecificColumnRowTest() {
        webElementTableTextNotContains(IDENTIFIER, EXPECTED_TEXT, 1, 1);
    }

    @Test
    public void webElementTableTextEqualsTest() {
        webElementTableTextEquals(IDENTIFIER, EXPECTED_TEXT, 1, 1);
    }

    @Test
    public void webElementTableTextNotEqualsTest() {
        webElementTableTextNotEquals(IDENTIFIER, EXPECTED_TEXT, 1, 1);
    }

    @Test
    public void combineTest() {
        String result = combine("Verify element=%s exists on page #=%s", IDENTIFIER, 1);
        Assert.assertEquals(result, "Verify element=//div[@id='top-nav'] exists on page #=1");
    }

    @Test
    public void redirectTest() {
        redirect("http://www.mywebsite.com");
    }

    @Test
    public void executeJavascriptTest() {
        executeJavascript("Ext.each(Ext.ComponentQuery.query(\"tabpanel\"),function(){if (this.stateId==='TabPanelMain'){this.setActiveTab(1)}})");
    }

    @Test
    public void executeJavascriptArgsTest() {
        executeJavascript("Ext.each(Ext.ComponentQuery.query(\"tabpanel\"),function(){if (this.stateId==='%s'){this.setActiveTab(1)}})", "TabPanelMain");
    }

    @Test
    public void webElementCssPropertyEqualsTest() {
        webElementCssPropertyEquals(IDENTIFIER, CssProperty.BACKGROUND, "blue");
    }

    @Test
    public void webElementCssPropertyNotEqualsTest() {
        webElementCssPropertyNotEquals(IDENTIFIER, CssProperty.BACKGROUND, "blue");
    }

    @Test
    public void webElementCssPropertyContainsTest() {
        webElementCssPropertyContains(IDENTIFIER, CssProperty.LEFT, "10px");
    }

    @Test
    public void webElementCssPropertyNotContainsTest() {
        webElementCssPropertyNotContains(IDENTIFIER, CssProperty.COLOR, "red");
    }

    @Test
    public void getWebElementAttributeTest() {
        getWebElementAttribute(IDENTIFIER, WebElementAttribute.HREF);
    }

    @Test
    public void getWebElementTextTest() {
        getWebElementText(IDENTIFIER);
    }

    @Test
    public void isExpectedPageTest() {
        isExpectedPage(IDENTIFIER);
    }

    @Test
    public void alertTextEqualsTest() {
        alertTextEquals(EXPECTED_TEXT, "OK");
    }

    @Test
    public void alertTextNotEqualsTest() {
        alertTextNotEquals(EXPECTED_TEXT, "OK");
    }

    @Test
    public void alertTextContainsTest() {
        alertTextContains(EXPECTED_TEXT, "OK");
    }

    @Test
    public void alertTextNotContainsTest() {
        alertTextNotContains(EXPECTED_TEXT, "OK");
    }

    @Test
    public void webElementTextExistsTest() {
        webElementTextExists(EXPECTED_TEXT);
    }

    @Test
    public void webElementTextNotExists() {
        webElementTextNotExists(EXPECTED_TEXT);
    }

    @Test
    public void webElementTextEqualsTest() {
        webElementTextEquals(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void webElementTextNotEqualsTest() {
        webElementTextNotEquals(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void webElementTextContainsTest() {
        webElementTextContains(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void webElementTextNotContainsTest() {
        webElementTextNotContains(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void webElementVisibleTest() {
        webElementVisible(IDENTIFIER);
    }

    @Test
    public void webElementNotVisibleTest() {
        webElementNotVisible(IDENTIFIER);
    }

    @Test
    public void webElementExistsTest() {
        webElementExists(IDENTIFIER);
    }

    @Test
    public void webElementNotExistsTest() {
        webElementNotExists(IDENTIFIER);
    }

    @Test
    public void webElementAttributeEqualsTest() {
        webElementAttributeEquals(IDENTIFIER, WebElementAttribute.STYLE, "left");
    }

    @Test
    public void webElementAttributeNotEqualsTest() {
        webElementAttributeNotEquals(IDENTIFIER, WebElementAttribute.STYLE, "left");
    }

    @Test
    public void webElementAttributeContainsTest() {
        webElementAttributeContains(IDENTIFIER, WebElementAttribute.HREF, "pwc");
    }

    @Test
    public void webElementAttributeNotContainsTest() {
        webElementAttributeNotContains(IDENTIFIER, WebElementAttribute.HREF, "pwc");
    }

    @Test
    public void webElementHoverTextContainsTest() {
        webElementHoverTextContains(IDENTIFIER, "foobar");
    }

    @Test
    public void webElementHoverTextNotContainsTest() {
        webElementHoverTextNotContains(IDENTIFIER, "foobar");
    }

    @Test
    public void measureDurationForElementToAppearTest() {
        int duration = durationForElementToAppear(IDENTIFIER);
        Assert.assertNotEquals(duration, -1);
    }

    @Test
    public void measureDurationForElementToDisappearTest() {
        int duration = durationForElementToDisappear(IDENTIFIER);
        Assert.assertNotEquals(duration, -1);
    }

    @Test
    public void waitForElementToDisappearTest() {
        waitForElementToDisappear(IDENTIFIER);
    }

    @Test
    public void waitForElementToDisplayTest() {
        waitForElementToDisplay(IDENTIFIER);
    }

    @Test
    public void waitForTextToDisplayTest() {
        waitForTextToDisplay(EXPECTED_TEXT);
    }

    @Test
    public void waitForElementToDisplayContainingTextTest() {
        waitForElementToDisplayContainingText(IDENTIFIER, EXPECTED_TEXT);
    }

    @Test
    public void waitForTextToDisappearTest() {
        waitForTextToDisappear(EXPECTED_TEXT);
    }

    @Test
    public void webElementBlur() {
        webElementBlur(IDENTIFIER);
    }

    @Test
    public void refreshTest() {
        refreshBrowser();
    }

    @Test
    public void webConsoleContainsTest() {
        webDiagnosticsConsoleContains("IndexOutOfBounds");
    }

    @Test
    public void webConsoleNotContainsTest() {
        webDiagnosticsConsoleNotContains("foobar");
    }

    @Test
    public void webConsoleLevelAboveTest() {
        webDiagnosticsConsoleLevelBelow(Level.SEVERE);
    }

    @Test
    public void webConsoleLevelGreaterThanOrEqual() {
        Set<String> ignoreList = new HashSet<>();
        ignoreList.add("help");
        webDiagnosticsConsoleRequestGreaterThanOrEqual("//div[@id='home']", Level.SEVERE, ignoreList);
    }

    @Test
    public void webConsoleLevelGreaterThanOrEqualNullIgnore() {
        webDiagnosticsConsoleRequestGreaterThanOrEqual("//div[@id='home']", Level.SEVERE);
    }

    @Test
    public void webNetworkRequestCountEqualsTest() {
        webDiagnosticsRequestCountEquals("mycompany/api", 3);
    }

    @Override
    public void beforeMethod() {
    }

    @Override
    public void afterMethod() {
    }
}
