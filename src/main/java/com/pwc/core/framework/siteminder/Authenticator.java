package com.pwc.core.framework.siteminder;

import com.pwc.core.framework.processors.rest.WebServiceProcessor;
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

    public CloseableHttpClient generateClient(String url, String username, String password, SSLContext sslcontext) {
        return WebServiceProcessor.buildHttpClient(url, username, password, sslcontext);
    }

    /**
     * Build custom SSLContext
     *
     * @return custom, secured SSLContext
     * @throws NoSuchAlgorithmException exception
     * @throws KeyManagementException   exception
     * @throws KeyStoreException        exception
     */
    protected static SSLContext buildSSLContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        return custom()
                .setSecureRandom(new SecureRandom())
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();
    }

    /**
     * Get Authentication cookies from a login attempt with the Siteminder server for a site protected
     * with SSL.
     *
     * @param url      the url to use to redirect to the Siteminder login prompt.
     * @param username the username to log into Siteminder with.
     * @param password the password to login into Siteminder with.
     * @return a List of cookies that contain the Siteminder authentication token
     * @throws IOException authenticated cookies generation failure
     */
    public List<Cookie> getAuthenticationCookies(String url, String username, String password) throws IOException {
        List<Cookie> cookies = null;
        CloseableHttpClient httpClient;
        CloseableHttpResponse response = null;
        try {

            SSLContext sslcontext = buildSSLContext();
            httpClient = generateClient(url, username, password, sslcontext);
            LOG(true, "Authenticating User='%s'", username);

            HttpGet get = generateHttpGet(url);
            LOG(true, "Authenticating Request='%s'", get.getRequestLine());

            HttpClientContext context = getHttpClientContext();
            response = httpClient.execute(get, context);
            LOG(true, "Authentication Status = '%s'", response.getStatusLine());

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

    public HttpClientContext getHttpClientContext() {
        return HttpClientContext.create();
    }

    /**
     * Remove cookie by Cookie name
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
