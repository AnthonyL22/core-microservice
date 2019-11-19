package com.pwc.core.framework.controller;

import com.jayway.restassured.path.json.exception.JsonPathException;
import com.pwc.core.framework.data.TestExecute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class JiraControllerTest {

    private static final HashMap BASE_CYCLE = new HashMap();
    private static final String JIRA_KEY = "ANT-36566";
    private static final String CYCLE_ID = "9420";
    private static final String TEST_CYCLE_NAME = "Regression-1.0.0";
    private static final String PROJECT_ID = "12400";
    private static final String BASE_URL = "http://jira.pwc.com/rest/";

    private JiraController jiraController;
    private TestExecute testExecute;

    @Before
    public void setUp() {

        jiraController = new JiraController();

        jiraController.setCycleName(TEST_CYCLE_NAME);
        jiraController.setJiraUrl(BASE_URL);
        jiraController.setJiraUsername("admin");
        jiraController.setJiraPassword("password");
        jiraController.setJiraEnabled(true);

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

    }

    @Test
    public void reportJiraResultTest() {
        jiraController.reportJiraResult(testExecute);
    }

    @Test
    public void getJiraStoryIdTest() {
        String jiraIdFound = jiraController.getJiraStoryId(JIRA_KEY);
        Assert.assertEquals("Verify Jira Issue ID", null, jiraIdFound);
    }

    @Test
    public void getTestCycleByNameTest() {
        HashMap cycleFound = jiraController.getTestCycleByName(TEST_CYCLE_NAME);
        Assert.assertEquals("Verify Test Cycle", null, cycleFound);
    }

    @Test(expected = JsonPathException.class)
    public void includeTestInCycleTest() {
        TestExecute liveTestExecute = jiraController.includeTestInCycle(JIRA_KEY, BASE_CYCLE);
        Assert.assertEquals("Verify Test Execute", testExecute, liveTestExecute);
    }

}
