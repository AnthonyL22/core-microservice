package com.pwc.core.framework.ci;

import com.pwc.core.framework.FrameworkConstants;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.StringWriter;

public class ContinuousIntegrationGrouper {

    private static final String DEFAULT_OUTPUT_FILE_NAME = "extended_output.properties";
    private static String destinationPropertiesFileLocation;
    private static String canonicalClassName;
    private static String outputFile;

    /**
     * Main to be called by exec-maven-plugin Maven plugin
     *
     * @param args build parameters in specific order:
     *             <ul>
     *             <li>1. Canonical Class Name to Decompile</li>
     *             <li>2. Output directory location for resulting file</li>
     *             <li>3. (Optional) Name of resulting file. Default to " + DEFAULT_OUTPUT_FILE_NAME</li>
     *             </ul>
     * @throws Exception class decompile failed
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Missing required two arguments; " +
                    "[0] = Canonical Class Name to Decompile, " +
                    "[1] = Output directory location for resulting file, " +
                    "[2] = (Optional) Name of resulting file. Default to " + DEFAULT_OUTPUT_FILE_NAME);
        } else {
            canonicalClassName = args[0];
            destinationPropertiesFileLocation = StringUtils.appendIfMissing(args[1], FrameworkConstants.SEPARATOR);
            outputFile = args.length < 3 ? DEFAULT_OUTPUT_FILE_NAME : args[2];
            String[] stringsArray = stripStringsFromClass();
            writeArrayToFile(stringsArray);
        }
    }

    /**
     * Write an <code>Array</code> of strings to file
     *
     * @param commaArrayVals array of String values to write to file
     * @throws Exception exception
     */
    public static void writeArrayToFile(String[] commaArrayVals) throws Exception {
        try {

            destinationPropertiesFileLocation = StringUtils.replace(destinationPropertiesFileLocation, "%20", " ");
            File propertiesFile = new File(destinationPropertiesFileLocation + outputFile);
            if (propertiesFile.exists()) {
                propertiesFile.delete();
            }

            FileUtils.writeStringToFile(propertiesFile, "input=");
            for (String commaArrayVal : commaArrayVals) {
                FileUtils.writeStringToFile(propertiesFile, String.format("%s,", commaArrayVal), true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Extract all <code>Strings</code> from a class to a <code>Array</code>
     *
     * @return array of Strings from the given class
     */
    private static String[] stripStringsFromClass() {

        String OPEN = "\"";
        String CLOSE = "\"";
        String[] stringsArray = new String[0];

        try {

            final StringWriter stringWriter = new StringWriter();
            final DecompilerSettings settings = DecompilerSettings.javaDefaults();
            settings.setIncludeErrorDiagnostics(true);
            try {

                destinationPropertiesFileLocation = StringUtils.replace(destinationPropertiesFileLocation, "%20", " ");
                StringBuilder decompiledFilePropertiesFilePath = new StringBuilder();
                decompiledFilePropertiesFilePath.append(StringUtils.replace(destinationPropertiesFileLocation, "\\", FrameworkConstants.SEPARATOR));
                decompiledFilePropertiesFilePath.append(StringUtils.replace(canonicalClassName, ".", FrameworkConstants.SEPARATOR));
                decompiledFilePropertiesFilePath.append(".class");
                System.out.println(String.format("Decompiled File = %s", decompiledFilePropertiesFilePath.toString()));
                Decompiler.decompile(decompiledFilePropertiesFilePath.toString(), new PlainTextOutput(stringWriter), settings);

            } finally {
                stringsArray = StringUtils.substringsBetween(stringWriter.getBuffer().toString(), OPEN, CLOSE);
                stringWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Found %s matching Strings", stringsArray.length));
        return stringsArray;

    }

    public static String getDefaultOutputFileName() {
        return DEFAULT_OUTPUT_FILE_NAME;
    }

    public static void setDestinationPropertiesFileLocation(String destinationPropertiesFileLocation) {
        ContinuousIntegrationGrouper.destinationPropertiesFileLocation = destinationPropertiesFileLocation;
    }

    public static void setOutputFile(String outputFile) {
        ContinuousIntegrationGrouper.outputFile = outputFile;
    }
}
