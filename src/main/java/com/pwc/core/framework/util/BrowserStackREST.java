package com.pwc.core.framework.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import static com.pwc.logging.service.LoggerService.LOG;

public class BrowserStackREST {

    private static final String API_ENDPOINT = "@api.browserstack.com/automate/sessions/";
    private String username;
    private String accessKey;

    public BrowserStackREST(String username, String accessKey) {
        this.username = username;
        this.accessKey = accessKey;
    }

    public void updateJobInfo(final String sessionId, Map<String, Object> updates) {

        try {
            StringBuilder customUri = new StringBuilder("https://");
            customUri.append(username);
            customUri.append(":");
            customUri.append(accessKey);
            customUri.append(API_ENDPOINT);
            customUri.append(sessionId);
            customUri.append(".json");
            URI uri = new URI(customUri.toString());
            HttpPut putRequest = new HttpPut(uri);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add((new BasicNameValuePair("status", updates.get("status").toString())));
            putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpClientBuilder.create().build().execute(putRequest);

        } catch (Exception e) {
            LOG(true, "Failed to mark ", e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
