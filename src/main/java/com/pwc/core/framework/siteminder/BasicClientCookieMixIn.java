package com.pwc.core.framework.siteminder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BasicClientCookieMixIn {

    BasicClientCookieMixIn(@JsonProperty("name") String name, @JsonProperty("value") String value) {
    }

    abstract boolean setPersistent(@JsonProperty("persistent") boolean persistent);

    @JsonIgnore
    abstract String getCommentURL();

    @JsonIgnore
    abstract int[] getPorts();

    @JsonIgnore
    abstract boolean isPersistent();

}
