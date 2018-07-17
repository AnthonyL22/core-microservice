package com.pwc.core.framework.util;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.pwc.logging.service.LoggerService.LOG;

public class TableUtils {

    private static final String TABLE_BODY_ROW_XPATH = "//tbody//tr";

    /**
     * Get a Cell's data value based on the column and row index
     *
     * @param tableElement     table to interrogate
     * @param rowIndex         row index (zero-based)
     * @param columnIdentifier table's column identifying text
     * @return Cell WebElement
     */
    public static String getWebElementTableText(final WebElement tableElement, int rowIndex, final String columnIdentifier) {

        WebElement cell = null;
        try {
            WebElement row = tableElement.findElements(By.xpath(WebElementUtils.getXPathOfWebElement(tableElement) + TABLE_BODY_ROW_XPATH)).get(rowIndex);
            List<WebElement> cellsInRow = row.findElements(By.tagName(WebElementType.TD.type));
            cell = cellsInRow.get(getColumnIndexBasedOnColumnIdentifier(tableElement, columnIdentifier));
        } catch (IndexOutOfBoundsException e) {
            WebElement row = tableElement.findElements(By.tagName(WebElementType.TR.type)).get(rowIndex);
            cell = row.findElements(By.tagName(WebElementType.TD.type)).get(getColumnIndexBasedOnColumnIdentifier(tableElement, columnIdentifier));
            LOG(false, "exception='%s'", e);
        } catch (Exception e) {
            LOG(false, "exception='%s'", e);
        }

        if (null != cell) {
            return cell.getText();
        } else {
            return "";
        }
    }

    /**
     * Find a possibly dynamic column index based on some textual identifier of a column.  For example the visible column name.
     *
     * @param tableElement     table to interrogate
     * @param columnIdentifier table's column identifying text
     * @return column index for identifier
     */
    public static int getColumnIndexBasedOnColumnIdentifier(final WebElement tableElement, final String columnIdentifier) {

        int columnIndex = -1;
        try {

            List<String> columnNames = new ArrayList<>();
            List<WebElement> headerCellsInTable = tableElement.findElements(By.tagName(WebElementType.TH.type));

            headerCellsInTable.forEach(harvestedColumnWebElement -> {
                if (StringUtils.isNotEmpty(harvestedColumnWebElement.getText())) {
                    columnNames.add(harvestedColumnWebElement.getText());
                }
            });

            columnIndex = columnNames.indexOf(columnIdentifier);
        } catch (Exception e) {
            LOG(true, "Column='%s' not found", columnIdentifier, e);
            return columnIndex;
        }
        return columnIndex;
    }

    /**
     * Fetch the number of table rows in a given table
     *
     * @param tableElement table to interrogate
     * @return number of rows in table
     */
    public static int getWebElementTableRowCount(final WebElement tableElement) {

        int rowCount;
        try {
            rowCount = tableElement.findElements(By.xpath(TABLE_BODY_ROW_XPATH)).size();
            if (rowCount == 0) {
                rowCount = tableElement.findElements(By.tagName(WebElementType.LI.type)).size();
            }
            if (rowCount == 0) {
                rowCount = tableElement.findElements(By.tagName(WebElementType.TR.type)).size();
            }
        } catch (Exception e) {
            LOG(false, "Unable to find table element text for table, Exception='%s'", e);
            rowCount = 0;
        }
        return rowCount;
    }

    /**
     * Return all the visible column values for a given column by a column's unique table header value
     *
     * @param tableElement     table to interrogate
     * @param columnIdentifier unique coluumn identifier for an element
     * @return all values in a column
     */
    public static List<String> readAllColumnValues(final WebElement tableElement, final String columnIdentifier) {

        return readAllColumnValues(tableElement, columnIdentifier, getWebElementTableRowCount(tableElement));
    }

    /**
     * Return the visible column values for a given column by index for a given amount of rows
     *
     * @param tableElement     table to interrogate
     * @param columnIdentifier unique coluumn identifier for an element
     * @param rowCount         number of rows to read
     * @return all values in a column
     */
    public static List<String> readAllColumnValues(final WebElement tableElement, final String columnIdentifier, int rowCount) {

        List<String> columnValuesRead = new ArrayList<>();
        try {
            columnValuesRead = new ArrayList<>();
            int numberOfRowsToRead;
            int currentTableRowCount = getWebElementTableRowCount(tableElement);
            if (rowCount > currentTableRowCount) {
                numberOfRowsToRead = currentTableRowCount;
            } else {
                numberOfRowsToRead = rowCount;
            }

            int columnIndex = getColumnIndexBasedOnColumnIdentifier(tableElement, columnIdentifier);

            for (int i = 1; i <= numberOfRowsToRead; i++) {
                List<WebElement> visibleCellsInRow = tableElement.findElements(By.xpath(WebElementUtils.getXPathOfWebElement(tableElement) + "//tbody//tr[" + i + "]//td"));
                columnValuesRead.add(visibleCellsInRow.get(columnIndex).getText());
            }
        } catch (Exception e) {
            LOG(true, "Unable to read column=%s", columnIdentifier, e);
            return columnValuesRead;
        }
        return columnValuesRead;
    }

}
