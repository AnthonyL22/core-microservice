package com.pwc.core.framework.siteminder;

import com.pwc.core.framework.processors.rest.WebServiceProcessor;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.testng.Assert;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import static com.pwc.logging.service.LoggerService.LOG;
import static org.apache.http.ssl.SSLContexts.custom;

/**
 * Singleton that provide ability to log into SiteMinder and grab authentication
 * tokens stored as HTTP cookies.
 */
public class Authenticator {

    private static Authenticator instance;
    private static final int MAX_AUTHENTICATION_RETRIES = 5;

    /**
     * Singleton get instance of Authenticator.
     *
     * @return Authenticator instance of Authenticator
     */
    public static Authenticator getInstance() {
        if (instance == null) {
            instance = new Authenticator();
        }
        return instance;
    }

    public HttpGet generateHttpGet(final String url) {
        return new HttpGet(url);
    }

    public CloseableHttpClient generateClient(String username, String password, SSLContext sslcontext) {

        return WebServiceProcessor.buildHttpClient(username, password, sslcontext);
    }

    /**
     * Build custom SSLContext.
     *
     * @return custom, secured SSLContext
     * @throws NoSuchAlgorithmException exception
     * @throws KeyManagementException   exception
     * @throws KeyStoreException        exception
     */
    private static SSLContext buildSSLContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {

        return custom().setSecureRandom(new SecureRandom()).loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
    }

    /**
     * Get Authentication cookies from a login attempt with the SiteMinder server for a site protected
     * with SSL.
     *
     * @param url      the url to use to redirect to the SiteMinder login prompt.
     * @param username the username to log into SiteMinder with.
     * @param password the password to login into SiteMinder with.
     * @return a List of cookies that contain the SiteMinder authentication token
     * @throws IOException authenticated cookies generation failure
     */
    public List<Cookie> getAuthenticationCookies(String url, String username, String password) throws IOException {

        List<Cookie> cookies = null;
        CloseableHttpClient httpClient;
        CloseableHttpResponse response = null;
        try {

            SSLContext sslcontext = buildSSLContext();
            httpClient = generateClient(username, password, sslcontext);
            LOG(true, "Authenticating User='%s'", username);

            HttpGet get = generateHttpGet(url);
            LOG(true, "Authenticating Request='%s'", get.getRequestLine());

            HttpClientContext context = getHttpClientContext();
            response = httpClient.execute(get, context);
            LOG(true, "Authentication Status = '%s'", response.getStatusLine());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                context = retryAuthentication(response, get, httpClient);
            }

            CookieStore cookieStore = context.getCookieStore();
            cookies = cookieStore.getCookies();
            if (cookies != null) {
                LOG(true, "Authentication Cookie Count=%s", cookies.size());
            }
        } catch (Exception e) {
            Assert.fail("Failed to authenticate for cookies using url=" + url, e);
        } finally {
            response.close();
        }

        return cookies;
    }

    /**
     * Due to potential authentication issues, this retry mechanism attempts 5x to authenticate.
     *
     * @param response   HttpResponse
     * @param get        HttpGet
     * @param httpClient HttpClient
     * @return valid HttpClientContext after retry or null if failed
     * @throws IOException exception trying to retry authentication
     */
    protected HttpClientContext retryAuthentication(CloseableHttpResponse response, HttpGet get, CloseableHttpClient httpClient) throws IOException {

        int retryCount = 1;
        HttpClientContext context;
        try {
            while (retryCount <= MAX_AUTHENTICATION_RETRIES) {
                LOG(true, "Authentication Retry %s time due to response='%s'", retryCount, response.getStatusLine().getStatusCode());
                context = getHttpClientContext();
                response = httpClient.execute(get, context);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    retryCount++;
                } else {
                    return context;
                }
            }

        } catch (Exception e) {
            Assert.fail(String.format("Failed to retry authentication after retrying %s times to url=%s", retryCount, get.getURI()), e);
        } finally {
            response.close();
        }
        return null;
    }

    public HttpClientContext getHttpClientContext() {
        return HttpClientContext.create();
    }

    /**
     * Remove cookie by Cookie name.
     *
     * @param cookies    list of Cookie objects
     * @param cookieName Cookie name to remove
     */
    private void removeCookieByName(List<Cookie> cookies, final String cookieName) {

        Cookie cookieToRemove = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                cookieToRemove = cookie;
                break;
            }
        }
        if (cookieToRemove != null) {
            cookies.remove(cookieToRemove);
        }
    }

}
