package qcom.itlegal.ipit.framework;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;
import qcom.itlegal.ipit.framework.controller.WebEventController;
import qcom.itlegal.ipit.framework.data.CssProperty;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.service.WebEventService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebTestCaseTest extends WebTestCase {

    public static final String IDENTIFIER = "//div[@id='top-nav']";
    public static final String EXPECTED_TEXT = "foobar";

    private WebEventController mockWebEventController;
    private WebEventService mockWebEventService;

    @Before
    public void setUp() {
        mockWebEventController = mock(WebEventController.class);
        mockWebEventService = mock(WebEventService.class);

        when(mockWebEventController.getWebEventService()).thenReturn(mockWebEventService);
        webEventController = mockWebEventController;
        webEventController.setWebEventService(mockWebEventService);
    }

    @Test
    public void deleteCookieTest() {
        deleteCookie("PAD-Cookie");
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
        webElementAttributeContains(IDENTIFIER, WebElementAttribute.HREF, "qualcomm");
    }

    @Test
    public void webElementAttributeNotContainsTest() {
        webElementAttributeNotContains(IDENTIFIER, WebElementAttribute.HREF, "qualcomm");
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

    @Override
    public void beforeMethod() {
    }

    @Override
    public void afterMethod() {
    }
}
