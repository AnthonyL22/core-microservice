package com.pwc.core.framework.util.xpath;


public interface XPathBuilder {

    // single element words
    XPathBuilder div();

    XPathBuilder anchor();

    XPathBuilder span();

    // parametrized words
    XPathBuilder with(String withThis);

    XPathBuilder is(String equalsThis);

    XPathBuilder equals(String equalsThis);

    XPathBuilder notEquals(String notEqualsThis);

    // optional words
    XPathBuilder optionalWord();

    // choice words
    XPathBuilder id();

    XPathBuilder clazz();

    XPathBuilder contains();

    // repeatable words
    XPathBuilder repeatableWord();

    String generate();

}
