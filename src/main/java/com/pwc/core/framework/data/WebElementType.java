package com.pwc.core.framework.data;


public enum WebElementType {

    DEFINITION_LIST("dl"),
    DEFINITION_TERM("dt"),
    DEFINITION_DESCRIPTION("dd"),
    FIGURE("figure"),
    FIG_CAPTION("figcaption"),
    SEARCH("search"),
    TEXT("text"),
    DATE("date"),
    EMAIL("email"),
    PASSWORD("password"),
    ICON("i"),
    IMG("img"),
    ANCHOR("a"),
    TABLE("table"),
    TBODY("tbody"),
    THEAD("thead"),
    TH("th"),
    TR("tr"),
    TD("td"),
    FILE("file"),
    FORM("form"),
    UL("ul"),
    LI("li"),
    FOOTER("footer"),
    ASIDE("aside"),
    NAV("nav"),
    COMBOBOX("combobox"),
    CHECKBOX("checkbox"),
    RADIO("radio"),
    LEGEND("legend"),
    FULL_HEADER("header"),
    BODY("body"),
    HEAD("head"),
    FIELDSET("fieldset"),
    SCRIPT("script"),
    DIV("div"),
    SPAN("span"),
    OPTION("option"),
    BUTTON("button"),
    SUBMIT("submit"),
    INPUT("input"),
    TEXTAREA("textarea"),
    SELECT("select"),
    LABEL("label"),
    HEADER("^h\\d{1}$");

    public String type;

    WebElementType(String t) {
        type = t;
    }

}
