package com.pwc.core.framework.data;

import java.util.HashMap;

public class HeaderKeysMap {

    private HashMap<String, String> authorizationMap;

    public HeaderKeysMap(HashMap headerMap) {
        this.authorizationMap = headerMap;
    }

    public HashMap<String, String> getAuthorizationMap() {
        return authorizationMap;
    }

    public void setAuthorizationMap(HashMap<String, String> authorizationMap) {
        this.authorizationMap = authorizationMap;
    }
}
