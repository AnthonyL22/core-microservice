package com.pwc.core.framework.data;


public enum IOSElementType {

    APPLICATION("XCUIElementTypeApplicationImpl"),
    BUTTON("XCUIElementTypeButton"),
    CELL("XCUIElementTypeCell"),
    STATIC_TEXT("XCUIElementTypeStaticText"),
    WINDOW("XCUIElementTypeWindow");

    public String type;

    IOSElementType(String t) {
        type = t;
    }

}
