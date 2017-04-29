package com.pwc.core.framework.service;

import com.pwc.core.framework.JavascriptConstants;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.driver.MicroserviceWebDriver;
import com.pwc.core.framework.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebEventServiceTest extends WebElementBaseTest {

    private static final String RAW_HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head lang=\"en\">\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title></title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<div id=\"777\">Hi there</div>\n" +
            "<button id=\"saveButton\">Save Changes</button>\n" +
            "</body>\n" +
            "</html>";

    private static final String TABLE_HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head lang=\"en\">\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title></title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<table id=\"datatable\" class=\"table dataTable no-footer dtr-inline display no-wrap collapse\" role=\"grid\" aria-describedby=\"datatable_info\">\n" +
            "<thead>\n" +
            "<tbody>\n" +
            "<tr class=\"odd\" role=\"row\">\n" +
            "<td class=\"sorting_1\">ABDC</td>\n" +
            "<td>My Table Row Information Here</td>\n" +
            "<td class=\" min-tablet-l\">John Do</td>\n" +
            "<td class=\" desktop\">123456789</td>\n" +
            "</tr>" +
            "<tr class=\"even\" role=\"row\">\n" +
            "<tr class=\"odd\" role=\"row\">\n" +
            "<tr class=\"even\" role=\"row\">\n" +
            "<tr class=\"odd\" role=\"row\">\n" +
            "</tbody>\n" +
            "</table>" +
            "</html>";

    private static final String PARTIAL_HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head lang=\"en\">\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    >" +
            "</html>";

    private static final String XPATH_LOCATOR = "//div[@id='777']";
    public static final String DEFAULT_URL = "https://mywebsite.com/";
    private final String ALERT_TEXT = "This is a warning shot";

    WebEventService webEventService;

    @Mock
    WebEventService mockWebEventService;

    @Mock
    MicroserviceWebDriver mockMicroserviceWebDriver;

    @Mock
    MicroserviceWebDriver mockWebDriverService;

    @Mock
    WebElement specialMockWebElement;

    @Mock
    Alert mockAlert;

    @Mock
    WebDriver.TargetLocator mockTargetLocator;
    public static final List<String> ELEMENT_XPATHS = new ArrayList<>();
    private WebDriver.Options mockOptions;
    private WebDriver.Window mockWindow;
    private Set<Cookie> mockCookies;
    private Set<Cookie> mockEmptyCookies;
    private Cookie mockTabNameCookie;
    private org.apache.http.cookie.Cookie mockHttpCookie;

    @Before
    public void setUp() throws SQLException {

        Credentials mockCredentials = new Credentials("foo", "bar");

        webEventService = new WebEventService();

        WebElement mockWebElement = mock(WebElement.class);
        List<WebElement> webElements = new ArrayList<>();
        webElements.add(mockWebElement);

        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        when(mockWebDriverService.executeScript("return document.readyState")).thenReturn("complete");
        when(mockWebDriverService.getPageSource()).thenReturn(RAW_HTML);
        when(mockWebDriverService.getTitle()).thenReturn("Unit Testing At It's Best");

        when(mockAlert.getText()).thenReturn(ALERT_TEXT);
        when(mockTargetLocator.activeElement()).thenReturn(mockWebElement);
        when(mockWebDriverService.switchTo()).thenReturn(mockTargetLocator);
        when(mockWebDriverService.switchTo().alert()).thenReturn(mockAlert);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        when(mockWebDriverService.getCapabilities()).thenReturn(capabilities);

        ELEMENT_XPATHS.add("//div[@style='nothing']");
        ELEMENT_XPATHS.add("//div[@class='foobar']");
        ELEMENT_XPATHS.add("//div[@id='777']");

        mockOptions = mock(WebDriver.Options.class);
        mockWindow = mock(WebDriver.Window.class);

        mockTabNameCookie = mock(Cookie.class);
        when(mockTabNameCookie.getName()).thenReturn("tabName");
        when(mockTabNameCookie.getValue()).thenReturn("Stuff");
        when(mockTabNameCookie.getDomain()).thenReturn(".mywebsite.com");
        Date tabNameCookieExpireDate = DateUtils.getDateByOffset(0);
        when(mockTabNameCookie.getExpiry()).thenReturn(tabNameCookieExpireDate);

        mockHttpCookie = mock(org.apache.http.cookie.Cookie.class);
        when(mockHttpCookie.getName()).thenReturn("tabName");
        when(mockHttpCookie.getValue()).thenReturn("Stuff");
        when(mockHttpCookie.getDomain()).thenReturn(".mywebsite.com");
        when(mockHttpCookie.getExpiryDate()).thenReturn(tabNameCookieExpireDate);

        org.openqa.selenium.Cookie mockJSessionIDCookie = mock(org.openqa.selenium.Cookie.class);
        when(mockJSessionIDCookie.getName()).thenReturn("JSESSIONID");
        when(mockJSessionIDCookie.getValue()).thenReturn("B98EDA58E524D5D3151E6CAD22CCEF3D.worker1");
        when(mockJSessionIDCookie.getDomain()).thenReturn(".mywebsite.com");
        Date jSessionCookieExpireDate = DateUtils.getDateByOffset(0);
        when(mockJSessionIDCookie.getExpiry()).thenReturn(jSessionCookieExpireDate);

        mockEmptyCookies = new HashSet<>();
        mockCookies = new HashSet<>();
        mockCookies.add(mockTabNameCookie);
        mockCookies.add(mockJSessionIDCookie);

        webEventService.setUrl(DEFAULT_URL);
        webEventService.setCredentials(mockCredentials);

        BasicCookieStore mockBasicCookieStore = mock(BasicCookieStore.class);
        webEventService.setCookieStore(mockBasicCookieStore);

        when(mockWebDriverService.executeScript(JavascriptConstants.IS_OPEN_HTTPS)).thenReturn(0L);

    }

    @Test(expected = AssertionError.class)
    public void getActiveEnvStageTest() {
        webEventService.setUrl(DEFAULT_URL + "stage/index.html");
        webEventService.loadSavedCookiesFromFile();
    }

    @Test(expected = AssertionError.class)
    public void getActiveEnvUATTest() {
        webEventService.setUrl(DEFAULT_URL + "uat/index.html");
        webEventService.loadSavedCookiesFromFile();
    }

    @Test(expected = AssertionError.class)
    public void getActiveEnvTestTest() {
        webEventService.setUrl(DEFAULT_URL + "test/index.html");
        webEventService.loadSavedCookiesFromFile();
    }

    @Test(expected = AssertionError.class)
    public void getActiveEnvDEVTest() {
        webEventService.setUrl(DEFAULT_URL + "dev/index.html");
        webEventService.loadSavedCookiesFromFile();
    }

    @Test(expected = AssertionError.class)
    public void addCookiesToHttpClientSemiValidLoginResponseTest() {
        List cookieList = new ArrayList(Arrays.asList(mockHttpCookie));
        webEventService.addCookiesToHttpClient(cookieList);
    }

    @Test(expected = AssertionError.class)
    public void addCookiesToHttpClientNullLoginResponseTest() {
        webEventService.setUrl(DEFAULT_URL);
        List cookieList = new ArrayList(Arrays.asList(mockHttpCookie));
        webEventService.addCookiesToHttpClient(cookieList);
    }

    @Test
    public void redirectDoNotMaximizeMobileBrowserTest() {
        DesiredCapabilities mockCapabilities = mock(DesiredCapabilities.class);
        mockCapabilities.setBrowserName("android");
        when(mockWebDriverService.getCurrentUrl()).thenReturn(DEFAULT_URL + "/myDetails/123s4");
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockWebDriverService.getCapabilities()).thenReturn(mockCapabilities);
        when(mockWebDriverService.getCapabilities().getBrowserName()).thenReturn("android");
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("/myDetails/777");

        Assert.assertEquals(webEventService.getUrl(), DEFAULT_URL + "myDetails/777");

    }

    @Test
    public void redirectMaximizeNotMobileTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(DEFAULT_URL + "/myDetails/123s4");
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("/myDetails/777");

        Assert.assertEquals(webEventService.getUrl(), DEFAULT_URL + "myDetails/777");
        verify(mockWebDriverService, times(2)).getCurrentUrl();
        verify(mockWebDriverService, times(1)).manage();
        verify(mockOptions, times(1)).window();
    }

    @Test
    public void deleteCookieTest() {

        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.getCookies()).thenReturn(mockCookies).thenReturn(mockEmptyCookies);
        doNothing().when(mockOptions).deleteCookie(mockTabNameCookie);

        webEventService.deleteCookie(mockTabNameCookie.getName());

    }

    @Test(expected = AssertionError.class)
    public void deleteCookieNotDeletedTest() {

        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.getCookies()).thenReturn(mockCookies);
        doNothing().when(mockOptions).deleteCookie(mockTabNameCookie);

        webEventService.deleteCookie(mockTabNameCookie.getName());

    }

    @Test
    public void deleteCookieNoCookiesAvailableTest() {

        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.getCookies()).thenReturn(mockEmptyCookies);
        doNothing().when(mockOptions).deleteCookie(mockTabNameCookie);

        webEventService.deleteCookie(mockTabNameCookie.getName());

    }

    @Test
    public void addCookieIntegerExpiryTest() {

        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.getCookies()).thenReturn(mockCookies);

        doNothing().when(mockOptions).addCookie(mockTabNameCookie);
        webEventService.addCookie(mockTabNameCookie.getName(), mockTabNameCookie.getValue(),
                mockTabNameCookie.getDomain(), mockTabNameCookie.getPath(), 1, true);
    }

    @Test
    public void addCookieNullExpiryTest() {

        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.getCookies()).thenReturn(mockCookies);

        doNothing().when(mockOptions).addCookie(mockTabNameCookie);
        webEventService.addCookie(mockTabNameCookie.getName(), mockTabNameCookie.getValue(),
                mockTabNameCookie.getDomain(), mockTabNameCookie.getPath(), null, true);
    }

    @Test
    public void modifyCookieTest() {

        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.getCookies()).
                thenReturn(mockCookies).
                thenReturn(mockEmptyCookies).
                thenReturn(mockCookies);

        doNothing().when(mockOptions).deleteCookie(mockTabNameCookie);
        doNothing().when(mockOptions).addCookie(mockTabNameCookie);
        webEventService.modifyCookie(mockTabNameCookie.getName(), mockTabNameCookie.getValue(),
                mockTabNameCookie.getDomain(), mockTabNameCookie.getPath(), null, true);
    }

    @Test(expected = AssertionError.class)
    public void elementAttributeNullWebElementExpectedToBeDisplayedOnPageTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("776");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='776']")).thenReturn(specialMockWebElement);

        webEventService.elementAttribute("777", WebElementAttribute.NAME, "foobar", true);
    }

    @Test(expected = AssertionError.class)
    public void alertTextEqualsNoMatchTest() {
        webEventService.alertTextEquals(ALERT_TEXT + "foo", "", true);
    }

    @Test
    public void alertTextEqualsNoButtonTest() {
        webEventService.alertTextEquals(ALERT_TEXT, "", true);
    }

    @Test
    public void alertTextEqualsOKButtonTest() {
        webEventService.alertTextEquals(ALERT_TEXT, "OK", true);
    }

    @Test
    public void alertTextEqualsYesButtonTest() {
        webEventService.alertTextEquals(ALERT_TEXT, "Yes", true);
    }

    @Test
    public void alertTextEqualsOtherButtonTest() {
        webEventService.alertTextEquals(ALERT_TEXT, "Maybe", true);
    }

    @Test(expected = AssertionError.class)
    public void alertTextEqualsNoAlertTest() {
        when(mockWebDriverService.switchTo()).thenReturn(null);
        webEventService.alertTextEquals(ALERT_TEXT, "Maybe", true);
    }

    @Test
    public void alertTextContainsEqualsNoMatchTest() {
        webEventService.alertTextContains(StringUtils.substringAfter(ALERT_TEXT, "warning"), "", true);
    }

    @Test(expected = AssertionError.class)
    public void alertTextContainsTest() {
        webEventService.alertTextContains("foobar", "", true);
    }

    @Test
    public void alertTextContainsOKButtonTest() {
        webEventService.alertTextContains(StringUtils.substringAfter(ALERT_TEXT, "warning"), "OK", true);
    }

    @Test
    public void alertTextContainsYesButtonTest() {
        webEventService.alertTextContains(StringUtils.substringAfter(ALERT_TEXT, "warning"), "Yes", true);
    }

    @Test
    public void alertTextContainsOtherButtonTest() {
        webEventService.alertTextContains(StringUtils.substringAfter(ALERT_TEXT, "warning"), "Maybe", true);
    }

    @Test(expected = AssertionError.class)
    public void alertTextContainsNoAlertTest() {
        when(mockWebDriverService.switchTo()).thenReturn(null);
        webEventService.alertTextContains(StringUtils.substringAfter(ALERT_TEXT, "warning"), "Maybe", true);
    }

    @Test
    public void redirectToUrlSegmentTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(DEFAULT_URL);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("/foobar/myTeam");
    }

    @Test
    public void redirectToFullUrlTest() {
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(DEFAULT_URL);
    }

    @Test
    public void redirectToFullAndMoreUrlTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(DEFAULT_URL);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("/myDetails/1234");

        Assert.assertEquals(webEventService.getUrl(), DEFAULT_URL + "myDetails/1234");
        verify(mockWebDriverService, times(2)).getCurrentUrl();
        verify(mockWebDriverService, times(1)).manage();
        verify(mockOptions, times(1)).window();
    }

    @Test
    public void redirectPreviouslyDecoratedUrlTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(DEFAULT_URL + "/myDetails/1234");
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("/myDetails/777");

        Assert.assertEquals(webEventService.getUrl(), DEFAULT_URL + "myDetails/777");
        verify(mockWebDriverService, times(2)).getCurrentUrl();
        verify(mockWebDriverService, times(1)).manage();
        verify(mockOptions, times(1)).window();
    }

    @Test
    public void redirectPreviouslyDecoratedDiffUrlTest() {
        when(mockWebDriverService.getCurrentUrl()).thenReturn(DEFAULT_URL + "/myDetails/1234");
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("/foobar");

        Assert.assertEquals(webEventService.getUrl(), DEFAULT_URL + "foobar");
        verify(mockWebDriverService, times(2)).getCurrentUrl();
        verify(mockWebDriverService, times(1)).manage();
        verify(mockOptions, times(1)).window();
    }

    @Test
    public void redirectToLocalhostFullAndMoreUrlTest() {

        String expected = "https://yourwebsite.com/myDetails/1234";
        String baseUrl = "https://yourwebsite.com";
        String redirectUrl = "/myDetails/1234";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectPreviouslyDecoratedLocalhostUrlTest() {

        String expected = "https://yourwebsite.com/myDetails/777";
        String baseUrl = "https://yourwebsite.com/myDetails/1234";
        String redirectUrl = "/myDetails/777";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectUrlSimpleRedirectTest() {

        String expected = "https://yourwebsite.com/myDetails/777";
        String baseUrl = "https://yourwebsite.com";
        String redirectUrl = "/myDetails/777";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectFilterBuilderUrlTest() {

        String expected = "https://mywebsite.com/view/loadFilter.faces?id=23414513&jira=false";
        String baseUrl = "https://mywebsite.com/?jira=false";
        String redirectUrl = "/view/loadFilter.faces?id=23414513&jira=false";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectUrlQueryStringWithExistingParameterTest() {

        String expected = "https://yourwebsite.com/view/loadFilter.faces?id=792568496&jira=false";
        String baseUrl = "https://yourwebsite.com/view/loadFilter.faces?id=792568496";
        String redirectUrl = "jira=false";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectUrlQueryStringWithExistingParameterMultipleParametersTest() {

        String expected = "https://yourwebsite.com/view/loadFilter.faces?id=792568496&jira=false&logging=off";
        String baseUrl = "https://yourwebsite.com/view/loadFilter.faces?id=792568496";
        String redirectUrl = "jira=false&logging=off";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectUrlQueryStringWithoutExistingParameterTest() {

        String expected = "https://yourwebsite.com?jira=false";
        String baseUrl = "https://yourwebsite.com";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl("jira=false");

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectUrlQueryStringWithoutExistingParameterMultipleParametersTest() {

        String expected = "https://yourwebsite.com?jira=false&logging=off";
        String baseUrl = "https://yourwebsite.com";
        String redirectUrl = "jira=false&logging=off";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void redirectUrlBadParametersCompleteUrlRedirectTest() {

        String expected = "https://mywebsite.com";
        String baseUrl = "https://yourwebsite.com";
        String redirectUrl = "https://mywebsite.com";

        when(mockWebDriverService.getCurrentUrl()).thenReturn(baseUrl);
        when(mockWebDriverService.manage()).thenReturn(mockOptions);
        when(mockOptions.window()).thenReturn(mockWindow);
        webEventService.setMicroserviceWebDriver(mockWebDriverService);
        webEventService.redirectToUrl(redirectUrl);

        Assert.assertEquals(webEventService.getUrl(), expected);

    }

    @Test
    public void tableTextContainsTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextContains("datatable", "Row Information", 1, 0, true);

    }

    @Test(expected = AssertionError.class)
    public void tableTextContainsShouldFailTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextContains("datatable", "Setellite", 1, 0, true);

    }

    @Test
    public void tableTextNotContainsTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextContains("datatable", "Setellite", 1, 0, false);

    }

    @Test(expected = AssertionError.class)
    public void tableTextNotContainsShouldFailTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextContains("datatable", "Row Information", 1, 0, false);

    }

    @Test
    public void tableTextEqualsTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextEquals("datatable", CELL_VALUE, 1, 0, true);

    }

    @Test
    public void tableTextNotEqualsTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextEquals("datatable", CELL_VALUE + "foo", 1, 0, false);

    }

    @Test(expected = AssertionError.class)
    public void tableTextEqualsShouldFailTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextEquals("datatable", CELL_VALUE + "foo", 1, 0, true);

    }

    @Test(expected = AssertionError.class)
    public void tableTextNotEqualsShouldFailTest() {

        final String CELL_VALUE = "My Table Row Information Here";

        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement firstMockCell = mock(WebElement.class);

        when(headingMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        when(firstMockCell.getTagName()).thenReturn(WebElementType.TD.type);
        when(firstMockCell.getText()).thenReturn(CELL_VALUE);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(headingMockRow);
        listOfRows.add(firstMockRow);

        List<WebElement> listOfCells = new ArrayList<>();
        listOfCells.add(firstMockCell);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        when(firstMockRow.findElements(By.tagName(WebElementType.TD.type))).thenReturn(listOfCells);

        webEventService.tableTextEquals("datatable", CELL_VALUE, 1, 0, false);

    }


    @Test
    public void gridRowCountMatchingCountTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement secondHeadingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement secondMockRow = mock(WebElement.class);
        WebElement thirdMockRow = mock(WebElement.class);
        WebElement fourthMockRow = mock(WebElement.class);
        WebElement fifthMockRow = mock(WebElement.class);
        when(headingMockRow.getTagName()).thenReturn(WebElementType.TH.type);
        when(headingMockRow.isDisplayed()).thenReturn(true);
        when(secondHeadingMockRow.getTagName()).thenReturn(WebElementType.TH.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(secondMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(thirdMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(fourthMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(fifthMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        List<WebElement> listOfHeadings = new ArrayList<>();
        listOfHeadings.add(headingMockRow);
        listOfHeadings.add(secondHeadingMockRow);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(firstMockRow);
        listOfRows.add(secondMockRow);
        listOfRows.add(thirdMockRow);
        listOfRows.add(fourthMockRow);
        listOfRows.add(fifthMockRow);

        when(getMockWebElement().findElement(By.tagName(WebElementType.TH.type))).thenReturn(headingMockRow);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        webEventService.tableRowCount("datatable", 4);

    }

    @Test(expected = AssertionError.class)
    public void gridRowCountFindTableTest() {
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());
        webEventService.tableRowCount("datatable", 0);
    }

    @Test(expected = AssertionError.class)
    public void gridRowCountUnableToFindTableTest() {
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable-foobar']")).thenReturn(getMockWebElement());
        webEventService.tableRowCount("datatable", 0);
    }

    @Test
    public void gridRowCountNotMatchingCountTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement secondMockRow = mock(WebElement.class);
        WebElement thirdMockRow = mock(WebElement.class);
        WebElement fourthMockRow = mock(WebElement.class);
        when(headingMockRow.getTagName()).thenReturn(WebElementType.TH.type);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(secondMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(thirdMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(fourthMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        List<WebElement> listOfHeadings = new ArrayList<>();
        listOfHeadings.add(headingMockRow);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(firstMockRow);
        listOfRows.add(secondMockRow);
        listOfRows.add(thirdMockRow);
        listOfRows.add(fourthMockRow);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(listOfRows);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        webEventService.tableRowCount("datatable", 4);

    }

    @Test
    public void gridRowCountNoTableHeaderTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(TABLE_HTML);
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='datatable']")).thenReturn(getMockWebElement());

        WebElement headingMockRow = mock(WebElement.class);
        WebElement firstMockRow = mock(WebElement.class);
        WebElement secondMockRow = mock(WebElement.class);
        WebElement thirdMockRow = mock(WebElement.class);
        WebElement fourthMockRow = mock(WebElement.class);
        when(firstMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(secondMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(thirdMockRow.getTagName()).thenReturn(WebElementType.TR.type);
        when(fourthMockRow.getTagName()).thenReturn(WebElementType.TR.type);

        List<WebElement> listOfHeadings = new ArrayList<>();
        listOfHeadings.add(headingMockRow);

        List<WebElement> listOfRows = new ArrayList<>();
        listOfRows.add(firstMockRow);
        listOfRows.add(secondMockRow);
        listOfRows.add(thirdMockRow);
        listOfRows.add(fourthMockRow);

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(listOfRows);
        webEventService.tableRowCount("datatable", 4);

    }

    @Test
    public void containingWordTest() {
        String xpath = webEventService.containingWord("class", "foo");
        Assert.assertEquals(xpath, "contains(concat(' ',normalize-space(@class),' '),' foo ')");
    }

    @Test
    public void waitForBrowserToLoadTest() {
        webEventService.waitForBrowserToLoad();
    }

    @Test(expected = AssertionError.class)
    public void waitForBrowserToLoadBrowserNeverLoadsTest() {
        when(mockWebDriverService.executeScript(JavascriptConstants.IS_OPEN_HTTPS)).thenReturn(7L);
        webEventService.waitForBrowserToLoad();
    }

    @Test(expected = AssertionError.class)
    public void loadSavedCookiesFromFileTest() {
        List cookies = webEventService.loadSavedCookiesFromFile();
        Assert.assertNull(cookies);
    }

    @Test
    public void waitForTextToDisappearDisappearTest() {
        webEventService.waitForTextToDisappear("Save Changesx");
    }

    @Test
    public void waitForTextToDisplayTest() {
        webEventService.waitForTextToDisplay("Save Changes");
    }

    @Test
    public void findWebElementXpathNoElementTest() {
        WebElement webElement = webEventService.findWebElement(XPATH_LOCATOR);
        Assert.assertNull(webElement);
    }

    @Test
    public void findWebElementXpathTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);

        when(mockWebDriverService.findElementByXPath(XPATH_LOCATOR)).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findWebElement(XPATH_LOCATOR);
        Assert.assertEquals(webElement.getTagName(), WebElementType.DIV.type);
        Assert.assertEquals(webElement.getAttribute("id"), "777");
    }

    @Test
    public void findWebElementBadParsingTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath("777")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findWebElement("777");
        Assert.assertNull(webElement);
    }

    @Test
    public void findWebElementPartialHtmlTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath("777")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findWebElement("777");
        Assert.assertNull(webElement);
    }

    @Test
    public void findWebElementCustomConstructedPathTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findWebElement("777");
        Assert.assertEquals(webElement.getTagName(), WebElementType.DIV.type);
        Assert.assertEquals(webElement.getAttribute("id"), "777");
    }

    @Test
    public void findWebElementCustomConstructedPathElementNotFoundTest() {
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='7778']")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findWebElement("777");
        Assert.assertNull(webElement);
    }

    @Test
    public void findWebElementBadHtmlTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(PARTIAL_HTML);
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='7778']")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findWebElement("777");
        Assert.assertNull(webElement);
    }

    @Test
    public void findWebElementSeleniumParserTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(RAW_HTML);
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(ELEMENT_XPATHS.get(2))).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.seleniumFindElementByXPath(ELEMENT_XPATHS);
        Assert.assertNotNull(webElement);
    }

    @Test
    public void findWebElementSeleniumParserUnableToFindTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(RAW_HTML);
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='7778']")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.seleniumFindElementByXPath(ELEMENT_XPATHS);
        Assert.assertNull(webElement);
    }

    @Test
    public void findWebElementCustomParserTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(RAW_HTML);
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(ELEMENT_XPATHS.get(2))).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findElementByXPath(ELEMENT_XPATHS);
        Assert.assertNotNull(webElement);
    }

    @Test(expected = AssertionError.class)
    public void hoverContainsTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        Actions mockActions = mock(Actions.class);
        Action mockAction = mock(Action.class);
        when(mockActions.moveToElement(specialMockWebElement)).thenReturn(mockActions);
        when(mockActions.build()).thenReturn(mockAction);
        doNothing().when(mockActions).perform();

        webEventService.hoverContains("777", "Satellite Navigation", true);
    }

    @Test
    public void findWebElementVTDParserFallBackToSeleniumParserTest() {
        when(mockWebDriverService.getPageSource()).thenReturn(RAW_HTML);
        createMockElement("777", WebElementAttribute.ID, WebElementType.DIV, true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='7778']")).thenReturn(getMockWebElement());

        WebElement webElement = webEventService.findElementByXPath(ELEMENT_XPATHS);
        Assert.assertNull(webElement);
    }

    @Test
    public void getTextByTextTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        String elementText = webEventService.getText("777");
        Assert.assertEquals(elementText, "Save Button");
    }

    @Test
    public void getTextByTextForElementThatDoesNotExistTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(null);

        String elementText = webEventService.getText("777");
        Assert.assertEquals(elementText, "");
    }

    @Test
    public void getTextByValueAttributeTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        String result = webEventService.getText("777");
        Assert.assertEquals(result, "Save Button");
    }

    @Test
    public void getAttributeTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        String result = webEventService.getAttribute("777", WebElementAttribute.HREF);
        Assert.assertEquals(result, DEFAULT_URL);
    }

    @Test
    public void elementCssValueContainsTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyContains("777", CssProperty.LEFT, "68px;", true);
    }

    @Test(expected = AssertionError.class)
    public void elementCssValueContainsShouldFailTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyContains("777", CssProperty.LEFT, "69px;", true);
    }

    @Test
    public void elementCssValueNotContainsTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyContains("777", CssProperty.LEFT, "69px;", false);
    }

    @Test(expected = AssertionError.class)
    public void elementCssValueNotContainsShouldFailTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyContains("777", CssProperty.LEFT, "68px;", false);
    }

    @Test
    public void getTextCssValueTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyEquals("777", CssProperty.LEFT, "left: 68px;", true);
    }

    @Test
    public void getTextCssValueNotEqualsTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyEquals("777", CssProperty.LEFT, "left: 69px;", false);
    }

    @Test(expected = AssertionError.class)
    public void getTextCssValueNotEqualsWithFailureTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyEquals("777", CssProperty.LEFT, "left: 69px;", true);
    }

    @Test(expected = AssertionError.class)
    public void getTextCssValueEqualsWithFailureTest() {
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.HREF.attribute)).thenReturn(DEFAULT_URL);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.BUTTON.type);
        when(specialMockWebElement.getCssValue("left")).thenReturn("left: 68px;");
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementCssPropertyEquals("777", CssProperty.LEFT, "left: 68px;", false);
    }

    @Test
    public void elementTextExistsShouldFindTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("Save Changes", true);
    }

    @Test(expected = AssertionError.class)
    public void elementTextExistsUnableToFindTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("foobar", true);
    }

    @Test(expected = AssertionError.class)
    public void elementTextExistsShouldNotFindExpectedTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("Save Changes", false);
    }

    @Test
    public void elementTextExistsShouldNotFindTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("foobar", false);
    }

    @Test
    public void pageTitleEqualsTest() {
        webEventService.pageTitleEquals("Unit Testing At It's Best");
    }

    @Test(expected = AssertionError.class)
    public void pageTitleEqualsUnexpectedTest() {
        webEventService.pageTitleEquals("Unexpected Page Title");
    }

    @Test
    public void executeJavascriptTest() {
        webEventService.executeJavascript("alert('hi');");
    }

    @Test(expected = AssertionError.class)
    public void elementTextExistsExpectToNotFindTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("777", "Save Changes", true);
    }

    @Test
    public void elementTextExistsExpectToFindNonCheckboxTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("777", "Save Button", true);
    }

    @Test
    public void elementTextExistsExpectToNotFindNonCheckboxTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("777", "Save Changes", false);
    }

    @Test
    public void webElementBlurByElementIdTest() {
        when(mockWebDriverService.executeScript("document.getElementById('Save Button').blur();")).thenReturn(specialMockWebElement);
        webEventService.elementBlur("Save Button");
    }

    @Test
    public void webElementBlurByElementXPathTest() {
        when(mockWebDriverService.executeScript("(document.evaluate('.//*[@id='777']', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;).blur();")).thenReturn(specialMockWebElement);
        webEventService.elementBlur(".//*[@id='777']");
    }

    @Test(expected = AssertionError.class)
    public void elementTextExistsExpectToFindAndFailNonCheckboxTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("777", "Save Button", false);
    }


    @Test(expected = AssertionError.class)
    public void elementTextExistsFindCheckboxTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextExists("777", WebElementAttribute.CHECKED.attribute, true);
    }

    @Test
    public void elementAttributeSelectElementTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        String dropDownValue = "My Drop Down Value";
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn(dropDownValue);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.SELECT.type);

        WebElement mockSelectedOption = mock(WebElement.class);
        when(mockSelectedOption.getText()).thenReturn(dropDownValue);

        List<WebElement> selectedOptions = new ArrayList<>();
        selectedOptions.add(mockSelectedOption);

        Select mockSelect = mock(Select.class);
        when(mockSelect.getAllSelectedOptions()).thenReturn(selectedOptions);

        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttribute("777", WebElementAttribute.VALUE, dropDownValue, true);
    }

    @Test
    public void elementAttributeCheckboxTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttribute("777", WebElementAttribute.CHECKED, "", false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void elementTextContainsSelectTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.SELECT.type);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Save", true);
    }

    @Test
    public void elementTextContainsTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Save", true);
    }

    @Test
    public void elementTextContainsTextTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("null");
        when(specialMockWebElement.getText()).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Save", true);
    }

    @Test(expected = AssertionError.class)
    public void elementTextNotContainsTextTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("null");
        when(specialMockWebElement.getText()).thenReturn("GO");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Save", true);
    }

    @Test
    public void elementTextNotContainsTextShouldPassTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("null");
        when(specialMockWebElement.getText()).thenReturn("GO");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Save", false);
    }

    @Test(expected = AssertionError.class)
    public void elementTextContainsShouldContainAndFailTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Save", false);
    }

    @Test
    public void elementTextContainsShouldNotContainTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Saving", false);
    }

    @Test(expected = AssertionError.class)
    public void elementTextContainsShouldNotContainAndFailTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementTextContains("777", "Button", false);
    }

    @Test
    public void isVisibleTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        boolean result = webEventService.isVisible("777");
        Assert.assertTrue(result);
    }

    @Test
    public void isVisibleNotTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        boolean result = webEventService.isVisible("778");
        Assert.assertFalse(result);
    }

    @Test
    public void isVisibleShouldNotBeVisibleTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(false);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        boolean result = webEventService.isVisible("777");
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void isVisibleShouldFailTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(false);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        boolean result = webEventService.isVisible("778");
        Assert.assertTrue(result);
    }

    @Test
    public void elementVisibleTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementVisible("777");
    }

    @Test
    public void elementVisibleNotVisibleTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(false);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementNotVisible("777");
    }

    @Test(expected = AssertionError.class)
    public void elementVisibleNotVisibleShouldFailTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementNotVisible("777");
    }

    @Test
    public void elementAttributeContainsTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttributeContains("777", WebElementAttribute.VALUE, "Button", true);
    }

    @Test
    public void elementAttributeContainsShouldNotFindTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttributeContains("777", WebElementAttribute.VALUE, "Buttons", false);
    }

    @Test(expected = AssertionError.class)
    public void elementAttributeContainsShouldNotFindAndFailTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttributeContains("777", WebElementAttribute.VALUE, "Buttons", true);
    }

    @Test(expected = AssertionError.class)
    public void elementAttributeContainsDifferentAttributeTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttributeContains("777", WebElementAttribute.TEXT, "Save Button", true);
    }

    @Test(expected = AssertionError.class)
    public void elementAttributeContainsDifferentTextTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementAttributeContains("777", WebElementAttribute.VALUE, "Saves Button", true);
    }

    @Test
    public void elementExistsTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementExists("777");
    }

    @Test
    public void elementExistsShouldNotFindElementTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementNotExists("7778");
    }

    @Test(expected = AssertionError.class)
    public void elementExistsShouldNotFindElementShouldFailTest() {
        when(specialMockWebElement.isDisplayed()).thenReturn(true);
        when(specialMockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(specialMockWebElement.getAttribute(WebElementAttribute.VALUE.attribute)).thenReturn("Save Button");
        when(specialMockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        when(mockWebDriverService.findElementByXPath(".//*[@id='777']")).thenReturn(specialMockWebElement);

        webEventService.elementNotExists("777");
    }

    @Test
    public void elementContainsTextTest() {
        boolean result = webEventService.elementContainsText(true, "foobar is the test case", "case");
        Assert.assertTrue(result);
    }

    @Test
    public void elementContainsTextNotContainsTest() {
        boolean result = webEventService.elementContainsText(true, "foobar is the test case", "patent");
        Assert.assertFalse(result);
    }

    @Test
    public void elementContainsTextExpectedToNotContainTest() {
        boolean result = webEventService.elementContainsText(false, "foobar is the test case", "abc");
        Assert.assertFalse(result);
    }

    @Test
    public void elementContainsTextExpectedToContainTest() {
        boolean result = webEventService.elementContainsText(false, "foobar is the test case", "foobar");
        Assert.assertTrue(result);
    }

    @Test
    public void elementEqualsTextTest() {
        boolean result = webEventService.elementEqualsText(true, "foobar here", "foobar here");
        Assert.assertTrue(result);
    }

    @Test
    public void elementEqualsTextNotExistsTest() {
        boolean result = webEventService.elementEqualsText(false, "foobar here", "foobar here");
        Assert.assertTrue(result);
    }

//    @Test
//    public void testAuthenticateSiteMinder(){
//
//        Authenticator mockAuthenticator = mock(Authenticator.class);
//        List<Cookie> cookies = new ArrayList<>();
//
//        when(mockAuthenticator.getAuthenticationCookies(DEFAULT_URL, Credentials.DEFAULT_PAD_USER.username, Credentials.DEFAULT_PAD_USER.password)).thenReturn(cookies);
//        webEventService.authenticateSiteMinder(DEFAULT_URL, Credentials.DEFAULT_PAD_USER);
//    }

}
