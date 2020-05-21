package com.pwc.core.framework.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.io.File;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilsTest {

    private static final String SAMPLE_FILE_CONTENTS =
                    "[{\"os\":\"Windows 10\",\"platform\":\"XP\",\"browser\":\"firefox\",\"browser-version\":\"11\",\"long-name\":\"Firefox\",\"long-version\":\"11.0.\",\"url\""
                                    + ":\"sauce-ondemand:?os=Windows 10&browser=firefox&browser-version=11&username=my-user&access-key=12345644564-7f51-4185-a3d7-5d2b413f2efa\"},{\"os\""
                                    + ":\"Windows 10\",\"platform\":\"XP\",\"browser\":\"firefox\",\"browser-version\":\"12\",\"long-name\":\"Firefox\",\"long-version\":\"12.0.\",\"url\""
                                    + ":\"sauce-ondemand:?os=Windows 10&browser=firefox&browser-version=12&username=my-user&access-key=12345644564-7f51-7f51-4185-a3d7-5d2b413f2efa\"}]";

    @Before
    public void setUp() {
    }

    @Test
    public void createNewEmptyFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void createNewFileWithFileNameFromFileObjTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        File file = new File(FILE_NAME);
        FileUtils.createFile(file, FILE_NAME, SAMPLE_FILE_CONTENTS);
        List<String> result = FileUtils.readFile(file, FILE_NAME);
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals(result.get(0), SAMPLE_FILE_CONTENTS);
        FileUtils.deleteFile(file, FILE_NAME);
    }

    @Test
    public void createNewFileFromFileObjTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(6) + ".json";
        File file = new File(FILE_NAME);
        FileUtils.createFile(file, FILE_NAME, SAMPLE_FILE_CONTENTS);
        List<String> result = FileUtils.readFile(file);
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals(result.get(0), SAMPLE_FILE_CONTENTS);
        FileUtils.deleteFile(file, FILE_NAME);
    }

    @Test
    public void createNewFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, SAMPLE_FILE_CONTENTS);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals(result.get(0), SAMPLE_FILE_CONTENTS);
    }

    @Test
    public void createNewFileNullContentsTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, null);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void readFileInvalidFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        List<String> result = FileUtils.readFile(FILE_NAME + ".txt");
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void copyFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        final String TARGET_FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, SAMPLE_FILE_CONTENTS);
        FileUtils.copyFile(FILE_NAME, TARGET_FILE_NAME);
        List<String> result = FileUtils.readFile(TARGET_FILE_NAME);
        Assert.assertTrue(result.size() == 1);
    }

    @Test
    public void copyInvalidFileTest() {
        final String TARGET_FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.copyFile(TARGET_FILE_NAME, TARGET_FILE_NAME);
        List<String> result = FileUtils.readFile(TARGET_FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void prependFileOnNewLineTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, SAMPLE_FILE_CONTENTS);
        FileUtils.prependToFile(FILE_NAME, "New First Paragraph", true);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue(result.get(0).contains("New First Paragraph"));
        Assert.assertTrue(result.get(1).contains(SAMPLE_FILE_CONTENTS));
    }

    @Test
    public void prependFileOnOneLineTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, SAMPLE_FILE_CONTENTS);
        FileUtils.prependToFile(FILE_NAME, "New First Words", false);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get(0).contains("New First Words"));
        Assert.assertTrue(result.get(0).contains(SAMPLE_FILE_CONTENTS));
    }

    @Test
    public void appendFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, SAMPLE_FILE_CONTENTS);
        FileUtils.appendToFile(FILE_NAME, "second paragraph");
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get(0).contains(SAMPLE_FILE_CONTENTS));
        Assert.assertTrue(result.get(0).contains("second paragraph"));
    }

    @Test
    public void appendInvalidFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.appendToFile(FILE_NAME, "second paragraph");
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void deleteFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.createFile(FILE_NAME, SAMPLE_FILE_CONTENTS);
        FileUtils.deleteFile(FILE_NAME);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void deleteFileFromCustomFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        File file = new File(FILE_NAME);
        FileUtils.createFile(file, FILE_NAME, SAMPLE_FILE_CONTENTS);
        FileUtils.deleteFile(file, FILE_NAME);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void deleteInvalidFileTest() {
        final String FILE_NAME = RandomStringUtils.randomAlphabetic(5) + ".json";
        FileUtils.deleteFile(FILE_NAME);
        List<String> result = FileUtils.readFile(FILE_NAME);
        Assert.assertTrue(result.size() == 0);
    }

}
