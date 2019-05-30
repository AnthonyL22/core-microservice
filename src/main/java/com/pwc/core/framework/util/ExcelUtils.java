package com.pwc.core.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.pwc.logging.service.LoggerService.LOG;

public class ExcelUtils {

    /**
     * Read and return all data from a column in the given sheet.
     *
     * @param sheetName        sheet name to read from
     * @param column           starting column to begin reading data (1 based)
     * @param startingRowIndex starting row to begin reading data (1 based)
     * @return complete list of all data read from a column
     */
    public static List<String> readColumnFromSheet(final String directoryName, final String fileName, final String sheetName, final int column, final int startingRowIndex) {

        List<String> hydratedList = new ArrayList<>();
        Object[][] columnValues = getColumnArray(directoryName, fileName, sheetName, column - 1, startingRowIndex - 1);
        if (null != columnValues) {
            for (Object[] columnValue : columnValues) {

                if (null != columnValue[0]) {
                    hydratedList.add(String.valueOf(columnValue[0]));
                }
            }
        }
        return hydratedList;
    }

    /**
     * Read and return all data from an entire row in the given sheet.
     *
     * @param sheetName sheet name to read from
     * @param rowIndex  starting column to begin reading data (1 based)
     * @return complete list of all data read from a row
     */
    public static List<String> readRowFromSheet(final String directoryName, final String fileName, final String sheetName, final int rowIndex) {

        List<String> hydratedList = new ArrayList<>();
        Object[][] rowValues = getRowArray(directoryName, fileName, sheetName, rowIndex - 1);
        if (null != rowValues) {
            for (Object[] rowValue : rowValues) {

                if (null != rowValue[0]) {
                    hydratedList.add(String.valueOf(rowValue[0]));
                }
            }
        }
        return hydratedList;
    }

    /**
     * Remove all duplicate values from a List.
     *
     * @param sourceListThatContainsDuplicates source list that may contain duplicate values
     * @return list with only unique values
     */
    public static List<Object> removeDuplicates(List<Object> sourceListThatContainsDuplicates) {

        List<Object> listWithoutDuplicates = sourceListThatContainsDuplicates.stream().distinct().collect(Collectors.toList());
        return listWithoutDuplicates;
    }

    /**
     * Read .xls row data
     *
     * @param fileName    file name to read
     * @param sheetName   sheet name to interrogate
     * @param startingRow 0 starting row index to read
     * @return data provider array
     */
    private static Object[][] getRowArray(final String directoryName, final String fileName, final String sheetName, final int startingRow) {

        String[][] tabArray = null;
        try {

            File file = PropertiesUtils.getFileFromResources(directoryName + fileName);
            FileInputStream excelFile = new FileInputStream(file);
            XSSFWorkbook excelWBook = new XSSFWorkbook(excelFile);
            XSSFSheet excelWorksheet = excelWBook.getSheet(sheetName);

            if (null == excelWorksheet) {
                LOG(true, "*** PLEASE check the sheet named '%s'", sheetName);
            } else {

                int rowIndex;
                int innerCellIndex;
                int totalNumberOfColumnsWithData = excelWorksheet.getRow(startingRow).getLastCellNum();

                tabArray = new String[totalNumberOfColumnsWithData][1];

                rowIndex = 0;
                for (int i = 0; i <= totalNumberOfColumnsWithData; i++, rowIndex++) {
                    innerCellIndex = 0;
                    String rowValue = getCellData(excelWorksheet, startingRow, i);
                    if (org.apache.commons.lang3.StringUtils.isNoneEmpty(rowValue)) {
                        tabArray[rowIndex][innerCellIndex] = rowValue;
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
     * @param fileName       file name to read
     * @param sheetName      sheet name to interrogate
     * @param startingColumn 0 starting column index to read
     * @param startingRow    0 starting row index to read
     * @return data provider array
     */
    private static Object[][] getColumnArray(final String directoryName, final String fileName, final String sheetName, final int startingColumn, final int startingRow) {

        String[][] tabArray = null;

        try {

            File file = PropertiesUtils.getFileFromResources(directoryName + fileName);
            FileInputStream excelFile = new FileInputStream(file);
            XSSFWorkbook excelWBook = new XSSFWorkbook(excelFile);
            XSSFSheet excelWorksheet = excelWBook.getSheet(sheetName);

            if (null == excelWorksheet) {
                LOG(true, "*** PLEASE check the sheet named '%s'", sheetName);
            } else {

                int rowIndex;
                int innerCellIndex;
                int totalRows = excelWorksheet.getPhysicalNumberOfRows();

                tabArray = new String[totalRows][1];

                rowIndex = 0;
                for (int i = startingRow; i <= totalRows; i++, rowIndex++) {
                    innerCellIndex = 0;
                    String columnValue = getCellData(excelWorksheet, i, startingColumn);
                    if (StringUtils.isNoneEmpty(columnValue)) {
                        tabArray[rowIndex][innerCellIndex] = columnValue;
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
    private static String getCellData(final XSSFSheet excelWorksheet, final int rowNumber, int columnNumber) {
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

}
