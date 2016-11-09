package com.pwc.core.framework.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.JavascriptConstants;
import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.driver.MicroserviceWebDriver;
import com.pwc.core.framework.siteminder.Authenticator;
import com.pwc.core.framework.siteminder.BasicClientCookieMixIn;
import com.pwc.core.framework.util.DateUtils;
import com.pwc.core.framework.util.DebuggingUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.StringReader;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pwc.assertion.AssertService.*;
import static com.pwc.logging.LoggerService.LOG;


@Component
public class WebEventService extends WebEventController {

    private long timeOutInSeconds;
    private long sleepInMillis;
    private long pageTimeoutInSeconds;

    public static final long CACHED_COOKIE_FILE_TIMEOUT = 1000 * 60 * 30;
    private static final String REGEX_XPATH_FINDER = ".*[\\[@'].*";

    private String url;
    private String siteMinderRedirectUrl;
    private Credentials credentials;
    private MicroserviceWebDriver microserviceWebDriver;
    private CloseableHttpClient customHttpClient;
    private File uniqueCookiesFile;
    private BasicCookieStore cookieStore;

    public WebEventService() {
    }

    public WebEventService(MicroserviceWebDriver driver) {
        this.microserviceWebDriver = driver;
    }

    /**
     * Authenticate using SiteMinder authentication
     *
     * @param targetUrl     url to authenticate with
     * @param credentials   Credentials to use for authentication
     * @param siteMinderUrl Siteminder URL to authenticate with
     * @param headless      is browser headless flag
     */
    public void authenticateSiteMinder(final String targetUrl, final Credentials credentials, final String siteMinderUrl, final boolean headless) {

        setUrl(targetUrl);
        setSiteMinderRedirectUrl(siteMinderUrl);
        this.credentials = credentials;
        List<Cookie> cookies;
        //cookies = loadSavedCookiesFromFile();
        //if (cookies == null) {
        cookies = generateUniqueCookiesFile();
        //}

        if (headless) {
            addCookiesToHttpClient(cookies);
        } else {
            addCookiesToDriver(cookies, siteMinderUrl);
        }

    }

    /**
     * Return saved cookie list from local file
     *
     * @return list of Cookies
     */
    protected List<Cookie> loadSavedCookiesFromFile() {

        String filename = getCookieFilename();
        List<Cookie> cookies = null;

        try {
            String env = getActiveEnv();
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            FileFilter jsonFileFilter = new WildcardFileFilter(String.format("%s.%s*%s*.%s", credentials.getUsername(), env, "cookies", "json"));
            File[] existingFiles = tempDir.listFiles(jsonFileFilter);

            if (existingFiles.length == 0) {
                return generateUniqueCookiesFile();
            } else {
                LOG(true, "Using existing cookies file='%s'", existingFiles[0]);
                long lastModifiedDatetime = existingFiles[0].lastModified();
                long fileExpiredDatetime = new Date().getTime() - CACHED_COOKIE_FILE_TIMEOUT;
                if (existingFiles[0].exists() && lastModifiedDatetime > fileExpiredDatetime) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.addMixIn(BasicClientCookie.class, BasicClientCookieMixIn.class);
                    cookies = mapper.readValue(existingFiles[0], new TypeReference<List<BasicClientCookie>>() {
                    });
                }
            }

        } catch (Exception e) {
            Assert.fail("Failed to load '" + filename + "'", e);
        }

        return cookies;

    }

    /**
     * Create a unique cookies JSON file for the script currently executing
     *
     * @return <code>List</code> of cookies for the current URL, Username, and Password
     */
    private List<Cookie> generateUniqueCookiesFile() {

        List<Cookie> cookies = null;
        String uniqueCookiesFile = getCookieFilename();
        try {
            Authenticator authenticator = Authenticator.getInstance();
            cookies = authenticator.getAuthenticationCookies(getUrl(), credentials.getUsername(), credentials.getPassword());
            cookies = scrubCookiesForDomain(cookies);
//            if (cookies != null) {
//                LOG(true, "Generating unique cookies file='%s'", uniqueCookiesFile);
//                this.uniqueCookiesFile = new File(uniqueCookiesFile);
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.writeValue(this.uniqueCookiesFile, cookies);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to generate valid cookies for url='" + getUrl() + "'", e.getCause());
        }
        return cookies;

    }

    /**
     * Remove any leftover cookies that may have polluted my cookie list. Then
     * safe new cookies to file
     *
     * @param cookies active cookie <code>List</code>
     * @return cleaned cookie list for the currently running domain only
     */
    private List<Cookie> scrubCookiesForDomain(List<Cookie> cookies) {

        if (cookies != null) {
            for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext(); ) {
                Cookie cookie = iterator.next();
                if (!url.contains(cookie.getDomain())) {
                    iterator.remove();
                    LOG("Cookie removed for '" + cookie.getDomain() + "' instead of '" + url + "'");
                }
            }
        }
        return cookies;

    }

    /**
     * Add authentication cookies to WebDriver
     *
     * @param cookies list of cookies to add
     */
    private void addCookiesToDriver(final List<Cookie> cookies, final String siteMinderUrl) {

        try {
            if (cookies.size() > 0) {
                constructUrlWithFullHostFromGivenUrl(getUrl());
                microserviceWebDriver.get(StringUtils.appendIfMissing(getUrl(), "/", "/") + siteMinderUrl);
                for (Cookie c : cookies) {
                    org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie(c.getName(), c.getValue(),
                            c.getDomain(), c.getPath(), c.getExpiryDate());
                    microserviceWebDriver.manage().addCookie(cookie);
                }
            }
        } catch (InvalidCookieDomainException e) {
            Assert.fail("Unable to set cookie for current domain.  Check for a dirty cookie list");
        }

    }

    /**
     * Add authentication cookies to CloseableHttpClient
     *
     * @param cookies list of cookies to add
     */
    protected void addCookiesToHttpClient(final List<Cookie> cookies) {

        if (cookies.size() > 0) {

            CloseableHttpResponse loginResponse = null;

            try {

                if (StringUtils.containsIgnoreCase(getUrl(), "jira.qualcomm.com")) {
                    setUrl("https://jira.qualcomm.com/jira/rest/gadget/1.0/statTypes");
                }

                cookieStore = new BasicCookieStore();
                for (Cookie cookie : cookies) {
                    cookieStore.addCookie(cookie);
                }

                SSLContext sslcontext = buildSSLContext();
                SSLConnectionSocketFactory sslConnectionSocketFactory = buildSSLConnectionSocketFactory(sslcontext);
                customHttpClient = buildCookieBasedHttpClient(cookieStore, sslConnectionSocketFactory);

                HttpUriRequest loginPost = RequestBuilder
                        .post()
                        .setUri(getUrl())
                        .addParameter("USER", credentials.getUsername())
                        .addParameter("PASSWORD", credentials.getPassword()).build();

                LOG(true, "Executing request '%s' %s", loginPost.getRequestLine(), "\n");

                loginResponse = customHttpClient.execute(loginPost);

                HttpEntity loginResponseEntity = loginResponse.getEntity();
                EntityUtils.consume(loginResponseEntity);

            } catch (Exception e) {
                Assert.fail("Unable to set cookie for current domain.  Check for a dirty cookie list");
            } finally {
                closeHttpResponse(loginResponse);
            }
        }

    }

    private static SSLContext buildSSLContext() throws Exception {
        SSLContext sslcontext = SSLContexts.custom()
                .setSecureRandom(new SecureRandom())
                .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        return sslcontext;
    }

    private static SSLConnectionSocketFactory buildSSLConnectionSocketFactory(SSLContext sslcontext) {
        SSLConnectionSocketFactory sslsf =
                new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return sslsf;
    }

    private static CloseableHttpClient buildCookieBasedHttpClient(BasicCookieStore cookieStore, SSLConnectionSocketFactory sslsf) {
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        return httpclient;
    }

    private void closeHttpResponse(final CloseableHttpResponse response) {
        try {
            LOG("Closing HTTP response");
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct the cookie file name for PAD product
     *
     * @return well-formed cookie name
     */
    private String getCookieFilename() {
        String env = getActiveEnv();
        StringBuilder fileName = new StringBuilder();
        fileName.append(System.getProperty("java.io.tmpdir"));
        if (!StringUtils.endsWith(System.getProperty("java.io.tmpdir"), "/")) {
            fileName.append(File.separator);
        }
        fileName.append(credentials.getUsername());
        fileName.append(".");
        fileName.append(env);
        fileName.append(".");
        fileName.append(new Date().getTime());
        fileName.append(".cookies.json");

        return fileName.toString();
    }

    /**
     * Get active URL from the url being operated on
     *
     * @return environment <code>String</code> value
     */
    private String getActiveEnv() {
        String env;
        if (StringUtils.containsIgnoreCase(url, "stage")) {
            env = "STAGE";
        } else if (StringUtils.containsIgnoreCase(url, "uat")) {
            env = "UAT";
        } else if (StringUtils.containsIgnoreCase(url, "test")) {
            env = "TEST";
        } else if (StringUtils.containsIgnoreCase(url, "dev")) {
            env = "DEV";
        } else if (StringUtils.containsIgnoreCase(url, "localhost")) {
            env = "LOCAL";
        } else {
            env = "PROD";
        }

        String applicationPrefix = StringUtils.substringAfter(url, "//");
        applicationPrefix = StringUtils.substring(applicationPrefix, 0, 5);
        return applicationPrefix + env;
    }

    /**
     * Find WebElement by all possible avenues
     *
     * @param elementIdentifier unique element identifying string
     * @return WebElement to then be used to interact with the AUT
     */
    public WebElement findWebElement(final String elementIdentifier) {

        waitForBrowserToLoad();

        if (elementIdentifier.matches(REGEX_XPATH_FINDER)) {
            try {
                WebElement webElement = this.microserviceWebDriver.findElementByXPath(elementIdentifier);
                if (webElement != null) {
                    return webElement;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } else {

            List<String> elementXPathLocators = new ArrayList<>();
            elementXPathLocators.add(".//*[@id='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@name='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@class='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@href='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@alt='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@type='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@value='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@src='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@points='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@placeholder='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@onclick='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@role='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@title='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[@rel='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[text()='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[*/text()='" + elementIdentifier + "']");
            elementXPathLocators.add(".//*[" + containingWord("class", elementIdentifier) + "]");
            elementXPathLocators.add("//*[contains(@id, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@name, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@class, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@href, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@alt, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@type, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@value, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@src, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@points, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@placeholder, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@onclick, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@role, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@title, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//*[contains(@rel, '" + elementIdentifier + "')]");
            elementXPathLocators.add("//h1[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//h2[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//h3[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.ANCHOR.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.SPAN.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.BUTTON.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.TEXTAREA.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.INPUT.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.FIG_CAPTION.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.LABEL.type + "[contains(.,'" + elementIdentifier + "')]");
            elementXPathLocators.add("//" + WebElementType.DEFINITION_TERM.type + "[contains(.,'" + elementIdentifier + "')]");
            return findElementByXPath(elementXPathLocators);
        }

        return null;
    }

    /**
     * Backup way of getting a <code>WebElement</code> which uses Selenium's parser
     *
     * @param xpathSearchList list of xpaths to search for element with
     * @return WebElement to then be used to interact with the AUT
     */
    public WebElement seleniumFindElementByXPath(List<String> xpathSearchList) {
        try {
            for (String xpathSearch : xpathSearchList) {
                WebElement webElement = null;
                try {
                    webElement = this.microserviceWebDriver.findElementByXPath(xpathSearch);
                } catch (Exception eatIt) {
                    eatIt.getMessage();
                }
                if (webElement != null) {
                    return webElement;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clean parsing problems out of <body></body> HTML that can fail our parsing
     *
     * @param htmlToScrub dirty HTML
     * @return clean HTML
     */
    private String scrubHtml(String htmlToScrub) {
        htmlToScrub = StringUtils.replace(htmlToScrub, "&nbsp;", "");
        htmlToScrub = StringUtils.replace(htmlToScrub, "<br>", "<br></br>");
        return htmlToScrub;
    }

    /**
     * Find WebElement by an XPath from the current page BODY
     *
     * @param xpathSearchList list of xpaths to search for element with
     * @return WebElement to then be used to interact with the AUT
     */
    public WebElement findElementByXPath(List<String> xpathSearchList) {

        try {

            String scrubbedBodyHtml = StringUtils.trim(StringUtils.substringBetween(this.microserviceWebDriver.getPageSource(), "</head>", "</html>"));
            org.jsoup.nodes.Document parsedDocument = Jsoup.parse(scrubbedBodyHtml, "", Parser.xmlParser());
            scrubbedBodyHtml = scrubHtml(parsedDocument.toString());
            parsedDocument = Jsoup.parse(scrubbedBodyHtml, "", Parser.xmlParser());
            String scrubbedParsedDocument = scrubHtml(parsedDocument.toString());

            InputSource pageSource = new InputSource(new StringReader(scrubbedParsedDocument));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            documentBuilder.setErrorHandler(null);
            Document document = documentBuilder.parse(pageSource);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            for (String xpathSearch : xpathSearchList) {
                if ((boolean) xpath.evaluate(xpathSearch, document, XPathConstants.BOOLEAN)) {
                    return this.microserviceWebDriver.findElementByXPath(xpathSearch);
                }
            }

        } catch (Exception e) {
            //LOG(String.format("%sReverting to Selenium Parser due to parsing issue='%s'", StringUtils.repeat("*", 3), e.getMessage()));
            return seleniumFindElementByXPath(xpathSearchList);
        }

        return null;

    }

    /**
     * Verify hover text CONTAINS text when hovering over an element
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedText      expected hover text
     * @param textExists        boolean flag for existing or not
     */
    public void hoverContains(final String elementIdentifier, final String expectedText, final boolean textExists) {
        Actions hoverAction = new Actions(microserviceWebDriver);
        WebElement webElement = findWebElement(elementIdentifier);
        hoverAction.moveToElement(webElement).build().perform();
        if (textExists) {
            waitForDuration(FrameworkConstants.DEFAULT_POLLING_DURATION);
            assertTrue("Verify hoverContains() exists text='%s'%s", pageContainsText(expectedText), expectedText, DebuggingUtils.getDebugInfo(this.microserviceWebDriver));
        } else {
            assertFalse("Verify hoverContains() exists text='%s'%s", pageContainsText(expectedText), expectedText, DebuggingUtils.getDebugInfo(this.microserviceWebDriver));
        }
    }

    /**
     * Verify the number of rows in a table
     *
     * @param elementIdentifier unique element identifying string
     * @param expectedRowCount  expected number of rows
     */
    public void tableRowCount(final String elementIdentifier, final int expectedRowCount) {
        WebElement gridElement = findWebElement(elementIdentifier);
        int actualHeaderRowCount = 0;
        int actualRowCount = 0;
        try {
            actualHeaderRowCount = getTableHeaderCount(gridElement);
            actualRowCount = gridElement.findElements(By.tagName(WebElementType.TR.type)).size() - actualHeaderRowCount;
        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("tableRowCount() Failed for expected row count=%s", expectedRowCount);
        }
        assertEquals("Verify tableRowCount()", actualRowCount, expectedRowCount);
    }

    /**
     * Count the number of table <code>TH</code> elements in a given table WebElement
     *
     * @param gridElement WebElement of type table
     * @return number of table header elements
     */
    private int getTableHeaderCount(WebElement gridElement) {
        try {
            return gridElement.findElement(By.tagName(WebElementType.TH.type)).isDisplayed() ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowIndex          row number of table
     * @param columnIndex       column number of table
     * @param textExists        check for existing or not
     */
    public void tableTextContains(final String elementIdentifier, final String expectedText, final int rowIndex, final int columnIndex, final boolean textExists) {
        try {
            WebElement table = findWebElement(elementIdentifier);
            WebElement row = table.findElements(By.tagName(WebElementType.TR.type)).get(rowIndex);
            WebElement cell = row.findElements(By.tagName(WebElementType.TD.type)).get(columnIndex);
            if (textExists) {
                assertTrue("Verify tableTextContains() contains text='%s'", StringUtils.containsIgnoreCase(StringUtils.trim(cell.getText()), expectedText), expectedText);
            } else {
                assertFalse("Verify tableTextContains() not contains text='%s'", StringUtils.containsIgnoreCase(StringUtils.trim(cell.getText()), expectedText), expectedText);
            }
        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("tableTextEquals() Failed for expectedText='%s'", expectedText);
        }
    }

    /**
     * Table search that finds and verifies a cell's text content given the row and column number
     * Important: all index's are ZERO (0) based
     *
     * @param elementIdentifier elementIdentifier unique element identifying string
     * @param expectedText      expected text within table
     * @param rowIndex          row number of table
     * @param columnIndex       column number of table
     * @param textExists        check for existing or not
     */
    public void tableTextEquals(final String elementIdentifier, final String expectedText, final int rowIndex, final int columnIndex, final boolean textExists) {
        try {
            WebElement table = findWebElement(elementIdentifier);
            WebElement row = table.findElements(By.tagName(WebElementType.TR.type)).get(rowIndex);
            WebElement cell = row.findElements(By.tagName(WebElementType.TD.type)).get(columnIndex);
            if (textExists) {
                assertEquals("Verify tableTextEquals() exists text='%s'", StringUtils.trim(cell.getText()), expectedText, expectedText);
            } else {
                assertNotEquals("Verify tableTextEquals() not exists text='%s'", StringUtils.trim(cell.getText()), expectedText, expectedText);
            }
        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("tableTextEquals() Failed for expectedText='%s'", expectedText);
        }
    }

    /**
     * Create a URL for redirection when a user wants to append to a url a full
     * query string starting with a '/'
     * <p>
     * ex: https://foo-bar.mywebsite.com/view/loadFilter.faces?id=23414513&jira=false
     *
     * @param url url snippet to redirect to
     */
    private void constructUrlWithFullHostFromCurrentUrl(String url) {
        Pattern URL_REGEX = Pattern.compile("http.*?://(\\w|\\-|\\.)+(:\\d+)?");
        Matcher m = URL_REGEX.matcher(this.microserviceWebDriver.getCurrentUrl());
        m.find();
        String host = StringUtils.appendIfMissing(m.group(0), "/");
        url = StringUtils.removeStart(url, "/");
        setUrl(host + url);
    }

    /**
     * Create a URL for redirection when a user wants to append to a url a full
     * query string starting with a '/'
     * <p>
     * ex: https://foo-bar.mywebsite.com/view/loadFilter.faces?id=23414513&jira=false
     *
     * @param url url snippet to redirect to
     */
    private void constructUrlWithFullHostFromGivenUrl(String url) {
        Pattern URL_REGEX = Pattern.compile("http.*?://(\\w|\\-|\\.)+(:\\d+)?");
        Matcher m = URL_REGEX.matcher(url);
        m.find();
        String host = StringUtils.appendIfMissing(m.group(0), "/");
        setUrl(host);
    }

    /**
     * Create a URL for redirection when a user wants to append to a url a simple
     * query parameter
     * <p>
     * ex: https://foo-bar.mywebsite.com/?jira=false
     *
     * @param url url snippet to redirect to
     */
    private void constructUrlWithQueryParameter(String url) {
        String currentUrl = this.microserviceWebDriver.getCurrentUrl();
        if (StringUtils.containsIgnoreCase(currentUrl, "data:,")) {
            setUrl(url);
            return;
        }
        StringBuilder newUrl = new StringBuilder(currentUrl);
        if (StringUtils.containsIgnoreCase(currentUrl, "?")) {
            newUrl.append("&");
        } else {
            newUrl.append("?");
        }
        newUrl.append(url);
        setUrl(newUrl.toString());
    }

    /**
     * Navigate directly to a particular URL
     *
     * @param url well-formed web URL or partial URL
     * @return duration took to perform page redirect
     */
    public long redirectToUrl(String url) {
        StopWatch sw = new StopWatch();
        try {
            if (StringUtils.endsWith(microserviceWebDriver.getCurrentUrl(), getSiteMinderRedirectUrl())) {
                setUrl(url);
            } else if (url.startsWith("/")) {
                constructUrlWithFullHostFromCurrentUrl(url);
            } else if (StringUtils.containsAny(url, '=')) {
                constructUrlWithQueryParameter(url);
            } else {
                setUrl(url);
            }

            sw.start();
            microserviceWebDriver.get(getUrl());
            //waitForBrowserToLoad();
            sw.stop();

            if (!StringUtils.equalsIgnoreCase(microserviceWebDriver.getCapabilities().getBrowserName(), "android")) {
                microserviceWebDriver.manage().window().maximize();
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG(true, "Unable to redirect to URL='%s' due to exception='%s'", getUrl(), e.getMessage());
        }

        return sw.getTotalTimeMillis();

    }

    /**
     * Generates a partial xpath expression that matches an element whose specified property
     * contains the given CSS word. So to match &lt;div class='foo bar'&gt; you would say "//div[" +
     * containingWord("class", "foo") + "]".
     *
     * @param attribute name
     * @param word      name
     * @return XPath fragment
     */
    public String containingWord(String attribute, String word) {
        return "contains(concat(' ',normalize-space(@" + attribute + "),' '),' " + word + " ')";
    }

    /**
     * Get the visible (i.e. not hidden by CSS) innerText of this element, including sub-elements,
     * without any leading or trailing whitespace.
     *
     * @param elementIdentifier WebElement to find via xpath or unique identifier
     * @return text value of element
     */
    public String getText(final String elementIdentifier) {
        try {
            WebElement webElement = findWebElement(elementIdentifier);
            if (StringUtils.isNotEmpty(webElement.getText())) {
                return StringUtils.trim(webElement.getText());
            } else {
                return webElement.getAttribute(WebElementAttribute.VALUE.attribute);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get the property of this element, including sub-elements
     *
     * @param elementIdentifier WebElement to find via xpath or unique identifier
     * @param attribute         WebElement's specific property to look for
     * @return text value of element
     */
    public String getAttribute(final String elementIdentifier, final WebElementAttribute attribute) {
        return findWebElement(elementIdentifier).getAttribute(attribute.attribute);
    }

    /**
     * Verify a CSS value Equals for this element based on the given css property
     *
     * @param elementIdentifier     WebElement to find via xpath or unique identifier
     * @param cssProperty           WebElement's CSS property to validate
     * @param expectedAttributeText Expected CSS property value
     * @param expectedToEqual       value should or should not exist flag
     */
    public void elementCssPropertyEquals(final String elementIdentifier, final CssProperty cssProperty, final String expectedAttributeText, final boolean expectedToEqual) {
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement != null && expectedToEqual) {
            assertEquals("Verify elementCssPropertyEquals() for element=%s", webElement.getCssValue(cssProperty.property), expectedAttributeText);
        } else {
            assertNotEquals("Verify elementCssPropertyEquals() for element=%s", webElement.getCssValue(cssProperty.property), expectedAttributeText);
        }
    }

    /**
     * Verify a CSS value contains for this element based on the given css property
     *
     * @param elementIdentifier    WebElement to find via xpath or unique identifier
     * @param cssProperty          WebElement's CSS property to validate
     * @param expectedPropertyText Expected CSS property value
     * @param expectedToContain    value should or should not exist flag
     */
    public void elementCssPropertyContains(final String elementIdentifier, final CssProperty cssProperty, final String expectedPropertyText, final boolean expectedToContain) {
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement != null && expectedToContain) {
            assertTrue("Verify elementCssPropertyContains() for expectedPropertyText='%s'",
                    StringUtils.containsIgnoreCase(webElement.getCssValue(cssProperty.property), expectedPropertyText), expectedPropertyText);
        } else {
            assertFalse("Verify elementCssPropertyContains() for expectedPropertyText='%s'",
                    StringUtils.containsIgnoreCase(webElement.getCssValue(cssProperty.property), expectedPropertyText), expectedPropertyText);
        }
    }

    /**
     * Check if the current page source contains the expectedText anywhere regardless of the
     * case
     *
     * @param expectedText expected text to locate on current page
     * @param textExists   text should or should not exist flag
     */
    public void elementTextExists(final String expectedText, final boolean textExists) {
        if (textExists) {
            assertTrue("Verify elementTextExists() exists text='%s'%s", pageContainsText(expectedText), expectedText, DebuggingUtils.getDebugInfo(this.microserviceWebDriver));
        } else {
            assertFalse("Verify elementTextExists() exists text='%s'%s", pageContainsText(expectedText), expectedText, DebuggingUtils.getDebugInfo(this.microserviceWebDriver));
        }
    }

    /**
     * Check current page is the expected page based on the page title
     *
     * @param expectedPageTitle expected page title
     */
    public void pageTitleEquals(final String expectedPageTitle) {
        assertEquals("Verify Page Title", this.microserviceWebDriver.getTitle(), expectedPageTitle);
    }

    /**
     * Execute Javascript via Selenium interface
     *
     * @param javaScript js String to execute
     * @return resulting object
     */
    public Object executeJavascript(String javaScript) {
        try {
            return this.microserviceWebDriver.executeScript(javaScript);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * Exeucte .blur() on a given element via xPath or by ID
     *
     * @param elementIdentifier element identifier (xPath or explicite Element ID)
     */
    public void elementBlur(final String elementIdentifier) {
        if (elementIdentifier.matches(REGEX_XPATH_FINDER)) {
            executeJavascript(String.format(JavascriptConstants.BLUR_ELEMENT_BY_XPATH, elementIdentifier));
        } else {
            executeJavascript(String.format(JavascriptConstants.BLUR_ELEMENT_BY_ID, elementIdentifier));
        }
    }

    /**
     * Check if the current page contains an WebElement with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param expectedText      WebElements text value
     * @param textExists        text should or should not exist flag
     */
    public void elementTextExists(final String elementIdentifier, final String expectedText, final boolean textExists) {
        elementAttribute(elementIdentifier, WebElementAttribute.VALUE, expectedText, textExists);
    }

    /**
     * Check if the current page contains an element with the expectedText displayed in it
     *
     * @param elementIdentifier WebElement to find
     * @param expectedText      WebElements text value
     * @param textExists        text should or should not exist flag
     */
    public void elementTextContains(final String elementIdentifier, final String expectedText, final boolean textExists) {
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement != null) {
            String actualText = null;

            if (StringUtils.equalsIgnoreCase(webElement.getTagName(), WebElementType.SELECT.type)) {
                Select dropDownList = new Select(webElement);
                List<WebElement> selectedOptions = dropDownList.getAllSelectedOptions();
                actualText = StringUtils.trim(selectedOptions.get(0).getText());
            } else {
                if (StringUtils.equalsIgnoreCase(webElement.getAttribute(WebElementAttribute.VALUE.attribute), "null") || (!StringUtils.isEmpty(StringUtils.trim(webElement.getText())))) {
                    actualText = StringUtils.trim(webElement.getText());
                } else if (!StringUtils.isEmpty(webElement.getAttribute(WebElementAttribute.VALUE.attribute))) {
                    actualText = webElement.getAttribute(WebElementAttribute.VALUE.attribute);
                }
            }

            if (textExists) {
                assertTrue("Verify elementTextContains() for element=%s", elementContainsText(true, actualText, expectedText), elementIdentifier);
            } else {
                assertFalse("Verify elementTextContains() for element=%s", elementContainsText(false, actualText, expectedText), elementIdentifier);
            }
        } else {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("elementTextContains() Failed for element=%s", elementIdentifier);
        }
    }

    /**
     * Check if the current page contains a visible element
     *
     * @param elementIdentifier WebElement to find
     * @return visible/not visible flag
     */
    public boolean isVisible(final String elementIdentifier) {
        boolean visible;
        try {
            WebElement element = findWebElement(elementIdentifier);
            if (element != null) {
                visible = element.isDisplayed();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return visible;
    }

    /**
     * Check if the current page contains an element (exists or not).  This differs from visible checking.
     *
     * @param elementIdentifier WebElement to find
     * @return exists/not exists flag
     */
    public boolean exists(final String elementIdentifier) {
        try {
            WebElement element = findWebElement(elementIdentifier);
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if the current page contains a visible element
     *
     * @param elementIdentifier WebElement to find
     */
    public void elementVisible(final String elementIdentifier) {
        waitForElementToDisplay(elementIdentifier);
        if ((isVisible(elementIdentifier))) {
            assertPass("elementVisible() Passed for element=%s", elementIdentifier);
        } else {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("elementVisible() Failed for element=%s", elementIdentifier);
        }
    }

    /**
     * Check if the current page does not contain a visible element
     *
     * @param elementIdentifier WebElement to find
     */
    public void elementNotVisible(final String elementIdentifier) {
        if (!isVisible(elementIdentifier)) {
            assertPass("elementNotVisible() Passed for element=%s", elementIdentifier);
        } else {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("elementNotVisible() Failed for element=%s", elementIdentifier);
        }
    }

    /**
     * Check if the current page contains an element
     *
     * @param elementIdentifier     WebElement to find
     * @param attribute             element property to validate
     * @param expectedAttributeText expected property textual value
     * @param expectedToExist       expected flag to decide if element should exist or not
     */
    public void elementAttribute(final String elementIdentifier, final WebElementAttribute attribute, final String expectedAttributeText, final boolean expectedToExist) {
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement != null && expectedToExist) {
            if (attribute.equals(WebElementAttribute.CHECKED.attribute)) {
                assertTrue("Verify elementAttribute() for element=%s", elementEqualsText(true, webElement.getAttribute(attribute.attribute), expectedAttributeText), elementIdentifier);
            } else {
                if (expectedAttributeText.isEmpty()) {
                    assertTrue("Verify elementAttribute() for element=%s", elementEqualsText(true, webElement.getAttribute(attribute.attribute), expectedAttributeText), elementIdentifier);
                } else {

                    if (StringUtils.equalsIgnoreCase(webElement.getTagName(), WebElementType.SELECT.type)) {
                        Select dropDownList = new Select(webElement);
                        List<WebElement> selectedOptions = dropDownList.getAllSelectedOptions();
                        for (WebElement selectedOption : selectedOptions) {
                            assertTrue("Verify elementAttribute() for element=%s", elementEqualsText(true, selectedOption.getText(), expectedAttributeText));
                        }
                    } else {
                        assertTrue("Verify elementAttribute() for element=%s", elementEqualsText(true, webElement.getAttribute(attribute.attribute), expectedAttributeText) ||
                                elementEqualsText(true, StringUtils.trim(webElement.getText()), expectedAttributeText), elementIdentifier);
                    }

                }
            }

        } else if (webElement == null && expectedToExist) {
            assertFail("Verify elementAttribute() for element=%s", elementIdentifier);
        } else if (webElement != null) {
            assertFalse("Verify elementAttribute() for element=%s", elementEqualsText(false, webElement.getAttribute(attribute.attribute), expectedAttributeText) ||
                    elementEqualsText(false, StringUtils.trim(webElement.getText()), expectedAttributeText), elementIdentifier);
        }
    }

    /**
     * Check if the current WebElement's property contains a text
     *
     * @param elementIdentifier     WebElement to find
     * @param attribute             WebElement attribute to interrogate
     * @param expectedAttributeText expected WebElement attribute
     * @param expectedToExist       expected flag to decide if element should exist or not
     */
    public void elementAttributeContains(final String elementIdentifier, final WebElementAttribute attribute, final String expectedAttributeText, final boolean expectedToExist) {
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement != null && expectedToExist) {
            assertTrue("Verify elementAttributeContains() for element=%s", elementContainsText(true, webElement.getAttribute(attribute.attribute), expectedAttributeText), elementIdentifier);
        } else if (webElement != null) {
            assertFalse("Verify elementAttributeContains() for element=%s", elementContainsText(false, webElement.getAttribute(attribute.attribute), expectedAttributeText), elementIdentifier);
        }
    }

    /**
     * Check if the current page contains an element
     *
     * @param elementIdentifier WebElement to find
     */
    public void elementExists(final String elementIdentifier) {
        waitForElementToExist(elementIdentifier);
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement != null) {
            assertPass("elementExists() Passed for element=%s", elementIdentifier);
        } else {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("elementExists() Failed for element=%s", elementIdentifier);
        }
    }

    /**
     * Check if the current page does not contain an element
     *
     * @param elementIdentifier WebElement to find
     */
    public void elementNotExists(final String elementIdentifier) {
        WebElement webElement = findWebElement(elementIdentifier);
        if (webElement == null) {
            assertPass("elementNotExists() Passed for element=%s", elementIdentifier);
        } else {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("elementNotExists() Failed for element=%s", elementIdentifier);
        }
    }

    /**
     * Verify alert windows and their msg attributes then click the appropriate button
     *
     * @param expectedText      Alert msg to verify
     * @param buttonTextToClick Alert button to select
     * @param expectedToExist   flag check for text to exist or not
     */
    public void alertTextEquals(final String expectedText, final String buttonTextToClick, final boolean expectedToExist) {
        try {

            WebDriverWait wait = new WebDriverWait(microserviceWebDriver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = microserviceWebDriver.switchTo().alert();

            if (expectedToExist) {
                assertEquals("Verify alert msg", StringUtils.trim(alert.getText()), expectedText);
            } else {
                assertNotEquals("Verify alert msg", StringUtils.trim(alert.getText()), expectedText);
            }

            if (StringUtils.containsIgnoreCase(buttonTextToClick, "ok") || StringUtils.containsIgnoreCase(buttonTextToClick, "yes")) {
                alert.accept();
            } else if (!StringUtils.isBlank(buttonTextToClick)) {
                alert.dismiss();
            }

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("alertTextEquals() encountered error=%s", e.getMessage());
        }
    }

    /**
     * Verify alert window contains text and their msg attributes then click the appropriate button
     *
     * @param expectedText      Alert msg to verify it contains
     * @param buttonTextToClick Alert button to select
     * @param expectedToExist   flag check for text to exist or not
     */
    public void alertTextContains(final String expectedText, final String buttonTextToClick, final boolean expectedToExist) {
        try {

            WebDriverWait wait = new WebDriverWait(microserviceWebDriver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = microserviceWebDriver.switchTo().alert();

            String actualText = StringUtils.trim(alert.getText());
            if (expectedToExist) {
                assertContains("Verify alert msg='%s'", actualText, expectedText, expectedText);
            } else {
                assertNotContains("Verify alert msg='%s'", actualText, expectedText, expectedText);
            }

            if (StringUtils.containsIgnoreCase(buttonTextToClick, "ok") || StringUtils.containsIgnoreCase(buttonTextToClick, "yes")) {
                alert.accept();
            } else if (!StringUtils.isBlank(buttonTextToClick)) {
                alert.dismiss();
            }

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            assertFail("alertTextEquals() encountered error=%s", e.getMessage());
        }
    }

    /**
     * SPECIAL usage to take screen shots of failures when text CONTAINS expected
     *
     * @param textExists   text should or should not exist flag
     * @param actualText   actual text to validate
     * @param expectedText expected text to validate
     * @return pass or fail criteria
     */
    public boolean elementContainsText(final boolean textExists, final String actualText, final String expectedText) {
        if (textExists && !StringUtils.containsIgnoreCase(actualText, expectedText)) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            return false;
        } else if (textExists && StringUtils.containsIgnoreCase(actualText, expectedText)) {
            return true;
        } else if (!textExists && StringUtils.containsIgnoreCase(actualText, expectedText)) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            return true;
        } else {
            return false;
        }
    }

    /**
     * SPECIAL usage to take screen shots of failures when text EQUALS expected
     *
     * @param textExists   text should or should not exist flag
     * @param actualText   actual text to validate
     * @param expectedText expected text to validate
     * @return pass or fail criteria
     */
    public boolean elementEqualsText(final boolean textExists, final String actualText, final String expectedText) {
        if (textExists && !StringUtils.equals(actualText, expectedText)) {
            return false;
        } else if (textExists && StringUtils.equals(actualText, expectedText)) {
            return true;
        } else if (!textExists && StringUtils.equals(actualText, expectedText)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Utility method to see if the page contains raw text
     *
     * @param textToFind text to find in the source of the current page
     * @return flag if text exists or not
     */
    private boolean pageContainsText(final String textToFind) {
        return this.microserviceWebDriver.getPageSource().contains(textToFind);
    }

    /**
     * Wait for Element to disappear in the browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to NOT display is going to surly
     * disappear
     *
     * @param elementIdentifier element to find visibly not displayed
     */
    public void waitForElementToDisappear(final String elementIdentifier) {
        try {
            (new WebDriverWait(this.microserviceWebDriver, timeOutInSeconds, sleepInMillis)).until(new ExpectedCondition<Boolean>() {

                boolean isElementNotVisible = true;
                int countDown = (int) timeOutInSeconds;

                public Boolean apply(WebDriver d) {

                    isElementNotVisible = !isVisible(elementIdentifier);
                    if (!isElementNotVisible && countDown > 0) {
                        LOG(true, "Waiting - Element='%s' STILL VISIBLE, Retrying for %s seconds ****", elementIdentifier, countDown--);
                    }
                    return isElementNotVisible;
                }
            });

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Element='%s', didn't disappear in allotted time.", elementIdentifier), e);
        }
    }

    /**
     * Wait for Element to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to exist is going to surly
     * display
     *
     * @param elementIdentifier element to find existing in the DOM
     */
    public void waitForElementToExist(final String elementIdentifier) {
        try {

            (new WebDriverWait(this.microserviceWebDriver, timeOutInSeconds, sleepInMillis)).until(new ExpectedCondition<Boolean>() {
                boolean elementExists = false;
                int countDown = (int) timeOutInSeconds;

                public Boolean apply(WebDriver d) {

                    elementExists = exists(elementIdentifier);
                    if (!elementExists && countDown > 0) {
                        LOG(true, "Waiting - Element='%s' NOT EXISTING, Retrying for %s seconds ****", elementIdentifier, countDown--);
                    }
                    return elementExists;
                }
            });

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Element='%s', didn't exist in allotted time.", elementIdentifier), e);
        }
    }

    /**
     * Wait for Element to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your elementIdentifier to wait for to display is going to surly
     * display
     *
     * @param elementIdentifier element to find visibly
     */
    public void waitForElementToDisplay(final String elementIdentifier) {
        try {

            (new WebDriverWait(this.microserviceWebDriver, timeOutInSeconds, sleepInMillis)).until(new ExpectedCondition<Boolean>() {
                boolean isElementVisible = false;
                int countDown = (int) timeOutInSeconds;

                public Boolean apply(WebDriver d) {

                    isElementVisible = isVisible(elementIdentifier);
                    if (!isElementVisible && countDown > 0) {
                        LOG(true, "Waiting - Element='%s' NOT VISIBLE, Retrying for %s seconds ****", elementIdentifier, countDown--);
                    }
                    return isElementVisible;
                }
            });

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Element='%s', didn't appear in allotted time.", elementIdentifier), e);
        }
    }

    /**
     * Wait for Element to load in browser with expected text contained in the element.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.
     *
     * @param elementIdentifier   element to find visibly
     * @param textToWaitToDisplay text to wait for to display
     */
    public void waitForElementToDisplayContainingText(final String elementIdentifier, final String textToWaitToDisplay) {
        try {

            (new WebDriverWait(this.microserviceWebDriver, timeOutInSeconds, sleepInMillis))
                    .ignoring(StaleElementReferenceException.class)
                    .until(new ExpectedCondition<Boolean>() {
                        boolean isElementVisibleWithText = false;
                        int countDown = (int) timeOutInSeconds;

                        public Boolean apply(WebDriver d) {

                            isElementVisibleWithText = StringUtils.contains(getText(elementIdentifier), textToWaitToDisplay);
                            if (!isElementVisibleWithText && countDown > 0) {
                                LOG(true, "Waiting - Element='%s' WITH TEXT='%s' NOT VISIBLE, Retrying for %s seconds ****", elementIdentifier, textToWaitToDisplay, countDown--);
                            }
                            return isElementVisibleWithText;
                        }
                    });

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Element='%s', didn't appear in allotted time.", elementIdentifier), e);
        }
    }


    /**
     * Wait for Text to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your textToWaitToDisplay to wait for to display is going to surly
     * display
     *
     * @param textToWaitToDisplay text to wait for to display
     */
    public void waitForTextToDisplay(final String textToWaitToDisplay) {
        try {

            (new WebDriverWait(this.microserviceWebDriver, timeOutInSeconds, sleepInMillis)).until(new ExpectedCondition<Boolean>() {
                boolean isElementVisible = false;
                int countDown = (int) timeOutInSeconds;

                public Boolean apply(WebDriver d) {
                    isElementVisible = pageContainsText(textToWaitToDisplay);
                    if (!isElementVisible && countDown > 0) {
                        LOG(true, "Waiting - Text='%s' NOT VISIBLE, Retrying for %s seconds ****", textToWaitToDisplay, countDown--);
                    }
                    return isElementVisible;
                }
            });

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Text='%s', didn't appear in allotted time.", textToWaitToDisplay), e);
        }
    }

    /**
     * Wait for Text to load in browser.  Will timeout after the configurable timeout and throw a failure to fail the test.
     * NOTE: be very careful with this method.  Make sure your textToWaitToDisplay to wait for to display is going to surly
     * display
     *
     * @param textToWaitToDisappear text to wait for to disappear
     */
    public void waitForTextToDisappear(final String textToWaitToDisappear) {
        try {

            (new WebDriverWait(this.microserviceWebDriver, timeOutInSeconds, sleepInMillis)).until(new ExpectedCondition<Boolean>() {
                boolean isElementNotVisible = true;
                int countDown = (int) timeOutInSeconds;

                public Boolean apply(WebDriver d) {
                    isElementNotVisible = !pageContainsText(textToWaitToDisappear);
                    if (!isElementNotVisible && countDown > 0) {
                        LOG(true, "Waiting - Text='%s' STILL VISIBLE, Retrying for %s seconds ****", textToWaitToDisappear, countDown--);
                    }
                    return isElementNotVisible;
                }
            });

        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Text='%s', didn't disappear in allotted time.", textToWaitToDisappear), e);
        }
    }

    /**
     * Wait for all active requests to complete before proceeding.
     * Will timeout after a specified number of seconds and allow test to continue.
     */
    public void waitForBrowserToLoad() {

        try {
            (new WebDriverWait(this.microserviceWebDriver, pageTimeoutInSeconds, sleepInMillis)).until(new ExpectedCondition<Boolean>() {
                boolean noActiveRequests = false;
                int countDown = (int) pageTimeoutInSeconds;

                public Boolean apply(WebDriver d) {

                    noActiveRequests = pendingAjaxRequests();
                    //noActiveRequests = Boolean.valueOf(microserviceWebDriver.executeScript(FrameworkConstants.JAVASCRIPT_NO_AJAX_REQUESTS).toString());
                    if (!noActiveRequests && countDown > 0) {
                        //countDown--;
                        LOG(true, "Waiting - BROWSER NOT READY. Retrying for %s seconds ****", countDown--);
                    }
                    return noActiveRequests;
                }
            });
        } catch (Exception e) {
            DebuggingUtils.takeScreenShot(this.microserviceWebDriver);
            Assert.fail(String.format("Browser didn't appear READY in allotted time of %s seconds", pageTimeoutInSeconds), e);
        }
    }

    /**
     * Check for pending AJAX requests
     *
     * @return flag when no active AJAX requests are present
     */
    private boolean pendingAjaxRequests() {

        Object ajaxConnectionCount = executeJavascript(JavascriptConstants.IS_OPEN_HTTPS);
        if (ajaxConnectionCount instanceof Long) {
            Long count = (Long) ajaxConnectionCount;
            if (count > 0L) {
                LOG(true, "active request count: %s", count);
            }
            if (count == 0L) {
                return true;
            }
        } else {
            monkeyPatchXMLHttpRequest();
        }

        return false;
    }

    /**
     * Perform XMLHttpRequest checking for readyState == Done
     */
    private void monkeyPatchXMLHttpRequest() {

        Object numberOfAjaxConnections = executeJavascript(JavascriptConstants.IS_OPEN_HTTPS);
        if (numberOfAjaxConnections instanceof Long) {
            return;
        }
        String script = "  (function() {" +
                "var oldOpen = XMLHttpRequest.prototype.open;" +
                "window.openHTTPs = 0;" +
                "XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {" +
                "window.openHTTPs++;" +
                "this.addEventListener('readystatechange', function() {" +
                "if(this.readyState == 4) {" +
                "window.openHTTPs--;" +
                "}" +
                "}, false);" +
                "oldOpen.call(this, method, url, async, user, pass);" +
                "}" +
                "})();";
        executeJavascript(script);

    }

    /**
     * Refresh the currently displayed browser
     */
    public void refreshBrowser() {
        microserviceWebDriver.navigate().refresh();
    }

    public void setMicroserviceWebDriver(MicroserviceWebDriver microserviceWebDriver) {
        this.microserviceWebDriver = microserviceWebDriver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getUniqueCookiesFile() {
        return uniqueCookiesFile;
    }

    public void deleteCookie(final String cookieIdentifier) {
        Set<org.openqa.selenium.Cookie> cookiesToDelete = findCookies(cookieIdentifier);
        for (org.openqa.selenium.Cookie cookie : cookiesToDelete) {
            this.microserviceWebDriver.manage().deleteCookie(cookie);
            assertEquals("Verify deleteCookie() name='%s', value='%s'", findCookies(cookie).size(), 0, cookie.getName(), cookie.getValue());
        }
    }

    public void addCookie(final String cookieName, final String cookieValue, String cookieDomain, String cookiePath, Object cookieExpiryDateOffset, boolean secureCookie) {
        org.openqa.selenium.Cookie cookieToAdd;
        if (cookieExpiryDateOffset instanceof Integer) {
            cookieToAdd = new org.openqa.selenium.Cookie(cookieName, cookieValue, cookieDomain, cookiePath, DateUtils.getDateByOffset((int) cookieExpiryDateOffset), secureCookie);
        } else {
            cookieToAdd = new org.openqa.selenium.Cookie(cookieName, cookieValue, cookieDomain, cookiePath, null, secureCookie);
        }
        this.microserviceWebDriver.manage().addCookie(cookieToAdd);
        assertTrue("Verify addCookie() name='%s', value='%s'", findCookies(cookieName).size() > 0, cookieToAdd.getName(), cookieToAdd.getValue());
    }

    public void modifyCookie(final String cookieName, final String cookieValue, String cookieDomain, String cookiePath, Object cookieExpiryDateOffset, boolean secureCookie) {
        deleteCookie(cookieName);
        addCookie(cookieName, cookieValue, cookieDomain, cookiePath, cookieExpiryDateOffset, secureCookie);
        assertTrue("Verify addCookie() name='%s', value='%s'", findCookies(cookieName).size() > 0, cookieName, cookieValue);
    }

    /**
     * Find a Cookie in the list of currently loaded cookies in the RemoteWebDriver
     * based on the given Cookie name or Cookie value
     *
     * @param cookieIdentifier cookie name or value of the cookie to find
     * @return cookies Set of cookies matching search criteria
     */
    private Set<org.openqa.selenium.Cookie> findCookies(final String cookieIdentifier) {
        Set<org.openqa.selenium.Cookie> foundCookies = new HashSet<>();
        Set<org.openqa.selenium.Cookie> cookies = this.microserviceWebDriver.manage().getCookies();
        for (org.openqa.selenium.Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), cookieIdentifier) ||
                    StringUtils.equals(cookie.getValue(), cookieIdentifier)) {
                foundCookies.add(cookie);
            }
        }
        return foundCookies;
    }

    /**
     * Find a Cookie in the list of currently loaded cookies in the RemoteWebDriver
     * based on the given Cookie obj
     *
     * @param cookieToFind cookie to find in collection of active cookies
     * @return cookies Set of cookies matching search criteria
     */
    private Set<org.openqa.selenium.Cookie> findCookies(final org.openqa.selenium.Cookie cookieToFind) {
        Set<org.openqa.selenium.Cookie> foundCookies = new HashSet<>();
        Set<org.openqa.selenium.Cookie> cookies = this.microserviceWebDriver.manage().getCookies();
        for (org.openqa.selenium.Cookie cookie : cookies) {
            if (cookie == cookieToFind) {
                foundCookies.add(cookie);
            }
        }
        return foundCookies;
    }

    /**
     * Wait for implicit amount of time
     *
     * @param millis wait duration
     */
    private void waitForDuration(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException eat) {
            eat.getMessage();
        }
    }

    public MicroserviceWebDriver getMicroserviceWebDriver() {
        return this.microserviceWebDriver;
    }

    public CloseableHttpClient getCustomHttpClient() {
        return this.customHttpClient;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getSiteMinderRedirectUrl() {
        return siteMinderRedirectUrl;
    }

    public void setSiteMinderRedirectUrl(String siteMinderRedirectUrl) {
        this.siteMinderRedirectUrl = siteMinderRedirectUrl;
    }

    public void setSleepInMillis(long sleepInMillis) {
        this.sleepInMillis = sleepInMillis;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public void setPageTimeoutInSeconds(long pageTimeoutInSeconds) {
        this.pageTimeoutInSeconds = pageTimeoutInSeconds;
    }

}
