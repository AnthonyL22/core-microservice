package com.pwc.core.framework.util;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class JsonUtilsTest {

    private static final String SAMPLE_JSON = "[{\"os\":\"Windows 10\",\"platform\":\"XP\",\"browser\":\"firefox\",\"browser-version\":\"11\",\"long-name\":\"Firefox\",\"long-version\":\"11.0.\",\"url\":\"sauce-ondemand:?os=Windows 10&browser=firefox&browser-version=11&username=myuser-jenkins&access-key=12347900-7f51-4185-a3d7-5d2b413f2efa\"},{\"os\":\"Windows 10\",\"platform\":\"XP\",\"browser\":\"firefox\",\"browser-version\":\"12\",\"long-name\":\"Firefox\",\"long-version\":\"12.0.\",\"url\":\"sauce-ondemand:?os=Windows 10&browser=firefox&browser-version=12&username=myuser-jenkins&access-key=12347900-7f51-4185-a3d7-5d2b413f2efa\"}]";
    private static final List SAMPLE_JSON_LIST = new ArrayList(Arrays.asList("foobar", "testing", "here"));
    private static final Set SAMPLE_JSON_SET = new HashSet(Arrays.asList("foobar", "testing", "here"));

    @Before
    public void setUp() {
    }

    @Test
    public void parseJsonTest() {
        JsonPath result = JsonUtils.parseJson(SAMPLE_JSON);
        List operatingSystems = result.get("os");
        List platforms = result.get("platform");
        Assert.assertTrue(operatingSystems.size() == 2, "Validate OS json parsing");
        Assert.assertEquals(operatingSystems.get(0), "Windows 10", "Validate OS json parsing");
        Assert.assertTrue(platforms.size() == 2, "Validate Platform json parsing");
        Assert.assertEquals(platforms.get(1), "XP", "Validate Platform json parsing");
    }

    @Test
    public void parseJsonAsListTest() {
        List<HashMap> result = JsonUtils.getJsonList(SAMPLE_JSON);
        Assert.assertTrue(result.size() == 2, "Validate JSON List Size");
    }

    @Test
    public void getJSONStringFromListTest() {
        String result = JsonUtils.getJSONString(SAMPLE_JSON_LIST);
        Assert.assertEquals(result, "[\"foobar\",\"testing\",\"here\"]");
    }

    @Test
    public void getJSONStringFromSetTest() {
        String result = JsonUtils.getJSONString(SAMPLE_JSON_SET);
        Assert.assertTrue(result.contains("foobar"));
        Assert.assertTrue(result.contains("testing"));
        Assert.assertTrue(result.contains("here"));
    }

    @Test
    public void getJSONStringFromOtherTest() {
        String result = JsonUtils.getJSONString("foobar");
        Assert.assertEquals(result, "[\"foobar\"]");
    }

    @Test
    public void isValidJSONTest() {
        boolean result = JsonUtils.isJSONValid(SAMPLE_JSON);
        Assert.assertTrue(result);
    }

    @Test
    public void isInvalidOneCharValidJSONTest() {
        boolean result = JsonUtils.isJSONValid("1");
        Assert.assertFalse(result);
    }

    @Test
    public void isInValidJSONTest() {
        boolean result = JsonUtils.isJSONValid("[{\"os\n::owser-version=12&username=my-user&access-key=12345644564-7f51-7f51-4185-a3d7-5d2b413f2efa\"}");
        Assert.assertFalse(result);
    }

}
