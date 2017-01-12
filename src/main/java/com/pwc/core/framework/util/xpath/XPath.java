package com.pwc.core.framework.util.xpath;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class XPath implements XPathBuilder {

    private List<String> elements = new ArrayList<>();
    private String parameter;
    private String attributeKey;
    private String attributeValue;
    private String operator;
    private String function;

    public XPathBuilder div() {
        this.elements.add(WebElementType.DIV.type);
        return this;
    }

    public XPathBuilder anchor() {
        this.elements.add(WebElementType.ANCHOR.type);
        return this;
    }

    @Override
    public XPathBuilder span() {
        this.elements.add(WebElementType.SPAN.type);
        return this;
    }

    @Override
    public XPathBuilder with(String withThis) {
        this.attributeKey = "@" + withThis;
        return this;
    }

    @Override
    public XPathBuilder is(String isThis) {
        this.attributeValue = isThis;
        this.operator = ", ";
        return this;
    }

    @Override
    public XPathBuilder equals(String equalsThis) {
        this.attributeValue = equalsThis;
        this.operator = "=";
        return this;
    }

    @Override
    public XPathBuilder notEquals(String notEqualsThis) {
        this.attributeValue = notEqualsThis;
        this.function = "not";
        return this;
    }

    @Override
    public XPathBuilder optionalWord() {
        System.out.println("Optional Word");
        return this;
    }

    @Override
    public XPathBuilder id() {
        this.attributeKey = "@id";
        return this;
    }

    @Override
    public XPathBuilder clazz() {
        this.attributeKey = "@class";
        return this;
    }

    @Override
    public XPathBuilder contains() {
        this.function = "contains";
        return this;
    }

    @Override
    public XPathBuilder repeatableWord() {
        System.out.println("Repeatable Word");
        return this;
    }

    @Override
    public String generate() {
        StringBuilder xpath = new StringBuilder();
        if (!elements.isEmpty()) {
            for (String element : elements) {
                xpath.append("//");
                xpath.append(element);
            }
        }
        if (StringUtils.isNotEmpty(parameter)) {
            xpath.append("//");
            xpath.append(parameter);
        }
        if (StringUtils.isNotEmpty(attributeKey)) {
            xpath.append("[");
            if (StringUtils.equalsIgnoreCase(operator, "=")) {
                xpath.append(attributeKey);
            }
        }
        if (StringUtils.isNotEmpty(attributeValue)) {

            if (StringUtils.equalsIgnoreCase(operator, "=")) {
                xpath.append(operator);
                xpath.append("'");
                xpath.append(attributeValue);
                xpath.append("'");
                xpath.append("]");
            } else if (StringUtils.isNotEmpty(function)) {
                xpath.append(function);
                xpath.append("(");
                xpath.append(attributeKey);
                if (StringUtils.equalsIgnoreCase(function, "contains")) {
                    xpath.append(",");
                    xpath.append(" ");
                } else {
                    xpath.append("=");
                }
                xpath.append("'");
                xpath.append(attributeValue);
                xpath.append("'");
                xpath.append(")");
                xpath.append("]");
            } else {
                xpath.append(operator);
                xpath.append("(");
                if (StringUtils.isNotEmpty(attributeKey)) {
                    xpath.append(attributeKey);
                }
                xpath.append("=");
                xpath.append("'");
                xpath.append(attributeValue);
                xpath.append("'");
                xpath.append(")");
                xpath.append("]");
            }

        }
        return xpath.toString();
    }

}
