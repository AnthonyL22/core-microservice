package qcom.itlegal.ipit.framework.processors.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import qcom.itlegal.ipit.framework.data.WebElementAttribute;
import qcom.itlegal.ipit.framework.data.WebElementType;
import qcom.itlegal.ipit.framework.service.WebElementBaseTest;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardActivityProcessorTest extends WebElementBaseTest {

    public static final String ATTRIBUTE = "my user";
    protected KeyboardActivityProcessor keyboardActivityProcessor;
    private WebElement mockWebElement;

    @Before
    public void setUp() {
        keyboardActivityProcessor = KeyboardActivityProcessor.getInstance();
        mockWebElement = mock(WebElement.class);
        when(mockWebElement.getAttribute(WebElementAttribute.TYPE.attribute)).thenReturn(WebElementType.TEXT.type);
        when(mockWebElement.getTagName()).thenReturn(WebElementType.INPUT.type);
    }

    @Test
    public void webActionTest() {
        keyboardActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(1)).getTagName();
    }

    @Test
    public void webActionNotAppliesTest() {
        when(mockWebElement.getTagName()).thenReturn(WebElementType.DIV.type);
        keyboardActivityProcessor.webAction(mockWebElement, ATTRIBUTE);
        verify(mockWebElement, times(2)).getTagName();
    }

}
