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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListItemElementTest extends WebElementBaseTest {

    ListItemElementImpl listItemElement;

    @Mock
    WebElement mockWebElement;

    @Before
    public void setUp() {
        listItemElement = new ListItemElementImpl();
    }

    @Test
    public void listItemElementAppliesTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.LI.type);
        boolean result = ListItemElementImpl.applies(mockWebElement);
        Assert.assertTrue(result);
    }

    @Test
    public void listItemElementAppliesUnorderedListTest() {
        when(mockWebElement.getAttribute(WebElementAttribute.ID.attribute)).thenReturn("777");
        when(mockWebElement.getTagName()).thenReturn(WebElementType.UL.type);
        boolean result = ListItemElementImpl.applies(mockWebElement);
        Assert.assertFalse(result);
    }

    @Test
    public void webActionTest() {
        createMockElement("x-boundlist-item", WebElementAttribute.CLASS, WebElementType.LI, true);
        listItemElement.webAction(getMockWebElement(), WebElementAttribute.VALUE.attribute);
    }

    @Test(expected = AssertionError.class)
    public void webActionExceptionTest() {
        listItemElement.webAction(null, WebElementAttribute.VALUE.attribute);
    }

}
