package com.pwc.core.framework.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonField {

    ADDRESSES("addresses"),
    DELETED("Deleted"),
    HTML("html"),
    INBOXES("inboxes"),
    MESSAGE("message"),
    MESSAGES("messages"),
    MSGS("msgs");

    private String field;

}
