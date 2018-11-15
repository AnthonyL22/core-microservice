package com.pwc.core.framework;

public final class JavascriptConstants {

    // ALERT Actions
    public static final String ALERT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; alert(element.textContent);";

    // BLUR Actions
    public static final String BLUR_ELEMENT_BY_XPATH = "(document.evaluate('%s', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;).blur();";
    public static final String BLUR_ELEMENT_BY_ID = "document.getElementById('%s').blur();";

    // TEXT ENTRY Action
    public static final String GET_ELEMENT_TEXT_JAVASCRIPT = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; return element.innerText";
    public static final String ENTER_VALUE_ELEMENT_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; element.value='%s';";

    // SELECT/COMBO Actions
    public static final String SELECT_VALUE_BY_XPATH = ENTER_VALUE_ELEMENT_BY_XPATH + " for(var i=0; i < element.options.length; i++){if(element.options[i].text === \"%s\") {element.selectedIndex = i; break;}};";
    public static final String SELECT_VALUE_BY_IDENTIFIER_IDENTIFIER_VALUE_EXT_COMBO = "Ext.ComponentQuery.query('combo#%s')[0].setValue(Ext.ComponentQuery.query('combo#%s')[0].getStore().findRecord('name','%s'))";
    public static final String SELECT_VALUE_BY_IDENTIFIER_INDEX_IDENTIFIER_EXT_COMBO = "Ext.ComponentQuery.query('combo#%s')[%s].setValue(Ext.ComponentQuery.query('combo#%s')[%s].getStore().findRecord('name','%s'))";

    // CLICK Actions
    public static final String CLICK_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; element.click();";
    public static final String DOUBLE_CLICK_BY_XPATH = "var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; var clickEvent = document.createEvent('MouseEvents'); clickEvent.initEvent('dblclick', true, true); element.dispatchEvent (clickEvent)";
    public static final String CLICK_ELEMENT_BY_ID = "document.getElementById('%s').click();";
    public static final String CLICK_BY_TAG_AND_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].value == '%s') elements[i].click();}";
    public static final String CLICK_BY_TAG_NODE_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.nodeValue == '%s') elements[i].click();}";
    public static final String CLICK_BY_TAG_TYPE_AND_EQUALS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent == '%s') elements[i].click()}";
    public static final String CLICK_BY_TAG_TYPE_AND_CONTAINS_VALUE = "var elements = document.getElementsByTagName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    public static final String CLICK_BY_CLASS_NAME_AND_CONTAINS_VALUE = "var elements = document.getElementsByClassName('%s'); " +
            "for (var i=0; i<elements.length; i++) " +
            "{if (elements[i].firstChild.textContent.indexOf('%s') > -1) elements[i].click();}";
    public static final String CLICK_LAST_ELEMENT_BY_CLASS_NAME = "var elements = document.getElementsByClassName('%s'); elements[elements.length - 1].click();";

    // LOGICAL Queries
    public static final String IS_JQUERY_AJAX_REQUESTS_ACTIVE = "if($.active == 0){ return true;} else { return false;}";
    public static final String IS_DOCUMENT_READY = "if(document.readyState == 'complete'){ return true;} else { return false;}";
    public static final String LIST_HTTP_RESOURCES = "var resourceList = []; var entries = window.performance.getEntriesByType('resource'); entries.forEach(function (resource) { resourceList.push(resource.name);}); return(resourceList);";

    // Application Values
    public static final String GET_LOCAL_STORAGE_VALUE_BY_ITEM_KEY = "var key = window.localStorage.getItem(\"%s\"); return key";

    // NAVIGATION Actions
    public static final String SCROLL_TO_ELEMENT_ACTION = "arguments[0].scrollIntoView(true);";
    public static final String SCROLL_TO_TOP_OF_WINDOW_ACTION = "window.scrollTo(document.body.scrollHeight,0);";
    public static final String SCROLL_TO_BOTTOM_OF_WINDOW_ACTION = "window.scrollTo(0,document.body.scrollHeight);";
    public static final String URL_REDIRECT_ACTION = "window.location.replace('%s')";

    private JavascriptConstants() {
    }

}
