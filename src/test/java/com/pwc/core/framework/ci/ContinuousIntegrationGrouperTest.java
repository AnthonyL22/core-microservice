package com.pwc.core.framework.ci;

import com.pwc.core.framework.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ContinuousIntegrationGrouperTest {

    private static final String CANONICAL_CLASS_NAME = "com.pwc.core.framework.ci.UnitTestGroups";
    private static final String OUTPUT_FILE = "group.properties";
    private static String destinationPropertiesFileLocation;
    private static String[] fullyDecoratedArgs = new String[3];

    @Before
    public void setUp() {

        URL url = UnitTestGroups.class.getClassLoader().getResource("");
        File resourceDirectory = new File(url.getPath());
        destinationPropertiesFileLocation = resourceDirectory.getPath();
        fullyDecoratedArgs[0] = CANONICAL_CLASS_NAME;
        fullyDecoratedArgs[1] = destinationPropertiesFileLocation;
        fullyDecoratedArgs[2] = OUTPUT_FILE;
    }

    @Test(expected = NullPointerException.class)
    public void nullMainTest() throws Exception {

        ContinuousIntegrationGrouper.main(null);
    }

    @Test(expected = Exception.class)
    public void canonicalNameArgumentOnlyMainTest() throws Exception {

        String[] args = {CANONICAL_CLASS_NAME};
        ContinuousIntegrationGrouper.main(args);
    }

    @Test
    public void defaultOutputFileMainTest() throws Exception {

        String[] args = {CANONICAL_CLASS_NAME, destinationPropertiesFileLocation};
        ContinuousIntegrationGrouper.main(args);
        URL url = ClassLoader.getSystemClassLoader().getResource("");
        File resourceDirectory = new File(url.getPath());
        File classesDirectory = new File(resourceDirectory.getParent() + FrameworkConstants.SEPARATOR + "classes");
        File defaultFile = FileUtils.getFile(classesDirectory, ContinuousIntegrationGrouper.getDefaultOutputFileName());
        Assert.assertEquals(defaultFile.getName(), ContinuousIntegrationGrouper.getDefaultOutputFileName());
    }

    @Test
    public void canonicalNameAndOutputDirectoryArgumentOnlyMainTest() throws Exception {

        destinationPropertiesFileLocation = StringUtils.replace(destinationPropertiesFileLocation, "%20", " ");
        String[] args = {CANONICAL_CLASS_NAME, destinationPropertiesFileLocation};
        ContinuousIntegrationGrouper.main(args);
        File output = new File(destinationPropertiesFileLocation + FrameworkConstants.SEPARATOR + ContinuousIntegrationGrouper.getDefaultOutputFileName());
        List<String> lines = FileUtils.readLines(output, StandardCharsets.UTF_8);
        Assert.assertTrue(StringUtils.contains(lines.get(0), "input="));
        Assert.assertTrue(StringUtils.contains(lines.get(0), "Test"));
        Assert.assertEquals(lines.size(), 1);
    }

    @Test
    public void allArgumentsMainTest() throws Exception {

        ContinuousIntegrationGrouper.main(fullyDecoratedArgs);
        destinationPropertiesFileLocation = StringUtils.replace(destinationPropertiesFileLocation, "%20", " ");
        File output = new File(destinationPropertiesFileLocation + FrameworkConstants.SEPARATOR + OUTPUT_FILE);
        List<String> lines = FileUtils.readLines(output, StandardCharsets.UTF_8);
        Assert.assertTrue(StringUtils.contains(lines.get(0), "input="));
        Assert.assertTrue(StringUtils.contains(lines.get(0), "Test"));
        Assert.assertEquals(lines.size(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void nullClassNameToDecompileMainTest() throws Exception {

        String[] badArgs = {null, destinationPropertiesFileLocation};
        ContinuousIntegrationGrouper.main(badArgs);
    }

    @Test
    public void writeArrayToFileTest() throws Exception {

        destinationPropertiesFileLocation = org.apache.commons.lang3.StringUtils.replace(destinationPropertiesFileLocation, "%20", " ");
        ContinuousIntegrationGrouper.setDestinationPropertiesFileLocation(destinationPropertiesFileLocation + System.getProperty("file.separator"));
        ContinuousIntegrationGrouper.setOutputFile(OUTPUT_FILE);
        ContinuousIntegrationGrouper.writeArrayToFile(new String[] {"hi,there,now"});
        File output = new File(destinationPropertiesFileLocation + System.getProperty("file.separator") + OUTPUT_FILE);
        List<String> lines = FileUtils.readLines(output, StandardCharsets.UTF_8);
        Assert.assertEquals(lines.size(), 1);
        Assert.assertEquals(lines.get(0), "input=hi,there,now,");
    }

    @Test(expected = FileSystemException.class)
    public void invalidOutputFilesWriteArrayToFileTest() throws Exception {

        ContinuousIntegrationGrouper.setDestinationPropertiesFileLocation("");
        ContinuousIntegrationGrouper.setOutputFile("");
        ContinuousIntegrationGrouper.writeArrayToFile(new String[] {"hi,there,now"});
    }

}
