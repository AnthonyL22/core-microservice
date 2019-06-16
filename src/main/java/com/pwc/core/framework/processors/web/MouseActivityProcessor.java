package com.pwc.core.framework.processors.web;

import com.pwc.core.framework.processors.web.elements.AnchorElementImpl;
import com.pwc.core.framework.processors.web.elements.ButtonElementImpl;
import com.pwc.core.framework.processors.web.elements.ButtonInputElementImpl;
import com.pwc.core.framework.processors.web.elements.CheckboxElementImpl;
import com.pwc.core.framework.processors.web.elements.ComboBoxElementImpl;
import com.pwc.core.framework.processors.web.elements.HeadingElementImpl;
import com.pwc.core.framework.processors.web.elements.IconElementImpl;
import com.pwc.core.framework.processors.web.elements.ImageElementImpl;
import com.pwc.core.framework.processors.web.elements.ListItemElementImpl;
import com.pwc.core.framework.processors.web.elements.MatSelectableElementImpl;
import com.pwc.core.framework.processors.web.elements.PDropDownElementImpl;
import com.pwc.core.framework.processors.web.elements.RadioButtonElementImpl;
import com.pwc.core.framework.processors.web.elements.SelectableElementImpl;
import com.pwc.core.framework.processors.web.elements.SpanElementImpl;
import org.openqa.selenium.WebElement;

public class MouseActivityProcessor {

    private static MouseActivityProcessor instance = new MouseActivityProcessor();

    private MouseActivityProcessor() {
    }

    public static MouseActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(WebElement webElement) {
        return CheckboxElementImpl.applies(webElement) ||
                RadioButtonElementImpl.applies(webElement) ||
                ButtonElementImpl.applies(webElement) ||
                ButtonInputElementImpl.applies(webElement) ||
                AnchorElementImpl.applies(webElement) ||
                SelectableElementImpl.applies(webElement) ||
                MatSelectableElementImpl.applies(webElement) ||
                IconElementImpl.applies(webElement) ||
                ImageElementImpl.applies(webElement) ||
                ComboBoxElementImpl.applies(webElement) ||
                ListItemElementImpl.applies(webElement) ||
                SpanElementImpl.applies(webElement) ||
                HeadingElementImpl.applies(webElement) ||
                PDropDownElementImpl.applies(webElement);
    }

    public void webAction(WebElement webElement, Object value) {
        if (CheckboxElementImpl.applies(webElement)) {
            CheckboxElementImpl checkboxElement = new CheckboxElementImpl();
            checkboxElement.webAction(webElement, value);
        } else if (RadioButtonElementImpl.applies(webElement)) {
            RadioButtonElementImpl radioButtonElement = new RadioButtonElementImpl();
            radioButtonElement.webAction(webElement, value);
        } else if (ButtonElementImpl.applies(webElement)) {
            ButtonElementImpl buttonElement = new ButtonElementImpl();
            buttonElement.webAction(webElement, value);
        } else if (ButtonInputElementImpl.applies(webElement)) {
            ButtonInputElementImpl buttonInputElement = new ButtonInputElementImpl();
            buttonInputElement.webAction(webElement, value);
        } else if (AnchorElementImpl.applies(webElement)) {
            AnchorElementImpl anchorElement = new AnchorElementImpl();
            anchorElement.webAction(webElement, value);
        } else if (SelectableElementImpl.applies(webElement)) {
            SelectableElementImpl selectableElement = new SelectableElementImpl();
            selectableElement.webAction(webElement, value);
        } else if (MatSelectableElementImpl.applies(webElement)) {
            MatSelectableElementImpl matSelectableElement = new MatSelectableElementImpl();
            matSelectableElement.webAction(webElement, value);
        } else if (IconElementImpl.applies(webElement)) {
            IconElementImpl iconElement = new IconElementImpl();
            iconElement.webAction(webElement, value);
        } else if (ImageElementImpl.applies(webElement)) {
            ImageElementImpl imageElement = new ImageElementImpl();
            imageElement.webAction(webElement, value);
        } else if (ListItemElementImpl.applies(webElement)) {
            ListItemElementImpl listItemElement = new ListItemElementImpl();
            listItemElement.webAction(webElement, value);
        } else if (ComboBoxElementImpl.applies(webElement)) {
            ComboBoxElementImpl comboBoxElement = new ComboBoxElementImpl();
            comboBoxElement.webAction(webElement, value);
        } else if (SpanElementImpl.applies(webElement)) {
            SpanElementImpl spanElement = new SpanElementImpl();
            spanElement.webAction(webElement, value);
        } else if (HeadingElementImpl.applies(webElement)) {
            HeadingElementImpl headingElement = new HeadingElementImpl();
            headingElement.webAction(webElement, value);
        } else if (PDropDownElementImpl.applies(webElement)) {
            PDropDownElementImpl pDropDownElement = new PDropDownElementImpl();
            pDropDownElement.webAction(webElement, value);
        }
    }

}
