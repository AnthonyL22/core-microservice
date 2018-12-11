package com.pwc.core.framework.controller;

import com.jayway.restassured.path.json.JsonPath;
import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.command.WebServiceCommand;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.HeaderKeysMap;
import com.pwc.core.framework.data.OAuthKey;
import com.pwc.core.framework.data.SmSessionKey;
import com.pwc.core.framework.processors.rest.WebServiceProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.pwc.logging.service.LoggerService.LOG;

@Component
public class WebServiceController extends WebServiceProcessor {

    @Value("${web.services.url}")
    private String url;

    @Value("${web.services.user}")
    private String user;

    @Value("${web.services.password}")
    private String password;

    /**
     * Web service execution based on web service URL defined in automation.properties
     *
     * @param credentials   user credentials
     * @param command       web service command
     * @param pathParameter web service path parameter(s)
     * @param parameterMap  web service parameter map
     * @return web service response
     */
    public Object webServiceAction(final Credentials credentials, final WebServiceCommand command, final Object pathParameter, final Object parameterMap) {
        if (credentials != null) {
            if (pathParameter == null && parameterMap == null) {
                return execute(url, credentials.getUsername(), credentials.getPassword(), command);
            } else if (parameterMap == null && pathParameter != null) {
                return execute(url, credentials.getUsername(), credentials.getPassword(), command, pathParameter);
            } else {
                return execute(url, credentials.getUsername(), credentials.getPassword(), command, pathParameter, parameterMap);
            }
        } else {
            if (pathParameter == null && parameterMap == null) {
                return execute(url, user, password, command);
            } else if (parameterMap == null && pathParameter != null) {
                return execute(url, user, password, command, pathParameter);
            } else {
                return execute(url, user, password, command, pathParameter, parameterMap);
            }
        }
    }

    /**
     * Web service execution based on web service URL defined in automation.properties.  Typically, accessing a
     * direct url
     *
     * @param credentials user credentials
     * @param url         web service url segment
     * @return web service response
     */
    public Object webServiceAction(final Credentials credentials, final String url) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(StringUtils.appendIfMissing(this.url, "/", "/"));
        urlBuilder.append(StringUtils.removeStart(url, "/"));
        if (credentials != null) {
            return execute(urlBuilder.toString(), credentials.getUsername(), credentials.getPassword());
        } else {
            return execute(urlBuilder.toString(), user, password);
        }
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param oAuthKey OAuth key
     * @param command  BaseGetCommand command type
     * @return web service response
     */
    protected Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command) {
        return webServiceAction(oAuthKey, command, null, null);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param oAuthKey    OAuth key
     * @param command     BaseGetCommand command type
     * @param requestBody POST request body
     * @return web service response
     */
    protected Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command, final Object requestBody) {
        return webServiceAction(oAuthKey, command, requestBody, null);
    }

    /**
     * Web service execution based on web service URL defined in automation.properties
     *
     * @param oAuthKey      OAuth key
     * @param command       web service command
     * @param pathParameter web service path parameter(s)
     * @param parameterMap  web service parameter map
     * @return web service response
     */
    public Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command, final Object pathParameter, final Object parameterMap) {
        if (pathParameter == null && parameterMap == null) {
            return execute(url, oAuthKey, command);
        } else if (parameterMap == null && pathParameter != null) {
            return execute(url, oAuthKey, command, pathParameter);
        } else {
            return execute(url, oAuthKey, command, pathParameter, parameterMap);
        }
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param headerKeysMap map of header key/value pairs necessary for authorization
     * @param command       BaseGetCommand command type
     * @param requestBody   POST request body
     * @return web service response
     */
    protected Object webServiceAction(final HeaderKeysMap headerKeysMap, final WebServiceCommand command, final Object requestBody) {
        return webServiceAction(headerKeysMap, command, requestBody, null);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param headerKeysMap map of header key/value pairs necessary for authorization
     * @param command       BaseGetCommand command type
     * @return web service response
     */
    protected Object webServiceAction(final HeaderKeysMap headerKeysMap, final WebServiceCommand command) {
        return webServiceAction(headerKeysMap, command, null, null);
    }

    /**
     * Web service execution based on web service URL defined in automation.properties
     *
     * @param headerKeysMap map of header key/value pairs necessary for authorization
     * @param command       web service command
     * @param pathParameter web service path parameter(s)
     * @param parameterMap  web service parameter map
     * @return web service response
     */
    public Object webServiceAction(final HeaderKeysMap headerKeysMap, final WebServiceCommand command, final Object pathParameter, final Object parameterMap) {
        if (pathParameter == null && parameterMap == null) {
            return execute(url, headerKeysMap, command);
        } else if (parameterMap == null && pathParameter != null) {
            return execute(url, headerKeysMap, command, pathParameter);
        } else {
            return execute(url, headerKeysMap, command, pathParameter, parameterMap);
        }
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param smSessionKey SMSESSION key
     * @param command      BaseGetCommand command type
     * @return web service response
     */
    protected Object webServiceAction(final SmSessionKey smSessionKey, final WebServiceCommand command) {
        return webServiceAction(smSessionKey, command, null, null);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param smSessionKey SMSESSION key
     * @param command      BaseGetCommand command type
     * @param requestBody  POST request body
     * @return web service response
     */
    protected Object webServiceAction(final SmSessionKey smSessionKey, final WebServiceCommand command, final Object requestBody) {
        return webServiceAction(smSessionKey, command, requestBody, null);
    }

    /**
     * Web service execution based on web service URL defined in automation.properties
     *
     * @param smSessionKey  SMSESSION key
     * @param command       web service command
     * @param pathParameter web service path parameter(s)
     * @param parameterMap  web service parameter map
     * @return web service response
     */
    public Object webServiceAction(final SmSessionKey smSessionKey, final WebServiceCommand command, final Object pathParameter, final Object parameterMap) {
        if (pathParameter == null && parameterMap == null) {
            return execute(url, smSessionKey, command);
        } else if (parameterMap == null && pathParameter != null) {
            return execute(url, smSessionKey, command, pathParameter);
        } else {
            return execute(url, smSessionKey, command, pathParameter, parameterMap);
        }
    }

    /**
     * Http execution based on a direct url
     *
     * @param credentials user credentials
     * @param url         user defined url
     * @return web service response
     */
    public Object httpServiceAction(final Credentials credentials, String url) {

        if (!StringUtils.containsIgnoreCase(url, "http")) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(this.url);
            urlBuilder.append(url);
            url = urlBuilder.toString();
        }

        if (credentials != null) {
            return execute(url, credentials.getUsername(), credentials.getPassword());
        } else if (StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(password)) {
            return execute(url, user, password);
        } else {
            return execute(url);
        }
    }

    /**
     * Http execution based on a direct url that returns the resulting HTML for a headless page
     *
     * @param credentials user credentials
     * @param url         user defined url
     * @return HTML representation of headless page
     */
    public String htmlServiceAction(final Credentials credentials, final String url) {

        StringBuilder urlBuilder = new StringBuilder();
        try {

            JsonPath response = (JsonPath) httpServiceAction(credentials, url);
            InputStream targetStream = new ByteArrayInputStream(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString().getBytes());
            BufferedReader rd = new BufferedReader(new InputStreamReader(targetStream));
            String line;
            while ((line = rd.readLine()) != null) {
                urlBuilder.append(line);
            }

        } catch (Exception e) {
            LOG(true, "Failed to read html response for url=%s due to exception=%s", url, e);
        }
        return urlBuilder.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
