package com.pwc.core.framework;

public interface JavascriptConstants {

    // Javascript Alerts
    String ALERT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; alert(element.textContent);";

    // Javascript Other
    String BLUR_ELEMENT_BY_XPATH = "(document.evaluate('%s', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;).blur();";
    String BLUR_ELEMENT_BY_ID = "document.getElementById('%s').blur();";

    // Javascript Entry
    String ENTER_VALUE_ELEMENT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; element.value='%s';";

    // Javascript Drop Down
    String SELECT_VALUE_ELEMENT_BY_XPATH = ENTER_VALUE_ELEMENT_BY_XPATH + " for(var i=0; i < element.options.length; i++){if(element.options[i].text === \"%s\") {element.selectedIndex = i; break;}};";

    // Javascript Clicks
    String CLICK_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; element.click();";
    String CLICK_ELEMENT_BY_ID = "document.getElementById('%s').click();";
    String CLICK_BY_TAG_AND_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].value == '%s') elements[i].click();}";
    String CLICK_BY_TAG_NODE_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.nodeValue == '%s') elements[i].click();}";
    String CLICK_BY_TAG_TYPE_AND_EQUALS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent == '%s') elements[i].click()}";
    String CLICK_BY_TAG_TYPE_AND_CONTAINS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    String CLICK_BY_CLASS_NAME_AND_CONTAINS_VALUE = "var elements = document.getElementsByClassName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    String CLICK_LAST_ELEMENT_BY_CLASS_NAME = "var elements = document.getElementsByClassName('%s'); elements[elements.length - 1].click();";

    // Javascript Logical Queries
    String IS_JQUERY_AJAX_REQUESTS_ACTIVE = "if($.active == 0){ return true;} else { return false;}";
    String IS_DOCUMENT_READY = "if(document.readyState == 'complete'){ return true;} else { return false;}";
    String IS_OPEN_HTTPS = "return window.openHTTPs";

}
