package com.pwc.core.framework.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ExcelUtilsTest {

    private static final String EXCEL_RESOURCE_DIRECTORY = "files/";
    private static final String EXCEL_FILE_NAME = "surfer_names.xlsx";
    private static final String VALID_SHEET_NAME = "riders";

    @Before
    public void setUp() {
    }

    @Test
    public void readColumnFromSheetTest() {
        List<String> results = ExcelUtils.readColumnFromSheet(EXCEL_RESOURCE_DIRECTORY, EXCEL_FILE_NAME, VALID_SHEET_NAME, 1, 1);
        Assert.assertEquals(results.size(), 7);
    }

    @Test
    public void readInvalidColumnFromSheetTest() {
        List<String> results = ExcelUtils.readColumnFromSheet(EXCEL_RESOURCE_DIRECTORY, EXCEL_FILE_NAME, "foobar", 1, 1);
        Assert.assertEquals(results.size(), 0);
    }

    @Test
    public void readRowFromSheetTest() {
        List<String> results = ExcelUtils.readRowFromSheet(EXCEL_RESOURCE_DIRECTORY, EXCEL_FILE_NAME, VALID_SHEET_NAME, 3);
        Assert.assertEquals(results.get(0), "Lacy");
        Assert.assertEquals(results.get(1), "Matanzas");
    }

    @Test
    public void readInvalidRowFromSheetTest() {
        List<String> results = ExcelUtils.readRowFromSheet(EXCEL_RESOURCE_DIRECTORY, EXCEL_FILE_NAME, "foobar", 3);
        Assert.assertEquals(results.size(), 0);
    }

    @Test
    public void removeDuplicatesTest() {
        List results = ExcelUtils.readColumnFromSheet(EXCEL_RESOURCE_DIRECTORY, EXCEL_FILE_NAME, VALID_SHEET_NAME, 1, 1);
        List uniqueRiders = ExcelUtils.removeDuplicates(results);
        Assert.assertEquals(uniqueRiders.size(), 6);
    }

}
