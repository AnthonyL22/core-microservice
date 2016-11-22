package com.pwc.core.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.pwc.logging.service.LoggerService.LOG;

public class PropertiesUtils {

    /**
     * Read a file's contents from Resources directory
     *
     * @param fileName File to read each line
     * @return List of lines read from the file
     */
    public static List<String> readResourceFile(final String fileName) {
        File file = PropertiesUtils.getFileFromResources(fileName);
        try {
            return FileUtils.readLines(file);
        } catch (Exception e) {
            LOG(true, "Unable to read file='%s' due to exception='%s'", fileName, e.getMessage());
        }
        return null;
    }

    /**
     * Get a file from the 'resources' directory given the fileName
     *
     * @param fileName FileName to find
     * @return file matching the name
     */
    public static File getFileFromResources(final String fileName) {
        try {
            URL url = PropertiesUtils.class.getClassLoader().getResource(fileName);
            String urlPath = StringUtils.replace(url.getPath(), "%20", " ");
            return new File(urlPath);
        } catch (Exception e) {
            LOG(true, "Unable to find file='%s' due to exception='%s'", fileName, e.getMessage());
        }
        return null;
    }

    /**
     * Get first file's path from the 'resources' directory given the fileName regardless of
     * the casing of the file name.
     *
     * @param fileName FileName to find
     * @return String file path of the file found
     */
    public static String getPathOfResourceFile(final String fileName) {
        try {
            return getFirstFileFromTestResources(fileName).getPath();
        } catch (Exception e) {
            LOG(true, "Unable to find file='%s' due to exception='%s'", fileName, e.getMessage());
        }
        return "";
    }

    /**
     * Get first file from the 'resources' directory given the fileName regardless of
     * the casing of the file name.
     *
     * @param fileName FileName to find
     * @return file matching the name regardless of case
     */
    public static File getFirstFileFromTestResources(final String fileName) {
        URL url = ClassLoader.getSystemClassLoader().getResource("");
        String urlPath = StringUtils.replace(url.getPath(), "%20", " ");
        File resourceDirectory = new File(urlPath);
        List<File> files = (List<File>) FileUtils.listFiles(new File(resourceDirectory.getParent()), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            if (StringUtils.equals(file.getName(), fileName) || StringUtils.containsIgnoreCase(file.getPath(), fileName)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Override the file.getPath() and return empty <code>String</code> as
     * file's path if file is null
     *
     * @param file file to find it's path
     * @return file path
     */
    public static String getPath(final File file) {
        try {
            return file.getPath();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get a property out of a .properties file
     *
     * @param propertiesFileName Properties file to search in
     * @param property           Property to find
     * @return Property defined
     */
    public static String getPropertyFromPropertiesFile(final String propertiesFileName, final String property) {
        return getPropertiesFromPropertyFile(propertiesFileName).getProperty(property);
    }

    /**
     * Get all all propeties in a given .properties file
     *
     * @param propertiesFileName Properties file to find
     * @return All properties in a property file
     */
    public static Properties getPropertiesFromPropertyFile(final String propertiesFileName) {
        Properties props = new Properties();
        try {
            File file = getFileFromResources(propertiesFileName);
            props.load(new FileInputStream(file.getPath()));
        } catch (Exception e) {
            LOG(true, "Unable to get properties file='%s' due to exception='%s'", propertiesFileName, e.getMessage());
            return props;
        }
        return props;
    }

    /**
     * Set an environment variable
     *
     * @param environmentVariable Map of environment variable key and value
     */
    public static void setEnv(Map<String, String> environmentVariable) {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(environmentVariable);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(environmentVariable);
        } catch (NoSuchFieldException e) {
            try {
                Class[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> env = System.getenv();
                for (Class cl : classes) {
                    if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(environmentVariable);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
