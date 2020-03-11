package com.pwc.core.framework.data;

public enum CommonField {

    ADDRESSES("addresses"),
    DELETED("Deleted"),
    HTML("html"),
    INBOXES("inboxes"),
    MESSAGE("message"),
    MESSAGES("messages"),
    MSGS("msgs");

    private String field;

    CommonField(String fieldName) {
        this.field = fieldName;
    }

    public String value() {
        return this.field;
    }

}
