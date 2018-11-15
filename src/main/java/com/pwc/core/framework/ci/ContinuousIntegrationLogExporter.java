package com.pwc.core.framework.ci;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.logging.helper.LoggerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static com.pwc.logging.service.LoggerService.LOG;

public class ContinuousIntegrationLogExporter {

    private static final String DEFAULT_TEST_DIR = "src.test.java";
    private static Map constants;

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            throw new Exception("Missing required arguments; " +
                    "[0] = Source Test Directory (ex: com.google.automation.tests), " +
                    "[1] = Manual Test Report Name (ex: Manual_Test_Report.txt), " +
                    "[2...] = OPTIONAL - Classes that contain Constant 'Name=Value' pairs");
        } else {

            String sourceDirectory = getUserDefinedTestSourceDirectory(args[0]);

            File testReportFile = new File(args[1]);
            if (testReportFile.exists()) {
                testReportFile.delete();
            }

            List<String> constantFiles = new ArrayList<>();
            if (args.length > 2) {
                for (int i = 2; i < args.length; i++) {
                    constantFiles.add(args[i]);
                }
                setConstants(getConstantValues(constantFiles));
            }

            List<File> files = generateManualTestOutput(sourceDirectory, testReportFile);
            appendStatisticsToReport(testReportFile, files);

        }

    }

    /**
     * Get constant values for a given Interface file
     *
     * @param constantFiles list of .java files that contain name/value pairs (ex: Constants.java)
     * @return full path to source directory
     */
    protected static Map getConstantValues(final List<String> constantFiles) {

        StringBuilder source = getBaseJavaDirectory();
        Map constantMap = new HashMap();

        try {

            for (String fileName : constantFiles) {

                List<File> fileToScan = (List<File>) FileUtils.listFiles(new File(source.toString()), new SuffixFileFilter(fileName), TrueFileFilter.INSTANCE);

                for (File file : fileToScan) {

                    List<String> readingLines = FileUtils.readLines(file, Charset.defaultCharset());
                    for (String readLine : readingLines) {
                        if (StringUtils.contains(readLine, "=")) {
                            readLine = readLine.trim();
                            String left = StringUtils.substring(readLine, StringUtils.indexOf(readLine, " "), StringUtils.indexOf(readLine, "=")).trim();
                            String right = StringUtils.substring(readLine, StringUtils.indexOf(readLine, "= ") + 1, StringUtils.indexOf(readLine, ";")).trim();
                            right = StringUtils.replace(right, "\"", "");
                            constantMap.put(left, right);
                        }
                    }
                }

            }

        } catch (IOException e) {
            LOG(true, "Error processing constant file(s)");
        }

        return constantMap;

    }

    /**
     * Generate manual test output report
     *
     * @param sourceDirectory source file directory
     * @param testReportFile  destination report File
     * @return list of files processed
     */
    protected static List<File> generateManualTestOutput(String sourceDirectory, File testReportFile) {

        List<File> files = (List<File>) FileUtils.listFiles(new File(sourceDirectory), new SuffixFileFilter("Test.java"), TrueFileFilter.INSTANCE);

        try {
            for (File file : files) {

                List<String> readingLines = FileUtils.readLines(file, Charset.defaultCharset());
                List<String> writeLines = new ArrayList<>();

                for (String readLine : readingLines) {
                    if (StringUtils.contains(readLine, "FEATURE")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("Feature: " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                    if (StringUtils.contains(readLine, "SCENARIO")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("Scenario: " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                    if (StringUtils.contains(readLine, "GIVEN")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("Given " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                    if (StringUtils.contains(readLine, "WHEN")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("When " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                    if (StringUtils.contains(readLine, "THEN")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("Then " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                    if (StringUtils.contains(readLine, "AND")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("And " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                    if (StringUtils.contains(readLine, "BUT")) {
                        writeLines.add(LoggerHelper.formatGherkinMessage("But " + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                    }

                }

                writeLines.add(org.apache.commons.lang.StringUtils.repeat("-", 60));
                writeLines.add("\n");
                FileUtils.writeLines(testReportFile, writeLines, true);

            }
        } catch (IOException e) {
            LOG(true, "Error generating manual test report");
        }
        return files;

    }

    /**
     * Append some admin statistics to the generated reprot
     *
     * @param outputFile report file
     * @param files      files to process
     */
    protected static void appendStatisticsToReport(final File outputFile, final List<File> files) {

        try {
            List<String> adminLines = new ArrayList<>();
            adminLines.add(org.apache.commons.lang.StringUtils.repeat("-", 60));
            adminLines.add("MANUAL TEST CASE REPORT SUMMARY");
            adminLines.add(org.apache.commons.lang.StringUtils.repeat("-", 60));
            adminLines.add(files.size() + " Tests Processed");
            adminLines.add("Packages Processed:");

            Collection<String> uniquePackageNames = new LinkedHashSet<>();

            for (File file : files) {
                List<String> readingLines = FileUtils.readLines(file, Charset.defaultCharset());
                for (String readLine : readingLines) {
                    if (StringUtils.contains(readLine, "package")) {
                        uniquePackageNames.add("  " + readLine);
                    }
                }
            }

            for (String packageName : uniquePackageNames) {
                adminLines.add(packageName);
            }

            FileUtils.writeLines(outputFile, adminLines, true);
        } catch (IOException e) {
            LOG(true, "Error processing statistics report");
        }
    }

    /**
     * Process logging that may contain arguments from String.format()
     *
     * @param readLine line to process
     * @return formatted line
     */
    protected static String processArguments(String readLine) {

        int start = StringUtils.indexOf(readLine, "\"") + 1;

        if (readLine.contains("%")) {

            int end = readLine.length();
            String format = StringUtils.substring(readLine, start, end);

            String sub = StringUtils.substringAfter(format, "\", ");
            sub = StringUtils.remove(sub, "\"");
            sub = StringUtils.remove(sub, ");");
            String[] temp = StringUtils.split(sub, ",");
            String[] trimmedList = new String[temp.length];
            for (int i = 0; i < temp.length; i++) {
                trimmedList[i] = temp[i].trim();
            }

            if (null != trimmedList) {
                replaceAllConstantsWithMapValues(trimmedList);
                return LoggerHelper.formatMessage(readLine, trimmedList);
            }

        } else {
            int end = StringUtils.lastIndexOf(readLine, "\"");
            String format = StringUtils.substring(readLine, start, end);
            return "\"" + format + "\"";
        }

        return readLine;

    }

    /**
     * Updated shared Map with key/value pairs
     *
     * @param trimmedList items
     */
    protected static void replaceAllConstantsWithMapValues(String[] trimmedList) {
        try {
            if (null != getConstants()) {
                for (int i = 0; i < trimmedList.length; i++) {
                    String possibleReplacementArguments = trimmedList[i];
                    if (possibleReplacementArguments.contains(".")) {
                        String keyToFind = StringUtils.substringAfter(possibleReplacementArguments, ".");
                        String value = getConstants().get(keyToFind).toString();
                        trimmedList[i] = value;
                    }
                }
            }

        } catch (Exception e) {
            LOG(true, "Failed to replace Map value(s)=" + trimmedList[0]);
        }
    }

    /**
     * Get Source directory where tests live
     *
     * @param arg source directory
     * @return full path to source directory
     */
    protected static String getUserDefinedTestSourceDirectory(String arg) {

        StringBuilder source = getBaseJavaDirectory();

        if (StringUtils.contains(arg, ".")) {
            arg = StringUtils.replace(arg, ".", FrameworkConstants.SEPARATOR);
        }

        source.append(StringUtils.prependIfMissing(arg, FrameworkConstants.SEPARATOR));
        source.append(FrameworkConstants.SEPARATOR);

        String sourceDirectory = StringUtils.replace(source.toString(), "/", FrameworkConstants.SEPARATOR);
        sourceDirectory = StringUtils.appendIfMissing(sourceDirectory, FrameworkConstants.SEPARATOR);
        sourceDirectory = StringUtils.prependIfMissing(sourceDirectory, FrameworkConstants.SEPARATOR);
        return sourceDirectory;
    }

    /**
     * Get the base src/test/java directory location
     *
     * @return base test calss source code directory
     */
    protected static StringBuilder getBaseJavaDirectory() {
        File currentDirectory = new File(new File(".").getAbsolutePath());
        String base = StringUtils.replace(currentDirectory.getAbsolutePath(), "%20", " ");
        base = StringUtils.removeStart(base, "/");
        base = StringUtils.removeStart(base, FrameworkConstants.SEPARATOR);
        base = StringUtils.replace(base, "/", FrameworkConstants.SEPARATOR);
        base = StringUtils.substringBeforeLast(base, ".");

        StringBuilder source = new StringBuilder(base);
        source.append(StringUtils.replace(DEFAULT_TEST_DIR, ".", FrameworkConstants.SEPARATOR));
        return source;
    }

    public static Map getConstants() {
        return constants;
    }

    public static void setConstants(Map constants) {
        ContinuousIntegrationLogExporter.constants = constants;
    }

}
