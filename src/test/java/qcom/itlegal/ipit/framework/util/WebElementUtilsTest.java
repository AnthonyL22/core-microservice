package qcom.itlegal.ipit.framework.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebElementUtilsTest {

    private static final String RAW_WEB_ELEMENT_STANDARD_XPATH = "[[ChromeDriver: chrome on XP (a1bdfd4d8ee42ec6c20dd7a8892fe6da)] -> xpath: //nav[@class='site']]";
    private static final String RAW_WEB_ELEMENT_WILDCARD_XPATH = "[[ChromeDriver: chrome on XP (eebdfd4d8ee42ec6c20dd7a8892fe6da)] -> xpath: //*[text()='Hello World']]";
    private static final String RAW_WEB_ELEMENT_CONTAINS_XPATH = "[[ChromeDriver: chrome on XP (409de32098843654e579c2becc3bda34)] -> xpath: //img[contains(@src, 'logo_home-mast.png')]]";
    private static final String RAW_WEB_ELEMENT_ANCHOR_XPATH = "[[ChromeDriver: chrome on XP (ea22e8e54c541bd34e40228c5e5a1da0)] -> xpath: //a[text()='Submit']]";

    private WebElement mockWebElement;

    @Before
    public void setUp() {
        mockWebElement = mock(WebElement.class);
    }

    @Test
    public void getXPathOfWebElementStandardXPathTest() {

        when(mockWebElement.toString()).thenReturn(RAW_WEB_ELEMENT_STANDARD_XPATH);
        String xPathOfWebElementConversion = WebElementUtils.getXPathOfWebElement(mockWebElement);
        Assert.assertEquals(xPathOfWebElementConversion, "//nav[@class='site']");

    }

    @Test
    public void getXPathOfWebElementWildcardXPathTest() {

        when(mockWebElement.toString()).thenReturn(RAW_WEB_ELEMENT_WILDCARD_XPATH);
        String xPathOfWebElementConversion = WebElementUtils.getXPathOfWebElement(mockWebElement);
        Assert.assertEquals(xPathOfWebElementConversion, "//*[text()='Hello World']");

    }

    @Test
    public void getXPathOfWebElementContainsFunctionXPathTest() {

        when(mockWebElement.toString()).thenReturn(RAW_WEB_ELEMENT_CONTAINS_XPATH);
        String xPathOfWebElementConversion = WebElementUtils.getXPathOfWebElement(mockWebElement);
        Assert.assertEquals(xPathOfWebElementConversion, "//img[contains(@src, 'logo_home-mast.png')]");

    }

    @Test
    public void getXPathOfWebElementAnchorXPathTest() {

        when(mockWebElement.toString()).thenReturn(RAW_WEB_ELEMENT_ANCHOR_XPATH);
        String xPathOfWebElementConversion = WebElementUtils.getXPathOfWebElement(mockWebElement);
        Assert.assertEquals(xPathOfWebElementConversion, "//a[text()='Submit']");

    }


}