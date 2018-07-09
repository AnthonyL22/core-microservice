package com.pwc.core.framework.util;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.pwc.logging.service.LoggerService.LOG;

public class FileUtils {

    /**
     * Create new, empty file in the target/test-classes directory in your local workspace
     *
     * @param fileName file name
     */
    public static void createFile(final String fileName) {
        createFile(fileName, "");
    }

    /**
     * Create new file in the target/test-classes directory in your local workspace
     *
     * @param fileName file name
     * @param content  content to write to disk
     */
    public static void createFile(final String fileName, final String content) {
        createFile(null, fileName, content);
    }

    /**
     * Create new file in the specified directory in your local workspace
     *
     * @param file     File to create
     * @param fileName file name
     * @param content  content to write to disk
     */
    public static void createFile(File file, final String fileName, final String content) {

        try {

            if (file == null) {
                URL url = ClassLoader.getSystemClassLoader().getResource("");
                file = new File(url.getPath() + fileName);
            }

            if (file.exists()) {
                file.delete();
            }

            if (!file.exists()) {
                org.apache.commons.io.FileUtils.write(file, content);
                boolean created = file.exists();
                if (created) {
                    LOG(true, "Created file file named='%s'", fileName);
                    return;
                }
            }

        } catch (Exception e) {
            LOG(true, "Unable to create file named='%s'", fileName);
        }

    }

    /**
     * Read any first file found in path matching the file name provided
     *
     * @param fileName file name
     * @return ArrayList of lines read from file
     */
    public static List<String> readFile(final String fileName) {
        return readFile(null, fileName);
    }

    /**
     * Read any first file found in path matching the file name provided
     *
     * @param file     File to read
     * @param fileName file name to read
     * @return ArrayList of lines read from file
     */
    public static List<String> readFile(File file, final String fileName) {
        List<String> lines = new ArrayList<>();

        if (file == null) {
            file = PropertiesUtils.getFileFromResources(fileName);
        }

        try {
            lines = org.apache.commons.io.FileUtils.readLines(file, Charset.defaultCharset());
        } catch (Exception e) {
            LOG(true, "Unable to read file named='%s'", fileName);
        }
        return lines;
    }

    /**
     * Read any first file found in path matching the file provided
     *
     * @param file File to read
     * @return ArrayList of lines read from file
     */
    public static List<String> readFile(File file) {
        List<String> lines = new ArrayList<>();

        try {
            lines = org.apache.commons.io.FileUtils.readLines(file, Charset.defaultCharset());
        } catch (Exception e) {
            LOG(true, "Unable to read file named='%s'", file.getName());
        }
        return lines;
    }

    /**
     * Copy file
     *
     * @param sourceFileName source file to copy
     * @param targetFileName destination file to copy to
     */
    public static void copyFile(final String sourceFileName, final String targetFileName) {

        try {

            File sourceFile = PropertiesUtils.getFileFromResources(sourceFileName);
            URL url = ClassLoader.getSystemClassLoader().getResource("");
            File targetFile = new File(url.getPath() + targetFileName);

            if (sourceFile.exists()) {
                org.apache.commons.io.FileUtils.copyFile(sourceFile, targetFile);
            }
            if (targetFile.exists()) {
                LOG(true, "Source File named='%s' copied to Destination File named='%s'", sourceFileName, targetFileName);
            }
        } catch (Exception e) {
            LOG(true, "Unable to copy file named='%s'", sourceFileName);
        }

    }

    /**
     * Prepend file contents to the beginning of a file.
     *
     * @param fileName         file to prepend to on a new line
     * @param prependOnNewLine add new content on new line or append to existing line
     * @param contentToPrepend contents to prepend
     */
    public static void prependToFile(final String fileName, final String contentToPrepend, final boolean prependOnNewLine) {

        File sourceFile = PropertiesUtils.getFileFromResources(fileName);
        try {
            if (sourceFile.exists()) {
                List<String> originalFileContents = readFile(fileName);
                if (prependOnNewLine) {
                    List<String> prependedList = new LinkedList<>(originalFileContents);
                    prependedList.add(0, contentToPrepend);
                    org.apache.commons.io.FileUtils.writeLines(sourceFile, prependedList, false);
                } else {
                    originalFileContents.set(0, contentToPrepend + originalFileContents.get(0));
                    org.apache.commons.io.FileUtils.writeLines(sourceFile, originalFileContents, false);
                }

                LOG(true, "File named='%s' prepend with content", sourceFile.getName());
            }
        } catch (Exception e) {
            LOG(true, "Unable to prepend file named='%s'", fileName);
        }
    }

    /**
     * Append file contents
     *
     * @param fileName        file to append to
     * @param contentToAppend contents to append
     */
    public static void appendToFile(final String fileName, final String contentToAppend) {

        File sourceFile = PropertiesUtils.getFileFromResources(fileName);
        try {
            if (sourceFile.exists()) {
                org.apache.commons.io.FileUtils.write(sourceFile, contentToAppend, StandardCharsets.UTF_8, true);
                LOG(true, "File named='%s' appended with content", sourceFile.getName());
            }
        } catch (Exception e) {
            LOG(true, "Unable to append file named='%s'", fileName);
        }
    }

    /**
     * Delete file from target dir
     *
     * @param fileName file to delete
     */
    public static void deleteFile(final String fileName) {
        deleteFile(null, fileName);
    }

    /**
     * Delete file from target dir
     *
     * @param file     file to delete
     * @param fileName file name to delete
     */
    public static void deleteFile(File file, final String fileName) {

        if (file == null) {
            file = PropertiesUtils.getFileFromResources(fileName);
        }

        try {
            if (file.exists()) {
                file.delete();

                if (!file.exists()) {
                    LOG(true, "Delete file named='%s'", fileName);
                    return;
                } else {
                    LOG(true, "Unable to delete file named='%s'", fileName);
                }

            }
        } catch (Exception e) {
            LOG(true, "Unable to delete file named='%s'", fileName);
        }

    }

}
