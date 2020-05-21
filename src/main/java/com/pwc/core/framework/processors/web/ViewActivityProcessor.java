package com.pwc.core.framework.processors.web;

import com.pwc.core.framework.processors.web.elements.DivElementImpl;
import com.pwc.core.framework.processors.web.elements.HeadingElementImpl;
import com.pwc.core.framework.processors.web.elements.LabelElementImpl;
import com.pwc.core.framework.processors.web.elements.LegendElementImpl;
import org.openqa.selenium.WebElement;

public class ViewActivityProcessor {

    private static ViewActivityProcessor instance = new ViewActivityProcessor();

    private ViewActivityProcessor() {
    }

    public static ViewActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(WebElement webElement) {
        return HeadingElementImpl.applies(webElement) || DivElementImpl.applies(webElement) || LabelElementImpl.applies(webElement) || LegendElementImpl.applies(webElement);
    }

    public void webAction(WebElement webElement, Object value) {
        if (DivElementImpl.applies(webElement)) {
            DivElementImpl divElement = new DivElementImpl();
            divElement.webAction(webElement, value);
        } else if (LabelElementImpl.applies(webElement)) {
            LabelElementImpl labelElement = new LabelElementImpl();
            labelElement.webAction(webElement, value);
        } else if (LegendElementImpl.applies(webElement)) {
            LegendElementImpl legendElement = new LegendElementImpl();
            legendElement.webAction(webElement, value);
        }
    }

}
