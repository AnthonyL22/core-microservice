package com.pwc.core.framework.processors.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.config.JsonPathConfig;
import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.command.WebServiceCommand;
import com.pwc.core.framework.data.HeaderKeysMap;
import com.pwc.core.framework.data.OAuthKey;
import com.pwc.core.framework.data.SmSessionKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StopWatch;
import org.testng.Assert;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pwc.logging.service.LoggerService.LOG;
import static org.apache.http.ssl.SSLContexts.custom;

public class WebServiceProcessor {

    private static final String DEFAULT_ENCODING = "ISO-8859-1";

    public WebServiceProcessor() {
    }

    /**
     * Build URL based on <code>BaseGetCommand</code> and request mapping <code>String</code>
     *
     * @param url           web service URL
     * @param command       command to execute
     * @param pathParameter web service path parameter
     * @return web service URL
     */
    protected String constructRestUrl(final String url, final WebServiceCommand command, final Object pathParameter) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(StringUtils.appendIfMissing(url, "/", "/"));
        if (StringUtils.endsWith(command.mappingName(), "=")) {
            urlBuilder.append(command.mappingName());
        } else {
            if (StringUtils.isNotEmpty(command.mappingName())) {
                urlBuilder.append(StringUtils.appendIfMissing(command.mappingName(), "/", "/"));
            }
        }

        if (pathParameter == null) {
            urlBuilder.append(command.methodName());
        } else {
            if (StringUtils.endsWith(command.methodName(), "=")) {
                urlBuilder.append(command.methodName());
            } else {
                if (StringUtils.isNotEmpty(command.methodName())) {
                    urlBuilder.append(StringUtils.appendIfMissing(command.methodName(), "/", "/"));
                }
            }
            if (StringUtils.containsIgnoreCase(pathParameter.toString(), "=") && !StringUtils.endsWithIgnoreCase(urlBuilder.toString(), "?")) {
                if (StringUtils.endsWithIgnoreCase(urlBuilder.toString(), "/")) {
                    urlBuilder.deleteCharAt(urlBuilder.length() - 1);
                }
                urlBuilder.append(StringUtils.prependIfMissing(pathParameter.toString(), "?"));
            } else {
                urlBuilder.append(pathParameter);
            }
        }


        LOG(true, "REST action for url='%s'", urlBuilder.toString());
        return urlBuilder.toString();
    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response
     *
     * @param url  web service URL
     * @param user username
     * @param pass password
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final String user, final String pass) {

        LOG(true, "HTTP action for url='%s'", url);
        Object wsResponse = null;
        try {

            HttpGet httpGet;
            CloseableHttpResponse response;
            CloseableHttpClient httpclient = getAuthenticatedClient(url, user, pass);
            httpGet = new HttpGet(url);
            httpGet = (HttpGet) setHeaderCredentials(user, pass, httpGet);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            response = httpclient.execute(httpGet);
            stopWatch.stop();
            HttpEntity httpEntity = response.getEntity();
            wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
            closeHttpConnections(httpclient, response);

        } catch (Exception e) {
            Assert.fail(String.format("HTTP call failed for url=%s", url), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response
     *
     * @param url     web service URL
     * @param user    username
     * @param pass    user's password
     * @param command Type of command to execute
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final String user, final String pass, final WebServiceCommand command) {
        return execute(url, user, pass, command, null);
    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response. Example: http://www.pacificwebconsulting.com/rest/getAllUsers
     *
     * @param url           web service URL
     * @param user          username
     * @param pass          user's password
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final String user, final String pass, final WebServiceCommand command, final Object pathParameter) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            HttpGet httpGet;
            HttpPost httpPost;
            HttpDelete httpDelete;
            HttpPut httpPut;
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = getAuthenticatedClient(wsUrl, user, pass);

            StopWatch stopWatch = new StopWatch();
            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {
                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(user, pass, httpPost);
                stopWatch.start();
                response = httpclient.execute(httpPost);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.GET_REQUEST)) {
                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(user, pass, httpGet);
                stopWatch.start();
                response = httpclient.execute(httpGet);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {
                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(user, pass, httpPut);
                stopWatch.start();
                response = httpclient.execute(httpPut);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.DELETE_REQUEST)) {
                httpDelete = new HttpDelete(wsUrl);
                httpDelete = (HttpDelete) setHeaderCredentials(user, pass, httpDelete);
                stopWatch.start();
                response = httpclient.execute(httpDelete);
                stopWatch.stop();
            }

            HttpEntity httpEntity = response.getEntity();
            wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
            closeHttpConnections(httpclient, response);

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response
     *
     * @param url      web service URL
     * @param oAuthKey OAuth key to use as the Bearer token
     * @param command  Type of command to execute
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final OAuthKey oAuthKey, final WebServiceCommand command) {
        return execute(url, oAuthKey, command, null);
    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response. Example: http://www.mywebsite.com/rest/getAllUsers
     *
     * @param url           web service URL
     * @param oAuthKey      OAuth key to use as the Bearer token
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final OAuthKey oAuthKey, final WebServiceCommand command, final Object pathParameter) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            HttpGet httpGet;
            HttpPost httpPost;
            HttpDelete httpDelete;
            HttpPut httpPut;
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build();

            StopWatch stopWatch = new StopWatch();
            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {
                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(oAuthKey, httpPost);
                stopWatch.start();
                response = httpclient.execute(httpPost);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.GET_REQUEST)) {
                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(oAuthKey, httpGet);
                stopWatch.start();
                response = httpclient.execute(httpGet);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {
                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(oAuthKey, httpPut);
                stopWatch.start();
                response = httpclient.execute(httpPut);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.DELETE_REQUEST)) {
                httpDelete = new HttpDelete(wsUrl);
                httpDelete = (HttpDelete) setHeaderCredentials(oAuthKey, httpDelete);
                stopWatch.start();
                response = httpclient.execute(httpDelete);
                stopWatch.stop();
            }

            HttpEntity httpEntity = response.getEntity();
            wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
            closeHttpConnections(httpclient, response);

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method
     *
     * @param url           web service URL
     * @param oAuthKey      OAuth key to use as the Bearer token
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @param payload       Request body payload
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final OAuthKey oAuthKey, final WebServiceCommand command, final Object pathParameter, final Object payload) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build();

            HttpGet httpGet;
            HttpPost httpPost;
            HttpPut httpPut;

            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {

                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(oAuthKey, httpPost);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpPost, (HashMap<String, Object>) payload);
                    LOG(true, "OAUTH POST URI='%s'", uri);

                    httpPost.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof List) {

                    StringEntity stringEntity = new StringEntity(((List) payload).get(0).toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "OAUTH POST JSON='%s'", ((List) payload).get(0).toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof String) {

                    StringEntity stringEntity = new StringEntity(payload.toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "OAUTH POST JSON='%s'", payload.toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }

            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {

                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(oAuthKey, httpPut);

                if (payload instanceof HashMap) {

                    URI uri = new URI(wsUrl);
                    LOG(true, "OAUTH PUT URI='%s'", uri);
                    httpPut.setURI(uri);

                    String entityPayload = getFirstValueInMap((HashMap<String, Object>) payload);
                    httpPut.setHeader("Content-Type", "application/json");
                    httpPut.setEntity(new StringEntity(entityPayload, "UTF-8"));

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPut);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            } else {

                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(oAuthKey, httpGet);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpGet, (HashMap<String, Object>) payload);
                    LOG(true, "OAUTH GET URI='%s'", uri);
                    httpGet.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            }

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response
     *
     * @param url           web service URL
     * @param headerKeysMap map of header key/value pairs necessary for authorization
     * @param command       Type of command to execute
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final HeaderKeysMap headerKeysMap, final WebServiceCommand command) {
        return execute(url, headerKeysMap, command, null);
    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response. Example: http://www.mywebsite.com/rest/getAllUsers
     *
     * @param url           web service URL
     * @param headerKeysMap map of header key/value pairs necessary for authorization
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final HeaderKeysMap headerKeysMap, final WebServiceCommand command, final Object pathParameter) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            HttpGet httpGet;
            HttpPost httpPost;
            HttpDelete httpDelete;
            HttpPut httpPut;
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build();

            StopWatch stopWatch = new StopWatch();
            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {
                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpPost);
                stopWatch.start();
                response = httpclient.execute(httpPost);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.GET_REQUEST)) {
                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpGet);
                stopWatch.start();
                response = httpclient.execute(httpGet);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {
                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpPut);
                stopWatch.start();
                response = httpclient.execute(httpPut);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.DELETE_REQUEST)) {
                httpDelete = new HttpDelete(wsUrl);
                httpDelete = (HttpDelete) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpDelete);
                stopWatch.start();
                response = httpclient.execute(httpDelete);
                stopWatch.stop();
            }

            HttpEntity httpEntity = response.getEntity();
            wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
            closeHttpConnections(httpclient, response);

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method
     *
     * @param url           web service URL
     * @param headerKeysMap map of header key/value pairs necessary for authorization
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @param payload       Request body payload
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final HeaderKeysMap headerKeysMap, final WebServiceCommand command, final Object pathParameter, final Object payload) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build();

            HttpGet httpGet;
            HttpPost httpPost;
            HttpPut httpPut;

            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {

                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpPost);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpPost, (HashMap<String, Object>) payload);
                    LOG(true, "AUTHORIZED POST URI='%s'", uri);

                    httpPost.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof List) {

                    StringEntity stringEntity = new StringEntity(((List) payload).get(0).toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "AUTHORIZED POST JSON='%s'", ((List) payload).get(0).toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof String) {

                    StringEntity stringEntity = new StringEntity(payload.toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "AUTHORIZED POST JSON='%s'", payload.toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }

            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {

                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpPut);

                if (payload instanceof HashMap) {

                    URI uri = new URI(wsUrl);
                    LOG(true, "AUTHORIZED PUT URI='%s'", uri);
                    httpPut.setURI(uri);

                    String entityPayload = getFirstValueInMap((HashMap<String, Object>) payload);
                    httpPut.setHeader("Content-Type", "application/json");
                    httpPut.setEntity(new StringEntity(entityPayload, "UTF-8"));

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPut);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            } else {

                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(headerKeysMap.getAuthorizationMap(), httpGet);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpGet, (HashMap<String, Object>) payload);
                    LOG(true, "AUTHORIZED GET URI='%s'", uri);
                    httpGet.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            }

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response
     *
     * @param url          web service URL
     * @param smSessionKey SMSESSION key to use as the SMSESSION token
     * @param command      Type of command to execute
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final SmSessionKey smSessionKey, final WebServiceCommand command) {
        return execute(url, smSessionKey, command, null);
    }

    /**
     * Generic REST web service Execution method for simple GET requests
     * that return a String response. Example: http://www.mywebsite.com/rest/getAllUsers
     *
     * @param url           web service URL
     * @param smSessionKey  SMSESSION key to use as the SMSESSION token
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final SmSessionKey smSessionKey, final WebServiceCommand command, final Object pathParameter) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            HttpGet httpGet;
            HttpPost httpPost;
            HttpDelete httpDelete;
            HttpPut httpPut;
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build();

            StopWatch stopWatch = new StopWatch();
            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {
                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(smSessionKey, httpPost);
                stopWatch.start();
                response = httpclient.execute(httpPost);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.GET_REQUEST)) {
                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(smSessionKey, httpGet);
                stopWatch.start();
                response = httpclient.execute(httpGet);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {
                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(smSessionKey, httpPut);
                stopWatch.start();
                response = httpclient.execute(httpPut);
                stopWatch.stop();
            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.DELETE_REQUEST)) {
                httpDelete = new HttpDelete(wsUrl);
                httpDelete = (HttpDelete) setHeaderCredentials(smSessionKey, httpDelete);
                stopWatch.start();
                response = httpclient.execute(httpDelete);
                stopWatch.stop();
            }

            HttpEntity httpEntity = response.getEntity();
            wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
            closeHttpConnections(httpclient, response);

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Generic REST web service Execution method
     *
     * @param url           web service URL
     * @param smSessionKey  SMSESSION key to use as the SMSESSION token
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @param payload       Request body payload
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final SmSessionKey smSessionKey, final WebServiceCommand command, final Object pathParameter, final Object payload) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build();

            HttpGet httpGet;
            HttpPost httpPost;
            HttpPut httpPut;

            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {

                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(smSessionKey, httpPost);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpPost, (HashMap<String, Object>) payload);
                    LOG(true, "SMSESSION POST URI='%s'", uri);

                    httpPost.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof List) {

                    StringEntity stringEntity = new StringEntity(((List) payload).get(0).toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "SMSESSION POST JSON='%s'", ((List) payload).get(0).toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof String) {

                    StringEntity stringEntity = new StringEntity(payload.toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "SMSESSION POST JSON='%s'", payload.toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }

            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {

                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(smSessionKey, httpPut);

                if (payload instanceof HashMap) {

                    URI uri = new URI(wsUrl);
                    LOG(true, "SMSESSION PUT URI='%s'", uri);
                    httpPut.setURI(uri);

                    String entityPayload = getFirstValueInMap((HashMap<String, Object>) payload);
                    httpPut.setHeader("Content-Type", "application/json");
                    httpPut.setEntity(new StringEntity(entityPayload, "UTF-8"));

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPut);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            } else {

                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(smSessionKey, httpGet);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpGet, (HashMap<String, Object>) payload);
                    LOG(true, "SMSESSION GET URI='%s'", uri);
                    httpGet.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            }

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Set the BasicAuth header to the user/pass provided
     *
     * @param user     username
     * @param pass     password
     * @param httpBase Http base to set header for
     * @return decorated HttpRequestBase for use by get, post, put, ect..
     */
    protected HttpRequestBase setHeaderCredentials(final String user, final String pass, HttpRequestBase httpBase) {
        try {
            String authentication = user + ":" + pass;
            byte[] encodedAuth = Base64.encodeBase64(authentication.getBytes(Charset.forName(DEFAULT_ENCODING)));
            String authenticationHeader = "Basic " + new String(encodedAuth);
            httpBase.setHeader(HttpHeaders.AUTHORIZATION, authenticationHeader);
            httpBase.setHeader(HttpHeaders.ACCEPT, "application/json");
        } catch (Exception e) {
            return httpBase;
        }
        return httpBase;
    }

    /**
     * Add a header containing a authorization key from either OAUTH2 or Site Minder
     *
     * @param authorizationKey Authorization key of OAuthKey or SmSessionKey types to add to the header
     * @param httpBase         Http base to set header for
     * @return decorated HttpRequestBase for use by get, post, put, ect..
     */
    private HttpRequestBase setHeaderCredentials(final Object authorizationKey, HttpRequestBase httpBase) {
        try {

            BasicHeader header;
            if (authorizationKey instanceof OAuthKey) {
                header = new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ((OAuthKey) authorizationKey).getKey());
            } else {
                header = new BasicHeader("Cookie", "SMSESSION=" + ((SmSessionKey) authorizationKey).getKey());
            }
            httpBase.setHeader(header);
        } catch (Exception e) {
            LOG(true, "Failed to set header credentials", e);
            return httpBase;
        }
        return httpBase;
    }

    /**
     * Add a header containing any number of header variables
     *
     * @param headerMap map of header key/value pairs to add to the request header
     * @param httpBase  Http base to set header for
     * @return decorated HttpRequestBase for use by get, post, put, ect..
     */
    private HttpRequestBase setHeaderCredentials(final HashMap<String, String> headerMap, HttpRequestBase httpBase) {

        try {
            headerMap.entrySet().stream().forEach(headerMapEntry -> {
                BasicHeader header = new BasicHeader(headerMapEntry.getKey(), headerMapEntry.getValue());
                httpBase.setHeader(header);
            });
        } catch (Exception e) {
            LOG(true, "Failed to set header credentials", e);
            return httpBase;
        }
        return httpBase;
    }

    /**
     * Generic REST web service Execution method
     *
     * @param url           web service URL
     * @param user          username
     * @param pass          user's password
     * @param command       Type of command to execute
     * @param pathParameter web service path parameter
     * @param payload       Request body payload
     * @return JsonPath json payload
     */
    protected Object execute(final String url, final String user, final String pass, final WebServiceCommand command, final Object pathParameter, final Object payload) {

        Object wsResponse = null;
        String wsUrl = constructRestUrl(url, command, pathParameter);

        try {

            CloseableHttpClient httpclient = getAuthenticatedClient(wsUrl, user, pass);

            HttpGet httpGet;
            HttpPost httpPost;
            HttpPut httpPut;

            if (StringUtils.equals(command.methodType(), FrameworkConstants.POST_REQUEST)) {

                httpPost = new HttpPost(wsUrl);
                httpPost = (HttpPost) setHeaderCredentials(user, pass, httpPost);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpPost, (HashMap<String, Object>) payload);
                    LOG(true, "POST URI='%s'", uri);

                    httpPost.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof List) {

                    StringEntity stringEntity = new StringEntity(((List) payload).get(0).toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "POST JSON='%s'", ((List) payload).get(0).toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                } else if (payload instanceof String) {

                    StringEntity stringEntity = new StringEntity(payload.toString());
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
                    LOG(true, "POST JSON='%s'", payload.toString());

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPost);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }

            } else if (StringUtils.equals(command.methodType(), FrameworkConstants.PUT_REQUEST)) {

                httpPut = new HttpPut(wsUrl);
                httpPut = (HttpPut) setHeaderCredentials(user, pass, httpPut);

                if (payload instanceof HashMap) {

                    URI uri = new URI(wsUrl);
                    LOG(true, "PUT URI='%s'", uri);
                    httpPut.setURI(uri);

                    String entityPayload = getFirstValueInMap((HashMap<String, Object>) payload);
                    httpPut.setHeader("Content-Type", "application/json");
                    httpPut.setEntity(new StringEntity(entityPayload, "UTF-8"));

                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpPut);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            } else {

                httpGet = new HttpGet(wsUrl);
                httpGet = (HttpGet) setHeaderCredentials(user, pass, httpGet);

                if (payload instanceof HashMap) {

                    URI uri = constructUriFromPayloadMap(httpGet, (HashMap<String, Object>) payload);
                    LOG(true, "GET URI='%s'", uri);
                    httpGet.setURI(uri);
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    stopWatch.stop();
                    HttpEntity httpEntity = response.getEntity();
                    wsResponse = getWebServiceResponse(response, httpEntity, stopWatch);
                    closeHttpConnections(httpclient, response);

                }
            }

        } catch (Exception e) {
            Assert.fail(String.format("REST call failed for url=%s with exception='%s'", wsUrl, e.getCause()), e);
        }

        return wsResponse;

    }

    /**
     * Close the Http response and client sessions
     *
     * @param httpclient HttpClient to close
     * @param response   HttpResponse to close
     * @throws IOException exception if closing connections failure occurs
     */
    private void closeHttpConnections(CloseableHttpClient httpclient, CloseableHttpResponse response) throws IOException {
        response.close();
        httpclient.close();
    }

    /**
     * Construct URI from payload map passed in
     *
     * @param httpRequestBase Http request
     * @param payload         payload to construct URI with
     * @return hydrated, decoded URI
     */
    protected URI constructUriFromPayloadMap(HttpRequestBase httpRequestBase, HashMap<String, Object> payload) {
        URI encodedUri = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, Object> entry : payload.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }

            encodedUri = new URIBuilder(httpRequestBase.getURI()).addParameters(nameValuePairs).build();
            String decoded = URLDecoder.decode(String.valueOf(encodedUri), StandardCharsets.UTF_8.toString());
            return new URI(decoded);

        } catch (Exception e) {
            return encodedUri;
        }
    }

    /**
     * Return the first value in the HashMap provided
     *
     * @param payload payload map
     * @return first value
     */
    private String getFirstValueInMap(HashMap<String, Object> payload) {
        try {
            for (Map.Entry<String, Object> entry : payload.entrySet()) {
                return String.valueOf(entry.getValue());
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    /**
     * Convert current HttpResponse to JSON
     *
     * @param response   current HttpResponse
     * @param httpEntity current HttpEntity
     * @param stopWatch  current StopWatch
     * @return json representation of HttpResponse
     */
    protected Object getWebServiceResponse(CloseableHttpResponse response, HttpEntity httpEntity, StopWatch stopWatch) {
        String wsResponse = convertHttpResponseToJson(response, httpEntity, stopWatch);
        if (isValidJson(wsResponse)) {
            return new JsonPath(wsResponse).using(new JsonPathConfig("UTF-8"));
        }
        return wsResponse;
    }

    /**
     * Convert the current CloseableHttpResponse response's StatusLine to a JSON representation
     *
     * @param response   current response
     * @param httpEntity Http response to evaluate
     * @param stopWatch  Spring stop watch used to report response time of web service to calling test
     * @return JSON response
     */
    private String convertHttpResponseToJson(CloseableHttpResponse response, HttpEntity httpEntity, StopWatch stopWatch) {
        Map<Object, Object> jsonPayload;
        try {
            jsonPayload = new HashMap();
            jsonPayload.put(FrameworkConstants.HTTP_STATUS_VALUE_KEY, response.getStatusLine().getStatusCode());
            jsonPayload.put(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY, response.getStatusLine().getReasonPhrase());
            if (response.getAllHeaders() != null) {
                jsonPayload.put(FrameworkConstants.HTTP_HEADERS_KEY, response.getAllHeaders());
            }
            if (httpEntity != null) {
                jsonPayload.put(FrameworkConstants.HTTP_ENTITY_KEY, EntityUtils.toString(httpEntity));
            }
            if (stopWatch != null) {
                jsonPayload.put(FrameworkConstants.HTTP_RESPONSE_TIME_KEY, stopWatch.getTotalTimeMillis());
            }
        } catch (IOException e) {
            return toJSON("");
        }
        return toJSON(jsonPayload);
    }

    /**
     * Validate if the passed in <code>String</code> is on valid JSON format
     *
     * @param possiblyJson text to check if it is JSON
     * @return true if text is JSON and false otherwise
     */
    protected boolean isValidJson(String possiblyJson) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(possiblyJson);
            return jsonElement.isJsonObject();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    /**
     * toJSON - Helper method to convert domain objects into JSON
     *
     * @param domainObject payload to turn into JSON
     * @return JSON representation of the Domain object
     */
    private static String toJSON(Object domainObject) {
        String json = null;
        try {
            ObjectMapper om = new ObjectMapper();
            com.fasterxml.jackson.databind.ObjectWriter ow = om.writer();
            json = ow.writeValueAsString(domainObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Get authenticated HttpClient
     *
     * @param url      url to authenticate
     * @param username username
     * @param password password
     * @return authenticated HttpClient
     */
    protected CloseableHttpClient getAuthenticatedClient(String url, String username, String password) {

        CloseableHttpClient httpclient = null;
        try {
            SSLContext sslcontext = buildSSLContext();
            httpclient = buildHttpClient(username, password, sslcontext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpclient;

    }

    /**
     * Build SSLContext
     *
     * @return custom SSL Context
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    private static SSLContext buildSSLContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        return custom()
                .setSecureRandom(new SecureRandom())
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();
    }

    /**
     * Build HttpClient for user and password provided
     *
     * @param username   username
     * @param password   password
     * @param sslContext SSLContext
     * @return hydrated HttpClient
     */
    public static CloseableHttpClient buildHttpClient(String username, String password, SSLContext sslContext) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
                .setSSLContext(sslContext)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return httpclient;
    }

}
