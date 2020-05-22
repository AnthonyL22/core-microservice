package com.pwc.core.framework.data;

public enum XCUIElementAttribute {

    ELEMENT_ID("elementId"),
    ENABLED("enabled"),
    HEIGHT("height"),
    LABEL("label"),
    NAME("name"),
    TYPE("type"),
    VISIBLE("visible"),
    VALUE("value"),
    WIDTH("width"),
    X("x"),
    Y("y");

    public String attribute;

    XCUIElementAttribute(String t) {
        attribute = t;
    }

}
