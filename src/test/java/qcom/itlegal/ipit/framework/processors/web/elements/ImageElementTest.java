package qcom.itlegal.ipit.framework.processors.web.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;

@RunWith(MockitoJUnitRunner.class)
public class ImageElementTest extends WebElementBaseTest {

    ImageElementImpl imageElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        imageElement = new ImageElementImpl();
    }

    @Test
    public void imageElementAppliesTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.IMG, true);
        boolean result = ImageElementImpl.applies(getMockWebElement());
        Assert.assertTrue(result);
    }

    @Test
    public void imageElementNotAppliesTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.DIV, true);
        boolean result = ImageElementImpl.applies(getMockWebElement());
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("fa fa-home fa-lg", WebElementAttribute.CLASS, WebElementType.IMG, true);
        imageElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        imageElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
