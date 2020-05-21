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
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ContinuousIntegrationLogExporterTest {

    private static final String TEST_SOURCE_PATH = "src.test.java.com.pwc.core.framework.ci.tests";
    private static final String MAIN_SOURCE_PATH = "src.test.java.com.pwc.core.framework.ci.parent";
    private static final String REPORT_FILE_NAME = "output.txt";
    private static final String TESTNG_GROUP_CLASS_NAME = "Groups.java";
    private static final String TESTNG_GROUP_NAME_TO_FOCUS_ON = "Smoke";
    private static String[] fullyDecoratedArgs = new String[5];

    @Before
    public void setUp() {
        fullyDecoratedArgs[0] = TEST_SOURCE_PATH;
        fullyDecoratedArgs[1] = MAIN_SOURCE_PATH;
        fullyDecoratedArgs[2] = REPORT_FILE_NAME;
        fullyDecoratedArgs[3] = TESTNG_GROUP_CLASS_NAME;
        fullyDecoratedArgs[4] = TESTNG_GROUP_NAME_TO_FOCUS_ON;
    }

    @Test()
    public void mainMinimalNumberOfArgumentsTest() throws Exception {

        ContinuousIntegrationLogExporter.main(new String[] {TEST_SOURCE_PATH, MAIN_SOURCE_PATH, REPORT_FILE_NAME, TESTNG_GROUP_CLASS_NAME, TESTNG_GROUP_NAME_TO_FOCUS_ON});

        File testReportFileWithStatistics = new File(REPORT_FILE_NAME);
        List<String> linesRead = FileUtils.readLines(testReportFileWithStatistics, StandardCharsets.UTF_8);
        Assert.assertTrue(Collections.frequency(linesRead, "  Feature: Smoke Test") > 0);
        Assert.assertTrue(Collections.frequency(linesRead, "    But I go back to the Home page") > 0);

    }

    @Test(expected = Exception.class)
    public void mainTwoArgumentMinimumTest() throws Exception {
        ContinuousIntegrationLogExporter.main(new String[] {TEST_SOURCE_PATH});
    }

    @Test()
    public void getBaseJavaDirectoryTest() {
        StringBuilder result = ContinuousIntegrationLogExporter.getJavaDirectory(TEST_SOURCE_PATH);
        Assert.assertTrue(StringUtils.containsIgnoreCase(result, "src" + File.separator + "test" + File.separator + "java"));
    }

    @Test
    public void getUserDefinedTestSourceDirectoryTest() {
        String result = ContinuousIntegrationLogExporter.getUserDefinedSourceDirectory(TEST_SOURCE_PATH);
        Assert.assertTrue(StringUtils.containsIgnoreCase(result, "src" + File.separator + "test" + File.separator + "java"));
        Assert.assertTrue(StringUtils.containsIgnoreCase(result, StringUtils.replace(TEST_SOURCE_PATH, ".", FrameworkConstants.SEPARATOR)));
    }

    @Test
    public void replaceAllConstantsWithMapValuesTest() {
        String[] trimmedList = new String[] {"home", "TestConstants.USER_NAME"};
        ContinuousIntegrationLogExporter.replaceAllConstantsWithMapValues(trimmedList);
    }

    @Test
    public void replaceAllConstantsWithMapValuesNullConstantsTest() {
        String[] trimmedList = new String[] {"home", "nicole"};
        ContinuousIntegrationLogExporter.setConstants(null);
        ContinuousIntegrationLogExporter.replaceAllConstantsWithMapValues(trimmedList);
    }

    @Test
    public void processArgumentsTest() {

        String simpleLogLine = "        FEATURE(\"Smoke Test\");";
        String complexLogLine = "        GIVEN(\"I am logged in page=%s and authenticated user=%s\", \"home\", \"nicole\");";

        String simpleResult = ContinuousIntegrationLogExporter.processArguments(simpleLogLine);
        Assert.assertEquals("\"Smoke Test\"", simpleResult);
        String complexResult = ContinuousIntegrationLogExporter.processArguments(complexLogLine);
        Assert.assertTrue(complexResult.contains("        GIVEN(\"I am logged in page=home and authenticated user="));

    }

    @Test
    public void appendStatisticsToReportTest() throws Exception {

        StringBuilder sourceDirectory = ContinuousIntegrationLogExporter.getJavaDirectory(TEST_SOURCE_PATH);
        File testReportFile = new File(REPORT_FILE_NAME);
        List<File> files = ContinuousIntegrationLogExporter.generateManualTestOutput(sourceDirectory.toString(), testReportFile);
        ContinuousIntegrationLogExporter.generateReportWithStatistics(testReportFile, files);

        File testReportFileWithStatistics = new File(REPORT_FILE_NAME);
        List<String> linesRead = FileUtils.readLines(testReportFileWithStatistics, StandardCharsets.UTF_8);
        Assert.assertTrue(Collections.frequency(linesRead, "MANUAL TEST CASE REPORT SUMMARY") > 0);

    }

}
