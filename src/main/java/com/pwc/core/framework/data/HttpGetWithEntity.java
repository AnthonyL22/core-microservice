package com.pwc.core.framework.data;

import org.apache.http.client.methods.HttpPost;

public class HttpGetWithEntity extends HttpPost {

    private static final String METHOD_NAME = "GET";

    public HttpGetWithEntity(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}
