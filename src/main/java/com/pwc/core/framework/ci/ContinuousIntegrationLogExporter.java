package com.pwc.core.framework.ci;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.logging.helper.LoggerHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pwc.logging.service.LoggerService.LOG;


public class ContinuousIntegrationLogExporter {

    private static Map constants;
    private static XWPFDocument document;
    private static String testNgGroupClassName;
    private static String testNgGroupName;
    private static final String GHERKIN_REGEX = "FEATURE\\(|SCENARIO\\(|GIVEN\\(|WHEN\\(|THEN\\(|AND\\(|BUT\\(|OR\\(|IF\\(|NOT\\(|FINALLY\\(";
    private static final int EXPECTED_PARAMETER_COUNT = 5;

    /**
     * Maven utility to transform class that has all TestNG groups defined into a comma separated list that can be
     * used in Jenkins jobs.
     *
     * @param args application arguments
     * @throws Exception missing information to run the application
     */
    public static void main(String[] args) throws Exception {

        if (args.length < EXPECTED_PARAMETER_COUNT) {

            StringBuilder exception = new StringBuilder("Missing required arguments;");
            exception.append("[0] = Source Test Directory (ex: src.test.java.com.google.automation.tests)");
            exception.append(", \r\n");
            exception.append("[1] = Source Main Directory (ex: src.main.java.com.google.automation.framework)");
            exception.append(", \r\n");
            exception.append("[2] = Manual Test Report Name (ex: Manual_Test_Report.txt | .doc)");
            exception.append(", \r\n");
            exception.append("[3] = Class that contain TestNG Group identifiers (ex. Groups.java))");
            exception.append(", \r\n");
            exception.append("[4] = Specific group name to process ignoring all others");
            throw new Exception(exception.toString());

        } else {

            StringBuilder sourceTestDirectory = getJavaDirectory(args[0]);
            File reportFile = new File(args[2]);
            if (reportFile.exists()) {
                reportFile.delete();
            }
            testNgGroupClassName = StringUtils.substringBefore(args[3], ".");
            testNgGroupName = args[4].toUpperCase();
            List<File> files = generateManualTestOutput(sourceTestDirectory.toString(), reportFile);
            generateReportWithStatistics(reportFile, files);

        }

    }

    /**
     * Generate manual test output report.
     *
     * @param sourceTestFileDirectory source file directory
     * @param testReportFile          destination report File
     * @return list of files processed
     */
    protected static List<File> generateManualTestOutput(String sourceTestFileDirectory, File testReportFile) {

        List<File> files = (List<File>) FileUtils.listFiles(new File(sourceTestFileDirectory), new SuffixFileFilter("Test.java"), TrueFileFilter.INSTANCE);

        try {
            boolean matchesDesiredGroup = false;
            for (File file : files) {
                List<String> readingLines = FileUtils.readLines(file, Charset.defaultCharset());
                List<String> writeLines = new ArrayList<>();
                for (String readLine : readingLines) {
                    if (StringUtils.containsIgnoreCase(readLine, String.format("%s.%s", testNgGroupClassName, testNgGroupName))) {
                        matchesDesiredGroup = true;
                    }
                    if (StringUtils.substringBefore(readLine, "\"").trim().matches(GHERKIN_REGEX) && matchesDesiredGroup) {
                        Pattern pattern = Pattern.compile(GHERKIN_REGEX);
                        Matcher matcher = pattern.matcher(readLine);
                        if (matcher.find()) {
                            String result = matcher.group(0);
                            if (result.contains("FEATURE(") || result.contains("SCENARIO(") || result.contains("GIVEN(")) {
                                writeLines.add(LoggerHelper.formatGherkinMessage(result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase().replace("(", ": ")
                                                + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                            } else {
                                writeLines.add(LoggerHelper.formatGherkinMessage(result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase().replace("(", " ")
                                                + StringUtils.substringBetween(processArguments(readLine), "\"", "\"")));
                            }
                        }
                    }
                }

                if (matchesDesiredGroup) {
                    matchesDesiredGroup = false;
                    writeLines.add(org.apache.commons.lang.StringUtils.repeat("-", 60));
                    writeLines.add("\n");

                    if (StringUtils.containsIgnoreCase(testReportFile.getName(), ".doc")) {
                        writeWordDoc(testReportFile.getName(), writeLines);
                    } else {
                        FileUtils.writeLines(testReportFile, writeLines, true);
                    }
                }
            }
        } catch (IOException e) {
            LOG(true, "Error generating manual test report");
        }
        return files;

    }

    /**
     * Append some admin statistics to the generated report.
     *
     * @param outputFile report file
     * @param files      files to process
     */
    protected static void generateReportWithStatistics(final File outputFile, final List<File> files) {

        try {
            List<String> adminLines = new ArrayList<>();
            adminLines.add(org.apache.commons.lang.StringUtils.repeat("-", 60));
            adminLines.add("MANUAL TEST CASE REPORT SUMMARY");
            adminLines.add(org.apache.commons.lang.StringUtils.repeat("-", 60));
            adminLines.add(files.size() + " Tests Processed");

            if (StringUtils.containsIgnoreCase(outputFile.getName(), ".doc")) {
                writeWordDoc(outputFile.getName(), adminLines);
            } else {
                FileUtils.writeLines(outputFile, adminLines, true);
            }
        } catch (IOException e) {
            LOG(true, "Error processing statistics report");
        }
    }

    /**
     * Process all logging into a Word document.
     *
     * @param filename Word document to create
     * @param lines    all lines to write to document
     */
    public static void writeWordDoc(final String filename, final List<String> lines) {

        try {
            if (null != document) {
                XWPFParagraph tmpParagraph = document.createParagraph();
                XWPFRun wordRun = tmpParagraph.createRun();
                wordRun.setFontSize(10);
                lines.forEach(line -> {
                    wordRun.setText(line);
                    wordRun.addCarriageReturn();
                });

            } else {
                document = new XWPFDocument();
                XWPFParagraph tmpParagraph = document.createParagraph();
                XWPFRun wordRun = tmpParagraph.createRun();
                wordRun.setFontSize(10);
                lines.forEach(line -> {
                    wordRun.setText(line);
                    wordRun.addCarriageReturn();
                });

            }
            FileOutputStream fos = new FileOutputStream(new File(filename));
            document.write(fos);
            fos.close();
        } catch (IOException e) {
            LOG(true, "Error processing statistics Word Document report");
        }
    }

    /**
     * Process logging that may contain arguments from String.format().
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

            if (CollectionUtils.isEmpty(Arrays.asList(trimmedList))) {
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
     * Updated shared Map with key/value pairs.
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
     * Get Source directory where classes are defined.
     *
     * @param arg source directory
     * @return full path to source directory
     */
    protected static String getUserDefinedSourceDirectory(String arg) {

        StringBuilder source = getJavaDirectory(arg);

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
     * Get the base src/test/java directory location.
     *
     * @param directory base java directory
     * @return base test class source code directory
     */
    protected static StringBuilder getJavaDirectory(final String directory) {

        File currentDirectory = new File(new File(".").getAbsolutePath());
        String base = StringUtils.replace(currentDirectory.getAbsolutePath(), "%20", " ");
        base = StringUtils.replace(base, "/", FrameworkConstants.SEPARATOR);
        base = StringUtils.substringBeforeLast(base, ".");

        StringBuilder source = new StringBuilder(base);
        source.append(StringUtils.replace(directory, ".", FrameworkConstants.SEPARATOR));
        return source;
    }

    public static Map getConstants() {
        return constants;
    }

    public static void setConstants(Map constants) {
        ContinuousIntegrationLogExporter.constants = constants;
    }

}
