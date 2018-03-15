package com.pwc.core.framework.siteminder;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SiteMinderTest extends Authenticator {

    private static final String APPLICATION_WEB_URL = "http://my-web-application.mywebsite.com";
    private static final String USERNAME = "foo";
    private static final String PASSWORD = "bar";

    private List<Cookie> cookies;
    private Authenticator spyAuthenticator;
    private CloseableHttpResponse mockCloseableHttpResponse;
    private HttpGet mockHttpGet;
    private CloseableHttpClient mockDefaultHttpClient;
    private HttpClientContext mockHttpClientContext;

    @Before
    public void setUp() throws Exception {
        mockDefaultHttpClient = mock(CloseableHttpClient.class);
        mockHttpGet = mock(HttpGet.class);
        mockHttpGet.setURI(new URI(APPLICATION_WEB_URL));
        mockHttpClientContext = mock(HttpClientContext.class);
        mockCloseableHttpResponse = mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = mock(StatusLine.class);

        CookieStore mockCookieStore = mock(CookieStore.class);

        cookies = new ArrayList<>();
        BasicClientCookie cookie = new BasicClientCookie("SMSESSION", "unit test");
        cookie.setDomain(".qclogin.unittesting");
        cookies.add(cookie);
        mockCookieStore.addCookie(cookies.get(0));

        when(mockCloseableHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockHttpClientContext.getCookieStore()).thenReturn(mockCookieStore);
        when(mockCookieStore.getCookies()).thenReturn(cookies);

        spyAuthenticator = spy(new Authenticator());
        doReturn(mockHttpClientContext).when(spyAuthenticator).getHttpClientContext();

        SSLContext mockSSLContext = mock(SSLContext.class);
        SSLContextBuilder mockSSLContextBuilder = mock(SSLContextBuilder.class);

        when(mockSSLContextBuilder.setSecureRandom(new SecureRandom())).thenReturn(mockSSLContextBuilder);
        when(mockSSLContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy())).thenReturn(mockSSLContextBuilder);
        when(mockSSLContextBuilder.build()).thenReturn(mockSSLContext);
        doReturn(mockDefaultHttpClient).when(spyAuthenticator).generateClient(APPLICATION_WEB_URL, USERNAME, PASSWORD, mockSSLContext);

        doReturn(mockHttpGet).when(spyAuthenticator).generateHttpGet(APPLICATION_WEB_URL);
        when(mockDefaultHttpClient.execute(mockHttpGet)).thenReturn(mockCloseableHttpResponse);
        when(spyAuthenticator.generateClient(APPLICATION_WEB_URL, USERNAME, PASSWORD, mockSSLContext)).thenReturn(mockDefaultHttpClient);
        when(mockDefaultHttpClient.execute(mockHttpGet, mockHttpClientContext)).thenReturn(mockCloseableHttpResponse);

    }

    @Test(expected = AssertionError.class)
    public void retryAuthenticationRuntimeExceptionTest() throws Exception {
        when(mockHttpGet.getURI()).thenReturn(URI.create(APPLICATION_WEB_URL));
        when(mockCloseableHttpResponse.getStatusLine()).thenThrow(Exception.class);
        HttpClientContext context = spyAuthenticator.retryAuthentication(mockCloseableHttpResponse, mockHttpGet, mockDefaultHttpClient);
        Assert.assertNull(context);
    }

    @Test
    public void retryAuthentication200ResponseTest() throws Exception {
        when(mockDefaultHttpClient.execute(mockHttpGet, mockHttpClientContext).getStatusLine().getStatusCode()).thenReturn(HttpStatus.SC_OK);
        HttpClientContext context = spyAuthenticator.retryAuthentication(mockCloseableHttpResponse, mockHttpGet, mockDefaultHttpClient);
        Assert.assertNotNull(context);
    }

    @Test
    public void retryAuthentication504ResponseAndRetryTest() throws Exception {
        when(mockDefaultHttpClient.execute(mockHttpGet, mockHttpClientContext).getStatusLine().getStatusCode()).thenReturn(HttpStatus.SC_GATEWAY_TIMEOUT);
        HttpClientContext context = spyAuthenticator.retryAuthentication(mockCloseableHttpResponse, mockHttpGet, mockDefaultHttpClient);
        Assert.assertNull(context);
    }

    @Test(expected = Exception.class)
    public void getAuthenticationCookiesTest() throws Exception {
        List<Cookie> cookies = spyAuthenticator.getAuthenticationCookies(APPLICATION_WEB_URL, USERNAME, PASSWORD);
        Assert.assertEquals(0, cookies.size());
    }

    @Test(expected = Exception.class)
    public void getAuthenticationOtherCookiesTest() throws Exception {
        cookies.clear();
        BasicClientCookie cookie = new BasicClientCookie("SESSIONID", "unit test");
        cookies.add(cookie);
        List<Cookie> cookies = spyAuthenticator.getAuthenticationCookies(APPLICATION_WEB_URL, USERNAME, PASSWORD);
        Assert.assertEquals(1, cookies.size());
    }

    @Test(expected = Exception.class)
    public void removeJSESSIONIDCookieTest() throws Exception {
        cookies.clear();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "unit test");
        cookies.add(cookie);
        List<Cookie> cookies = spyAuthenticator.getAuthenticationCookies(APPLICATION_WEB_URL, USERNAME, PASSWORD);
        Assert.assertEquals(0, cookies.size());
    }

    @Test(expected = Exception.class)
    public void getAuthenticationCookiesFailedToAuthenticateTest() throws Exception {
        List<Cookie> cookies = spyAuthenticator.getAuthenticationCookies(APPLICATION_WEB_URL, "SMSESSION", "unit test");
        Assert.assertEquals(0, cookies.size());
    }

    @Test(expected = Exception.class)
    public void getAuthenticationNoCookiesExceptionTest() throws Exception {
        cookies.clear();
        List<Cookie> cookies = spyAuthenticator.getAuthenticationCookies(APPLICATION_WEB_URL, USERNAME, PASSWORD);
        Assert.assertEquals(0, cookies.size());
    }

    @Test(expected = Exception.class)
    public void getAuthenticationCookiesExceptionTest() throws Exception {
        doReturn(null).when(spyAuthenticator).generateHttpGet(APPLICATION_WEB_URL);
        List<Cookie> cookies = spyAuthenticator.getAuthenticationCookies(APPLICATION_WEB_URL, USERNAME, PASSWORD);
        Assert.assertEquals(0, cookies.size());
    }

}
