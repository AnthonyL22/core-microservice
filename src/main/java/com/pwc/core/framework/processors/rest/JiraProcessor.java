package com.pwc.core.framework.processors.rest;


import com.pwc.core.framework.controller.WebServiceController;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Base64;

import static com.pwc.logging.service.LoggerService.LOG;

@Data
public class JiraProcessor {

    private boolean jiraEnabled;
    private String jiraUrl;
    private String jiraUsername;
    private String jiraPassword;
    private String cycleName;
    private static WebServiceProcessor webServiceProcessorInstance = null;

    protected JiraProcessor() {
        webServiceProcessorInstance = new WebServiceController();
    }

    /**
     * Perform POST to Jira endpoint.
     *
     * @param endpoint web service endpoint descriptor
     * @param payload  request payload
     * @return POST response
     */
    public Object executePost(String endpoint, String payload) {

        LOG(false, "AUTHORIZED POST URI='%s'", getJiraUrl() + endpoint);
        Object wsResponse = null;

        int timeout = 3;
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpPost http = new HttpPost(getJiraUrl() + endpoint);
            StringEntity entityBody = new StringEntity(payload);
            http.setEntity(entityBody);
            http.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            http.setHeader("Authorization", generateCredentials());
            CloseableHttpResponse response = httpclient.execute(http);
            HttpEntity httpEntity = response.getEntity();
            wsResponse = JiraProcessor.getWebServiceProcessorInstance().getWebServiceResponse(response, httpEntity, null);
        } catch (Exception e) {
            LOG(true, "Failed to perform POST to url='%s'", endpoint);
        }
        return wsResponse;
    }

    /**
     * Perform PUT to Jira endpoint.
     *
     * @param endpoint web service endpoint descriptor
     * @param payload  request payload
     * @return PUT response
     */
    public Object executePut(String endpoint, String payload) {

        LOG(false, "AUTHORIZED PUT URI='%s'", getJiraUrl() + endpoint);
        Object wsResponse = null;

        int timeout = 3;
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpPut http = new HttpPut(getJiraUrl() + endpoint);
            StringEntity entityBody = new StringEntity(payload);
            http.setEntity(entityBody);
            http.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            http.setHeader("Authorization", generateCredentials());
            CloseableHttpResponse response = httpclient.execute(http);
            HttpEntity httpEntity = response.getEntity();
            wsResponse = JiraProcessor.getWebServiceProcessorInstance().getWebServiceResponse(response, httpEntity, null);
        } catch (Exception e) {
            LOG(true, "Failed to perform PUT to url='%s'", endpoint);
        }
        return wsResponse;
    }

    /**
     * Perform GET to Jira endpoint.
     *
     * @param endpoint web service endpoint descriptor
     * @return GET response
     */
    public Object executeGet(String endpoint) {

        LOG(false, "AUTHORIZED GET URI='%s'", getJiraUrl() + endpoint);
        Object wsResponse = null;

        int timeout = 3;
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpGet http = new HttpGet(getJiraUrl() + endpoint);
            http.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            http.setHeader("Authorization", generateCredentials());
            CloseableHttpResponse response = httpclient.execute(http);
            HttpEntity httpEntity = response.getEntity();
            wsResponse = JiraProcessor.getWebServiceProcessorInstance().getWebServiceResponse(response, httpEntity, null);
        } catch (Exception e) {
            LOG(true, "Failed to perform GET to url='%s'", endpoint);
        }
        return wsResponse;
    }

    /**
     * Generate Authentication credentials.
     *
     * @return Base64 encoded credentials
     */
    protected String generateCredentials() {
        return "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", getJiraUsername(), getJiraPassword()).getBytes());
    }

    /**
     * Singleton WebServiceProcessor provider
     *
     * @return one WebServiceProcessor
     */
    private static WebServiceProcessor getWebServiceProcessorInstance() {

        if (null == webServiceProcessorInstance) {
            webServiceProcessorInstance = new WebServiceProcessor();
        }
        return webServiceProcessorInstance;
    }

}
