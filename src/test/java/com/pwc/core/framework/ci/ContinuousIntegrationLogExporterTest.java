package com.pwc.core.framework.ci;

import com.pwc.core.framework.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ContinuousIntegrationLogExporterTest {

    final String TEST_SOURCE_PATH = "com.pwc.core.framework.ci.tests";
    final String REPORT_FILE_NAME = "output.txt";
    final String CONSTANT_FILE_ONE = "TestConstants.java";
    final String CONSTANT_FILE_TWO = "Data.java";
    String[] fullyDecoratedArgs = new String[3];

    @Before
    public void setUp() {
        fullyDecoratedArgs[0] = TEST_SOURCE_PATH;
        fullyDecoratedArgs[1] = REPORT_FILE_NAME;
        fullyDecoratedArgs[2] = CONSTANT_FILE_ONE;
        fullyDecoratedArgs[2] = CONSTANT_FILE_TWO;
    }

    @Test()
    public void mainAllArgumentsTest() throws Exception {

        ContinuousIntegrationLogExporter.main(fullyDecoratedArgs);

        File testReportFileWithStatistics = new File(REPORT_FILE_NAME);
        List<String> linesRead = FileUtils.readLines(testReportFileWithStatistics, StandardCharsets.UTF_8);
        Assert.assertTrue(Collections.frequency(linesRead, "  Feature: Smoke Test") > 0);
        Assert.assertTrue(Collections.frequency(linesRead, "    But I go back to the Home page") > 0);
        Assert.assertTrue(Collections.frequency(linesRead, "MANUAL TEST CASE REPORT SUMMARY") > 0);

    }

    @Test()
    public void mainMinimalNumberOfArgumentsTest() throws Exception {

        ContinuousIntegrationLogExporter.main(new String[]{TEST_SOURCE_PATH, REPORT_FILE_NAME});

        File testReportFileWithStatistics = new File(REPORT_FILE_NAME);
        List<String> linesRead = FileUtils.readLines(testReportFileWithStatistics, StandardCharsets.UTF_8);
        Assert.assertTrue(Collections.frequency(linesRead, "  Feature: Smoke Test") > 0);
        Assert.assertTrue(Collections.frequency(linesRead, "    But I go back to the Home page") > 0);

    }

    @Test(expected = Exception.class)
    public void mainTwoArgumentMinimumTest() throws Exception {
        ContinuousIntegrationLogExporter.main(new String[]{TEST_SOURCE_PATH});
    }

    @Test()
    public void getBaseJavaDirectoryTest() throws Exception {
        StringBuilder result = ContinuousIntegrationLogExporter.getBaseJavaDirectory();
        Assert.assertTrue(StringUtils.containsIgnoreCase(result, "src" + File.separator + "test" + File.separator + "java"));
    }

    @Test
    public void getUserDefinedTestSourceDirectoryTest() throws Exception {
        String result = ContinuousIntegrationLogExporter.getUserDefinedTestSourceDirectory(TEST_SOURCE_PATH);
        Assert.assertTrue(StringUtils.containsIgnoreCase(result, "src" + File.separator + "test" + File.separator + "java"));
        Assert.assertTrue(StringUtils.containsIgnoreCase(result, StringUtils.replace(TEST_SOURCE_PATH, ".", FrameworkConstants.SEPARATOR)));
    }

    @Test
    public void replaceAllConstantsWithMapValuesTest() throws Exception {
        String[] trimmedList = new String[]{"home", "TestConstants.USER_NAME"};
        ContinuousIntegrationLogExporter.replaceAllConstantsWithMapValues(trimmedList);
    }

    @Test
    public void replaceAllConstantsWithMapValuesNullConstantsTest() throws Exception {
        String[] trimmedList = new String[]{"home", "TestConstants.USER_NAME"};
        ContinuousIntegrationLogExporter.setConstants(null);
        ContinuousIntegrationLogExporter.replaceAllConstantsWithMapValues(trimmedList);
    }

    @Test
    public void replaceAllConstantsWithMapValuesCompleteTest() throws Exception {

        String[] trimmedList = new String[]{"home", "TestConstants.USER_NAME"};
        List<String> constantFileNames = new ArrayList<>();
        constantFileNames.add("TestConstants.java");
        constantFileNames.add("Data.java");

        Map constantsMap = ContinuousIntegrationLogExporter.getConstantValues(constantFileNames);
        ContinuousIntegrationLogExporter.setConstants(constantsMap);
        ContinuousIntegrationLogExporter.replaceAllConstantsWithMapValues(trimmedList);

    }

    @Test
    public void processArgumentsTest() throws Exception {

        String simpleLogLine = "        FEATURE(\"Smoke Test\");";
        String complexLogLine = "        GIVEN(\"I am logged in page=%s and authenticated user=%s\", \"home\", TestConstants.USER_NAME);";

        String simpleResult = ContinuousIntegrationLogExporter.processArguments(simpleLogLine);
        Assert.assertEquals("\"Smoke Test\"", simpleResult);
        String complexResult = ContinuousIntegrationLogExporter.processArguments(complexLogLine);
        Assert.assertEquals("        GIVEN(\"I am logged in page=home and authenticated user=anthony lombardo\", \"home\", TestConstants.USER_NAME);", complexResult);

    }

    @Test
    public void appendStatisticsToReportTest() throws Exception {

        String sourceDirectory = ContinuousIntegrationLogExporter.getUserDefinedTestSourceDirectory(TEST_SOURCE_PATH);
        File testReportFile = new File(REPORT_FILE_NAME);
        List<File> files = ContinuousIntegrationLogExporter.generateManualTestOutput(sourceDirectory, testReportFile);
        ContinuousIntegrationLogExporter.appendStatisticsToReport(testReportFile, files);

        File testReportFileWithStatistics = new File(REPORT_FILE_NAME);
        List<String> linesRead = FileUtils.readLines(testReportFileWithStatistics, StandardCharsets.UTF_8);
        Assert.assertTrue(Collections.frequency(linesRead, "MANUAL TEST CASE REPORT SUMMARY") > 0);

    }

}
