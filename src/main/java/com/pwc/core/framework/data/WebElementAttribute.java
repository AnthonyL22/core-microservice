package com.pwc.core.framework.data;


public enum WebElementAttribute {

    ACCESS_KEY("accesskey"),
    ACTION("action"),
    AUTOCOMPLETE("autocomplete"),
    CELL_PADDING("cellpadding"),
    CHECKED("checked"),
    CLASS("class"),
    CLASS_ROLE("class role"),
    COLSPAN("colspan"),
    CONTENT_EDITABLE("contenteditable"),
    CONTEXT_MENU("contextmenu"),
    DATA_SELECTOR("data-selector"),
    DIR("dir"),
    DISABLED("disabled"),
    DRAGGABLE("draggable "),
    DROPZONE("dropzone "),
    HIDDEN("hidden"),
    HREF("href"),
    ID("id"),
    LANG("lang"),
    LINKTO("linkto"),
    NAME("name"),
    PLACEHOLDER("placeholder"),
    READONLY("readonly"),
    ROLE("role"),
    SELECTED("selected"),
    SIZE("size"),
    SPELLCHECK("spellcheck"),
    SRC("src"),
    STROKE("stroke"),
    STYLE("style"),
    TAB_INDEX("tabindex"),
    TARGET("target"),
    TEXT("text"),
    TITLE("title"),
    TRANSLATE("translate"),
    TYPE("type"),
    UNSELECTABLE("unselectable"),
    VALIGN("valign"),
    VALUE("value");

    public String attribute;

    WebElementAttribute(String a) {
        attribute = a;
    }

}
