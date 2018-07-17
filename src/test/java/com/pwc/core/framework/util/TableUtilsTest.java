package com.pwc.core.framework.util;

import com.pwc.core.framework.data.WebElementAttribute;
import com.pwc.core.framework.data.WebElementType;
import com.pwc.core.framework.service.WebElementBaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TableUtilsTest extends WebElementBaseTest {

    private static final String HOME_TOWN = "Hatfields Beach";

    @Before
    public void setUp() {
    }

    @Test
    public void getWebElementTableTextTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(getMockWebElement().toString()).thenReturn("[[MicroserviceChromeDriver: chrome on XP (283d79e2cdb5139a37a7e1b2303e5af0)] -> xpath: //div[contains(@class, 'datatable')]]");

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr"))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo));

        WebElement cellMockOne = mock(WebElement.class);
        WebElement cellMockTwo = mock(WebElement.class);
        WebElement cellMockThree = mock(WebElement.class);
        when(rowMockOne.findElements(By.tagName(WebElementType.TD.type))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));

        WebElement headerMockOne = mock(WebElement.class);
        WebElement headerMockTwo = mock(WebElement.class);
        WebElement headerMockThree = mock(WebElement.class);
        when(headerMockOne.getText()).thenReturn("ID");
        when(headerMockTwo.getText()).thenReturn("Country");
        when(headerMockThree.getText()).thenReturn("City");
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(Arrays.asList(headerMockOne, headerMockTwo, headerMockThree));

        when(cellMockThree.getText()).thenReturn(HOME_TOWN);

        String result = TableUtils.getWebElementTableText(getMockWebElement(), 0, "City");
        Assert.assertEquals(HOME_TOWN, result);

    }

    @Test
    public void getWebElementTableTextMissingTDTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree));

        WebElement cellMockOne = mock(WebElement.class);
        WebElement cellMockTwo = mock(WebElement.class);
        WebElement cellMockThree = mock(WebElement.class);
        when(rowMockOne.findElements(By.tagName(WebElementType.TD.type))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));

        WebElement headerMockOne = mock(WebElement.class);
        WebElement headerMockTwo = mock(WebElement.class);
        WebElement headerMockThree = mock(WebElement.class);
        when(headerMockOne.getText()).thenReturn("ID");
        when(headerMockTwo.getText()).thenReturn("Country");
        when(headerMockThree.getText()).thenReturn("City");
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(Arrays.asList(headerMockOne, headerMockTwo, headerMockThree));

        when(cellMockThree.getText()).thenReturn(HOME_TOWN);

        String result = TableUtils.getWebElementTableText(getMockWebElement(), 0, "City");
        Assert.assertEquals(HOME_TOWN, result);

    }

    @Test
    public void getWebElementTableTextMissingTableElementsTest() {

        String result = TableUtils.getWebElementTableText(null, 0, "City");
        Assert.assertEquals("", result);

    }

    @Test
    public void getColumnIndexBasedOnColumnIdentifierTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement headerMockOne = mock(WebElement.class);
        WebElement headerMockTwo = mock(WebElement.class);
        WebElement headerMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(Arrays.asList(headerMockOne, headerMockTwo, headerMockThree));

        when(headerMockOne.getText()).thenReturn("ID");
        when(headerMockTwo.getText()).thenReturn("Country");
        when(headerMockThree.getText()).thenReturn("City");

        int result = TableUtils.getColumnIndexBasedOnColumnIdentifier(getMockWebElement(), "City");
        Assert.assertEquals(2, result);

    }

    @Test
    public void getColumnIndexBasedOnColumnIdentifierColumnNotFondTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement headerMockOne = mock(WebElement.class);
        WebElement headerMockTwo = mock(WebElement.class);
        WebElement headerMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(Arrays.asList(headerMockOne, headerMockTwo, headerMockThree));

        when(headerMockOne.getText()).thenReturn("ID");
        when(headerMockTwo.getText()).thenReturn("Country");
        when(headerMockThree.getText()).thenReturn("City");

        int result = TableUtils.getColumnIndexBasedOnColumnIdentifier(getMockWebElement(), "Name");
        Assert.assertEquals(-1, result);

    }

    @Test
    public void getColumnIndexBasedOnColumnIdentifierExceptionTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenThrow(Exception.class);
        int result = TableUtils.getColumnIndexBasedOnColumnIdentifier(getMockWebElement(), "Name");
        Assert.assertEquals(-1, result);

    }

    @Test
    public void getWebElementTableRowCountTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//tbody//tr"))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree));

        int result = TableUtils.getWebElementTableRowCount(getMockWebElement());
        Assert.assertEquals(3, result);
    }

    @Test
    public void getWebElementTableRowCountLIListTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//tbody//tr"))).thenReturn(new ArrayList<>());

        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree));

        int result = TableUtils.getWebElementTableRowCount(getMockWebElement());
        Assert.assertEquals(3, result);
    }

    @Test
    public void getWebElementTableRowCountTRListTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//tbody//tr"))).thenReturn(new ArrayList<>());
        when(getMockWebElement().findElements(By.tagName(WebElementType.LI.type))).thenReturn(new ArrayList<>());

        when(getMockWebElement().findElements(By.tagName(WebElementType.TR.type))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree));

        int result = TableUtils.getWebElementTableRowCount(getMockWebElement());
        Assert.assertEquals(3, result);
    }

    @Test
    public void getWebElementTableRowCountNoListsTest() {

        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//tbody//tr"))).thenReturn(null);

        when(getMockWebElement().findElements(By.xpath(WebElementType.LI.type))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree));

        int result = TableUtils.getWebElementTableRowCount(getMockWebElement());
        Assert.assertEquals(0, result);
    }

    @Test
    public void readAllColumnValuesTest() {

        when(getMockWebElement().toString()).thenReturn("[[MicroserviceChromeDriver: chrome on XP (283d79e2cdb5139a37a7e1b2303e5af0)] -> xpath: //div[contains(@class, 'datatable')]]");
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement headerMockOne = mock(WebElement.class);
        WebElement headerMockTwo = mock(WebElement.class);
        WebElement headerMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(Arrays.asList(headerMockOne, headerMockTwo, headerMockThree));

        when(headerMockOne.getText()).thenReturn("ID");
        when(headerMockTwo.getText()).thenReturn("Country");
        when(headerMockThree.getText()).thenReturn("City");

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//tbody//tr"))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree));

        WebElement cellMockOne = mock(WebElement.class);
        WebElement cellMockTwo = mock(WebElement.class);
        WebElement cellMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr[1]//td"))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr[2]//td"))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr[3]//td"))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));

        when(cellMockOne.getText()).thenReturn("Wolfie");
        when(cellMockTwo.getText()).thenReturn("Pippa");
        when(cellMockThree.getText()).thenReturn("Lola");

        List<String> result = TableUtils.readAllColumnValues(getMockWebElement(), "City");
        Assert.assertTrue(result.size() == 3);
        Assert.assertEquals(result.get(0), "Lola");
        Assert.assertEquals(result.get(1), "Lola");
        Assert.assertEquals(result.get(2), "Lola");

    }

    @Test
    public void readAllColumnValuesRowCountLessThanVisibleRowCountTest() {

        when(getMockWebElement().toString()).thenReturn("[[MicroserviceChromeDriver: chrome on XP (283d79e2cdb5139a37a7e1b2303e5af0)] -> xpath: //div[contains(@class, 'datatable')]]");
        createMockElement("datatable", WebElementAttribute.ID, WebElementType.TABLE, true);

        WebElement headerMockOne = mock(WebElement.class);
        WebElement headerMockTwo = mock(WebElement.class);
        WebElement headerMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.tagName(WebElementType.TH.type))).thenReturn(Arrays.asList(headerMockOne, headerMockTwo, headerMockThree));

        when(headerMockOne.getText()).thenReturn("ID");
        when(headerMockTwo.getText()).thenReturn("Country");
        when(headerMockThree.getText()).thenReturn("City");

        WebElement rowMockOne = mock(WebElement.class);
        WebElement rowMockTwo = mock(WebElement.class);
        WebElement rowMockThree = mock(WebElement.class);
        WebElement rowMockFour = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//tbody//tr"))).thenReturn(Arrays.asList(rowMockOne, rowMockTwo, rowMockThree, rowMockFour));

        WebElement cellMockOne = mock(WebElement.class);
        WebElement cellMockTwo = mock(WebElement.class);
        WebElement cellMockThree = mock(WebElement.class);
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr[1]//td"))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr[2]//td"))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));
        when(getMockWebElement().findElements(By.xpath("//div[contains(@class, 'datatable')]//tbody//tr[3]//td"))).thenReturn(Arrays.asList(cellMockOne, cellMockTwo, cellMockThree));

        when(cellMockOne.getText()).thenReturn("Wolfie");
        when(cellMockTwo.getText()).thenReturn("Pippa");
        when(cellMockThree.getText()).thenReturn("Lola");

        List<String> result = TableUtils.readAllColumnValues(getMockWebElement(), "City", 5);
        Assert.assertTrue(result.size() == 3);
        Assert.assertEquals(result.get(0), "Lola");
        Assert.assertEquals(result.get(1), "Lola");
        Assert.assertEquals(result.get(2), "Lola");

    }

}
