package com.pwc.core.framework.controller;

import com.jayway.restassured.path.json.JsonPath;
import com.pwc.core.framework.data.TestExecute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JiraControllerTest {

    private static final HashMap BASE_CYCLE = new HashMap();
    private static final String JIRA_KEY = "ANT-36566";
    private static final String CYCLE_ID = "9420";
    private static final String TEST_CYCLE_NAME = "Regression-1.0.0";
    private static final String PROJECT_ID = "12400";
    private static final String BASE_URL = "http://jira.pwc.com/rest/";
    private static final String REPORT_JIRA_TEST_RESULT_PAYLOAD = "{\"executionId\":\"123\",\"issueId\":\"ANT-36566\",\"versionId\":\"-1\",\"cycleId\":\"9420\",\"projectId\":\"12400\",\"status\":0}";
    private static final JsonPath EXPECTED_JIRA_TEST_RESULT_RESPONSE = new JsonPath("{\"httpStatusValue\":200}");

    private JiraController mockJiraController;
    private TestExecute testExecute;
    private ITestResult mockITestResult;
    private ITestNGMethod mockITestNGMethod;

    @Before
    public void setUp() {

        mockJiraController = mock(JiraController.class, Mockito.CALLS_REAL_METHODS);
        when(mockJiraController.executePut("/zapi/latest/execution/123/execute", REPORT_JIRA_TEST_RESULT_PAYLOAD)).thenReturn(EXPECTED_JIRA_TEST_RESULT_RESPONSE);

        mockJiraController.setCycleName(TEST_CYCLE_NAME);
        mockJiraController.setJiraUrl(BASE_URL);
        mockJiraController.setJiraUsername("admin");
        mockJiraController.setJiraPassword("password");
        mockJiraController.setJiraEnabled(true);

        BASE_CYCLE.put("cycleId", CYCLE_ID);
        BASE_CYCLE.put("assigneeUserName", "anthony");
        BASE_CYCLE.put("assigneeDisplay", "anthony");
        BASE_CYCLE.put("projectId", 12400);
        BASE_CYCLE.put("versionId", "1");

        testExecute = new TestExecute.Builder() //
                        .setExecutionId("123") //
                        .setIssueId(JIRA_KEY) //
                        .setCycleId(CYCLE_ID) //
                        .setProjectId(PROJECT_ID) //
                        .setVersionId("-1") //
                        .build();

        ITestClass mockITestClass = mock(ITestClass.class);
        when(mockITestClass.getTestName()).thenReturn("[TestClass name=class com.pwc.automation.tests.web.BasicTest]");

        mockITestResult = mock(ITestResult.class);
        mockITestNGMethod = mock(ITestNGMethod.class);
        when(mockITestResult.getName()).thenReturn("UnitTestCase");

        when(mockITestNGMethod.getDescription()).thenReturn("MYSTORY-777");
        when(mockITestNGMethod.getTestClass()).thenReturn(mockITestClass);

    }

    @Test
    public void reportJiraResultTest() {
        mockJiraController.reportJiraResult(mockITestResult, testExecute, JIRA_KEY);
    }

    @Test
    public void getJiraStoryIdTest() {
        String jiraIdFound = mockJiraController.getJiraStoryId(JIRA_KEY);
        Assert.assertEquals("Verify Jira Issue ID", null, jiraIdFound);
    }

    @Test
    public void getTestCycleByNameTest() {
        HashMap cycleFound = mockJiraController.getTestCycleByName(TEST_CYCLE_NAME);
        Assert.assertEquals("Verify Test Cycle", null, cycleFound);
    }

    @Test(expected = Exception.class)
    public void includeTestInCycleTest() {
        TestExecute liveTestExecute = mockJiraController.includeTestInCycle(JIRA_KEY, BASE_CYCLE);
        Assert.assertEquals("Verify Test Execute", testExecute, liveTestExecute);
    }

}
