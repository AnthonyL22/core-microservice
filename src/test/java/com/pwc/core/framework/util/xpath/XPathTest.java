package com.pwc.core.framework.util.xpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

@RunWith(MockitoJUnitRunner.class)
public class XPathTest {

    /**
     * new EmailMessage().from("demo@guilhermechapiewski.com").to(yourAddress)
     * .withSubject("Fluent Mail API")
     * .withAttachment("file_name")
     * .withBody("Demo message").send();
     * <p>
     * XPath().a("div").has("id").equalTo("hello");
     */

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void singleWordSimpleTest() {
        String result = new XPath().div().generate();
        Assert.assertEquals(result, "//div");
    }

    @Test
    public void anchorParameterSimpleTest() {
        String result = new XPath().anchor().generate();
        Assert.assertEquals(result, "//a");
    }

    @Test
    public void withIdEqualsTest() {
        String result = new XPath().div().with("id").equals("hello").generate();
        Assert.assertEquals(result, "//div[@id='hello']");
    }

    @Test
    public void idEqualsTest() {
        String result = new XPath().div().id().equals("hello").generate();
        Assert.assertEquals(result, "//div[@id='hello']");
    }

    @Test
    public void classEqualsTest() {
        String result = new XPath().span().clazz().equals("home").generate();
        Assert.assertEquals(result, "//span[@class='home']");
    }

    @Test
    public void classNotEqualsTest() {
        String result = new XPath().span().clazz().notEquals("home").generate();
        Assert.assertEquals(result, "//span[not(@class='home')]");
    }

    @Test
    public void classContainsEqualsTest() {
        String result = new XPath().span().contains().clazz().is("home").generate();
        Assert.assertEquals(result, "//span[contains(@class, 'home')]");
    }

    @Test
    public void multipleElementsTest() {
        String result = new XPath().div().span().generate();
        Assert.assertEquals(result, "//div//span");
    }

}
