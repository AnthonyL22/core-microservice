package com.pwc.core.framework.processors.mobile;

import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeButtonImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeCellImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeStaticTextImpl;
import io.appium.java_client.MobileElement;

public class TapActivityProcessor {

    private static TapActivityProcessor instance = new TapActivityProcessor();

    private TapActivityProcessor() {
    }

    public static TapActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(MobileElement mobileElement) {
        return XCUIElementTypeCellImpl.applies(mobileElement) ||
                XCUIElementTypeStaticTextImpl.applies(mobileElement) ||
                XCUIElementTypeButtonImpl.applies(mobileElement);
    }

    public void mobileAction(MobileElement mobileElement, Object value) {

        if (XCUIElementTypeCellImpl.applies(mobileElement)) {
            XCUIElementTypeCellImpl element = new XCUIElementTypeCellImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeStaticTextImpl.applies(mobileElement)) {
            XCUIElementTypeStaticTextImpl element = new XCUIElementTypeStaticTextImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeButtonImpl.applies(mobileElement)) {
            XCUIElementTypeButtonImpl element = new XCUIElementTypeButtonImpl();
            element.mobileAction(mobileElement, value);
        }
    }

}
