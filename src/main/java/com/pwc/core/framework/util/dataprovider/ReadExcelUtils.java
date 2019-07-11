package com.pwc.core.framework.util.dataprovider;

import com.pwc.core.framework.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.pwc.logging.service.LoggerService.LOG;

public class ReadExcelUtils {

    private static XSSFSheet excelWorksheet;

    /**
     * Read .xls into TestNG data provider multi-dimensional array.
     *
     * @param resourcePath resource path from the /resources dir
     * @param fileName     file name to read
     * @param sheetName    sheet name to interrogate
     * @param totalCols    1 based number of columns to read
     * @return data provider array
     */
    public static Object[][] getTableArray(final String resourcePath, final String fileName, final String sheetName, final int totalCols) {

        String[][] tabArray = null;

        try {

            readExcelWorksheet(resourcePath, fileName, sheetName);

            int startRow = 1;
            int startCol = 0;
            int cellIndex;
            int innerCellIndex;
            int totalRows = excelWorksheet.getLastRowNum();

            tabArray = new String[totalRows][totalCols];
            cellIndex = 0;

            for (int i = startRow; i <= totalRows; i++, cellIndex++) {
                innerCellIndex = 0;
                for (int j = startCol; j <= totalCols; j++, innerCellIndex++) {
                    String columnValue = getCellData(i, j);
                    if (StringUtils.isNoneEmpty(columnValue)) {
                        tabArray[cellIndex][innerCellIndex] = columnValue;
                        LOG(true, "Read spreadsheet value=%s", tabArray[cellIndex][innerCellIndex]);
                    }

                }
            }

        } catch (Exception e) {
            LOG(true, "Could not read the Excel sheet due to exception=%s", e.getMessage());
        }

        return (tabArray);

    }

    /**
     * Read .xls column data
     *
     * @param resourcePath resource path from the /resources dir
     * @param fileName       file name to read
     * @param sheetName      sheet name to interrogate
     * @param startingColumn 0 starting column index to read
     * @param startingRow    0 starting row index to read
     * @return data provider array
     */
    public static Object[][] getColumnArray(final String resourcePath, final String fileName, final String sheetName, final int startingColumn, final int startingRow) {

        String[][] tabArray = null;

        try {

            readExcelWorksheet(resourcePath, fileName, sheetName);

            if (null == excelWorksheet) {
                LOG(true, "*** PLEASE check your Sheet enum for the sheet named '%s'", sheetName);
            } else {

                int rowIndex;
                int innerCellIndex;
                int totalRows = excelWorksheet.getLastRowNum();

                tabArray = new String[totalRows][1];

                rowIndex = 0;
                for (int i = startingRow; i <= totalRows; i++, rowIndex++) {
                    innerCellIndex = 0;
                    String columnValue = getCellData(i, startingColumn);
                    if (StringUtils.isNoneEmpty(columnValue)) {
                        tabArray[rowIndex][innerCellIndex] = columnValue;
                        LOG(false, "Read column value=%s", tabArray[rowIndex][innerCellIndex]);
                    }
                }

            }

        } catch (Exception e) {
            LOG(true, "Could not read the Excel sheet due to exception=%s", e.getMessage());
        }

        return (tabArray);

    }

    /**
     * Read CELL data from spreadsheet.
     *
     * @param rowNumber    row number to read
     * @param columnNumber column number to read
     * @return <code>String</code> value of cell data
     */
    private static String getCellData(int rowNumber, int columnNumber) {
        try {

            XSSFCell cell = excelWorksheet.getRow(rowNumber).getCell(columnNumber);
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    return (cell.getRichStringCellValue().getString());
                case Cell.CELL_TYPE_NUMERIC:
                    Double cellValue = cell.getNumericCellValue();
                    Integer cellIndex = cellValue.intValue();
                    return (String.valueOf(cellIndex));
                default:
                    return cell.getStringCellValue();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Read any Excel file an its internal sheet.
     *
     * @param resourcePath resource path from the /resources dir
     * @param fileName     Excel file to read
     * @param sheetName    shee name to read
     */
    private static void readExcelWorksheet(final String resourcePath, final String fileName, final String sheetName) {

        try {
            File file = PropertiesUtils.getFileFromResources(resourcePath + fileName);
            FileInputStream excelFile = new FileInputStream(file);
            XSSFWorkbook excelWBook = new XSSFWorkbook(excelFile);
            excelWorksheet = excelWBook.getSheet(sheetName);
        } catch (IOException e) {
            LOG(true, "Unable to read Excel sheet named=%s and sheet named=%s", fileName, sheetName);
        }
    }

}
