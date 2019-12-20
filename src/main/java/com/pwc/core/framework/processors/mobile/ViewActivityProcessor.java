package com.pwc.core.framework.processors.mobile;

import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeApplicationImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeWindowImpl;
import io.appium.java_client.MobileElement;

public class ViewActivityProcessor {

    private static ViewActivityProcessor instance = new ViewActivityProcessor();

    private ViewActivityProcessor() {
    }

    public static ViewActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(MobileElement mobileElement) {
        return XCUIElementTypeApplicationImpl.applies(mobileElement) ||
                XCUIElementTypeWindowImpl.applies(mobileElement);
    }

    public void mobileAction(MobileElement mobileElement, Object value) {
        if (XCUIElementTypeApplicationImpl.applies(mobileElement)) {
            XCUIElementTypeApplicationImpl element = new XCUIElementTypeApplicationImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeWindowImpl.applies(mobileElement)) {
            XCUIElementTypeWindowImpl element = new XCUIElementTypeWindowImpl();
            element.mobileAction(mobileElement, value);
        }
    }

}
