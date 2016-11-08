package qcom.itlegal.ipit.framework.processors.web;

import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.processors.web.elements.TextAreaElementImpl;
import qcom.itlegal.ipit.framework.processors.web.elements.TextInputElementImpl;

public class KeyboardActivityProcessor {

    private static KeyboardActivityProcessor instance = new KeyboardActivityProcessor();

    private KeyboardActivityProcessor() {
    }

    public static KeyboardActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(WebElement webElement) {
        return TextInputElementImpl.applies(webElement) ||
                TextAreaElementImpl.applies(webElement);
    }

    public void webAction(WebElement webElement, Object value) {
        if (TextInputElementImpl.applies(webElement)) {
            TextInputElementImpl textInputElementImpl = new TextInputElementImpl();
            textInputElementImpl.webAction(webElement, value);
        } else if (TextAreaElementImpl.applies(webElement)) {
            TextAreaElementImpl textAreaElementImpl = new TextAreaElementImpl();
            textAreaElementImpl.webAction(webElement, value);
        }
    }

}
