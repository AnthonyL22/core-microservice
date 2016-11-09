package com.pwc.core.framework.processors.web.elements;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

@RunWith(MockitoJUnitRunner.class)
public class IconElementTest extends WebElementBaseTest {

    IconElementImpl iconElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        iconElement = new IconElementImpl();
    }

    @Test
    public void iconElementAppliesTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.ICON, true);
        boolean result = IconElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void iconElementNotAppliesTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.DIV, true);
        boolean result = IconElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.ICON, true);
        iconElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        iconElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
