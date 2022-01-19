package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PropertiesUtilsTest {

    private static final String PROPERTIES_FILE = "automation.properties";
    private static final String WORD_DOCUMENT_FILE = "training.docx";
    private static final int EXPECTED_NUM_OF_PROPERTIES = 24;

    @Before
    public void setUp() {
    }

    @Test
    public void readResourceFileTest() {

        List<String> lines = PropertiesUtils.readResourceFile("config/dev-env/" + PROPERTIES_FILE);
        Assert.assertEquals(EXPECTED_NUM_OF_PROPERTIES, lines.size());
        Assert.assertEquals(lines.get(0), "web.url=http://my-web-application.mywebsite.com");
        Assert.assertEquals(lines.get(1), "web.services.url=http://my-web-services.com");
        Assert.assertEquals(lines.get(2), "web.services.user=foobar");
        Assert.assertEquals(lines.get(3), "web.services.password=foobar");
        Assert.assertEquals(lines.get(4), "enable.hard.assert=false");
        Assert.assertEquals(lines.get(5), "default.wait.for.sleep.millis=1000");
        Assert.assertEquals(lines.get(6), "element.wait.timeout.seconds=180");
        Assert.assertEquals(lines.get(7), "browser.wait.timeout.seconds=10");
        Assert.assertEquals(lines.get(8), "enable.ajax.requests.waiting=true");
        Assert.assertEquals(lines.get(9), "enable.siteMinder=false");
        Assert.assertEquals(lines.get(10), "siteminder.open.url=ping");
        Assert.assertEquals(lines.get(11), "saucelabs.username=saucelabs-user");
        Assert.assertEquals(lines.get(12), "saucelabs.accesskey=12345678-7f51-4185-a3d7-5d2b413f2efa");
        Assert.assertEquals(lines.get(13), "browserstack.username=browserstack-user");
        Assert.assertEquals(lines.get(14), "browserstack.accesskey=AAB1234567rdytP32hoZ");
        Assert.assertEquals(lines.get(15), "browserstack.local=false");
        Assert.assertEquals(lines.get(16), "experitest.accesskey=asdfadsfasdfasdfasdfacC5tIjoiTVRVM");
        Assert.assertEquals(lines.get(17), "capture.video=false");
    }

    @Test
    public void getPathValidFileTest() {

        File file = PropertiesUtils.getFileFromResources("config" + FrameworkConstants.SEPARATOR + "dev-env" + FrameworkConstants.SEPARATOR + PROPERTIES_FILE);
        String path = PropertiesUtils.getPath(file);
        Assert.assertTrue(path.contains("config"));
        Assert.assertTrue(path.contains("dev-env"));
        Assert.assertTrue(path.contains(PROPERTIES_FILE));
    }

    @Test
    public void getPathNullFileTest() {

        String path = PropertiesUtils.getPath(null);
        Assert.assertTrue(StringUtils.isBlank(path));
    }

    @Test
    public void invalidFileNameReadResourceFileTest() {

        List<String> lines = PropertiesUtils.readResourceFile("config/foobar.properties");
        Assert.assertNull(lines);
    }

    @Test
    public void getFileFromResourcesTest() {

        File file = PropertiesUtils.getFileFromResources("config" + FrameworkConstants.SEPARATOR + "dev-env" + FrameworkConstants.SEPARATOR + PROPERTIES_FILE);
        Assert.assertTrue(StringUtils.contains(file.getPath(), PROPERTIES_FILE));
        Assert.assertTrue(StringUtils.contains(file.getPath(), "config"));
    }

    @Test
    public void getPathOfResourceFileTest() {

        String filePath = PropertiesUtils.getPathOfResourceFile("files" + FrameworkConstants.SEPARATOR + WORD_DOCUMENT_FILE);
        Assert.assertTrue(StringUtils.contains(filePath, WORD_DOCUMENT_FILE));
        Assert.assertTrue(StringUtils.contains(filePath, "files"));
    }

    @Test
    public void getPathOfResourceFileInvalidFileNameTest() {

        String filePath = PropertiesUtils.getPathOfResourceFile("filesss" + FrameworkConstants.SEPARATOR + WORD_DOCUMENT_FILE);
        Assert.assertEquals(filePath, "");
    }

    @Test
    public void getFirstFileFromResourcesTest() {

        File file = PropertiesUtils.getFirstFileFromTestResources("dev-env" + FrameworkConstants.SEPARATOR + PROPERTIES_FILE);
        Assert.assertEquals(PROPERTIES_FILE, file.getName());
        Assert.assertTrue(StringUtils.contains(file.getPath(), PROPERTIES_FILE));
        try {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            Assert.assertEquals(EXPECTED_NUM_OF_PROPERTIES, lines.size());
            Assert.assertEquals(lines.get(0), "web.url=http://my-web-application.mywebsite.com");
            Assert.assertEquals(lines.get(1), "web.services.url=http://my-web-services.com");
            Assert.assertEquals(lines.get(2), "web.services.user=foobar");
            Assert.assertEquals(lines.get(3), "web.services.password=foobar");
            Assert.assertEquals(lines.get(4), "enable.hard.assert=false");
            Assert.assertEquals(lines.get(5), "default.wait.for.sleep.millis=1000");
            Assert.assertEquals(lines.get(6), "element.wait.timeout.seconds=180");
            Assert.assertEquals(lines.get(7), "browser.wait.timeout.seconds=10");
            Assert.assertEquals(lines.get(8), "enable.ajax.requests.waiting=true");
            Assert.assertEquals(lines.get(9), "enable.siteMinder=false");
            Assert.assertEquals(lines.get(10), "siteminder.open.url=ping");
            Assert.assertEquals(lines.get(11), "saucelabs.username=saucelabs-user");
            Assert.assertEquals(lines.get(12), "saucelabs.accesskey=12345678-7f51-4185-a3d7-5d2b413f2efa");
            Assert.assertEquals(lines.get(13), "browserstack.username=browserstack-user");
            Assert.assertEquals(lines.get(14), "browserstack.accesskey=AAB1234567rdytP32hoZ");
            Assert.assertEquals(lines.get(15), "browserstack.local=false");
            Assert.assertEquals(lines.get(16), "experitest.accesskey=asdfadsfasdfasdfasdfacC5tIjoiTVRVM");
            Assert.assertEquals(lines.get(17), "capture.video=false");
        } catch (IOException e) {
            Assert.fail("test failed due to exception=" + e.getMessage());
        }
    }

    @Test
    public void fileNotInPathGetFirstFileFromResourcesTest() {

        File file = PropertiesUtils.getFirstFileFromTestResources("foobar.properties");
        Assert.assertNull(file);
    }

    @Test
    public void getValidPropertyFromPropertyFileTest() {

        String property = PropertiesUtils.getPropertyFromPropertiesFile("config/dev-env/" + PROPERTIES_FILE, "enable.hard.assert");
        Assert.assertEquals("false", property);
    }

    @Test
    public void getInvalidPropertyFromPropertyFileTest() {

        String property = PropertiesUtils.getPropertyFromPropertiesFile("config/dev-env/" + PROPERTIES_FILE, "enable.hard.asser");
        Assert.assertNull(property);
    }

    @Test
    public void getPropertiesFromPropertyFileTest() {

        Properties properties = PropertiesUtils.getPropertiesFromPropertyFile("config/dev-env/" + PROPERTIES_FILE);
        Assert.assertEquals(EXPECTED_NUM_OF_PROPERTIES, properties.size());
    }

    @Test
    public void invalidGetPropertiesFromPropertyFileTest() {

        Properties properties = PropertiesUtils.getPropertiesFromPropertyFile("foobar.properties");
        Assert.assertEquals(0, properties.size());
    }

    @Test
    public void setEnvTest() {
        HashMap sauceEnvVariableMap = new HashMap<>();

        sauceEnvVariableMap.put(FrameworkConstants.SAUCELABS_BROWSER_PROPERTY, "chrome");
        sauceEnvVariableMap.put(FrameworkConstants.BROWSER_VERSION_PROPERTY, "26");
        sauceEnvVariableMap.put(FrameworkConstants.PLATFORM_NAME_PROPERTY, "Windows 2003");
        PropertiesUtils.setEnv(sauceEnvVariableMap);
    }

    @Test
    public void setEnvExceptionTest() {

        PropertiesUtils.setEnv(null);
    }

}
