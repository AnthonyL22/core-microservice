package com.pwc.core.framework.controller;

import com.jayway.restassured.path.json.JsonPath;
import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.data.TestCycle;
import com.pwc.core.framework.data.TestExecute;
import com.pwc.core.framework.processors.rest.JiraProcessor;
import com.pwc.core.framework.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static com.pwc.logging.service.LoggerService.LOG;

@Component
public class JiraController extends JiraProcessor {

    private static final String GET_ISSUE_BASE_URL = "/api/2/issue/";
    private static final String ZAPI_EXECUTE_BASE_URL = "/zapi/latest/execution/";
    private static final String ZQL_QUERY_BASE_URL = "/zapi/latest/zql/executeSearch/?zqlQuery=";

    @Value("${jira.zapi.enabled}")
    private boolean jiraEnabled;

    @Value("${jira.zapi.url}")
    private String jiraUrl;

    @Value("${jira.zapi.username}")
    private String jiraUsername;

    @Value("${jira.zapi.password}")
    private String jiraPassword;

    @Value("${jira.zapi.cycle.name}")
    private String cycleName;

    /**
     * Get fully hydrated Jira story details.
     *
     * @param issueKey Jira issue key (ex: PROJECT-12345)
     * @return internal Jira story ID
     */
    public String getJiraStoryId(final String issueKey) {

        JsonPath response = (JsonPath) executeGet(GET_ISSUE_BASE_URL + issueKey);
        JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
        return entity.get("id");
    }

    /**
     * Add the given test to a test cycle to prepare for test execution.
     *
     * @param issueKey Issue key
     * @param cycleMap Test Cycle object
     * @return test execution object
     */
    public TestExecute includeTestInCycle(final String issueKey, final HashMap cycleMap) {

        TestCycle testCycle = new TestCycle.Builder() //
                .setIssueId(issueKey) //
                .setCycleId(cycleMap.get("cycleId").toString()) //
                .setAssignee(cycleMap.get("assigneeUserName").toString()) //
                .setAssigneeType(cycleMap.get("assigneeDisplay").toString()) //
                .setProjectId(cycleMap.get("projectId").toString()) //
                .setVersionId(cycleMap.get("versionId").toString()) //
                .build();

        JSONObject payload = JsonUtils.convertObjectToJson(testCycle);
        JsonPath response = (JsonPath) executePost(ZAPI_EXECUTE_BASE_URL, payload.toString());
        JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
        String executionId = StringUtils.substringBetween(entity.get().toString(), "{", "=").trim();

        TestExecute testExecute = new TestExecute.Builder() //
                .setExecutionId(executionId) //
                .setIssueId(issueKey) //
                .setCycleId(cycleMap.get("cycleId").toString()) //
                .setProjectId(cycleMap.get("projectId").toString()) //
                .setVersionId("-1") //
                .build();

        return testExecute;
    }

    /**
     * Get first Cycle found based on Cycle Name provided.
     *
     * @param cycleName Cycle name to find
     * @return first cycle name entity
     */
    public HashMap getTestCycleByName(final String cycleName) {

        String encodedQuery = null;
        try {
            encodedQuery = URLEncoder.encode(String.format("=\"%s\"", cycleName), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            LOG(true, "Failed to get test cycle by name '%s'", cycleName);
        }
        JsonPath response = (JsonPath) executeGet(String.format("%scycleName%s", ZQL_QUERY_BASE_URL, encodedQuery));
        JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
        List executionList = entity.get("executions");
        return (HashMap) executionList.get(0);
    }

    /**
     * Execute a Jira test case and update it's status in Jira/Zephyr.
     *
     * @param execute hydrated TestExecution object
     * @return Jira update response
     */
    public JsonPath reportJiraResult(final TestExecute execute) {

        JSONObject payload = JsonUtils.convertObjectToJson(execute);
        JsonPath response = (JsonPath) executePut(ZAPI_EXECUTE_BASE_URL + execute.getExecutionId() + "/execute", payload.toString());
        return new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
    }

    @Override
    public boolean isJiraEnabled() {
        return jiraEnabled;
    }

    @Override
    public void setJiraEnabled(boolean jiraEnabled) {
        this.jiraEnabled = jiraEnabled;
    }

    @Override
    public String getJiraUrl() {
        return jiraUrl;
    }

    @Override
    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }

    @Override
    public String getJiraUsername() {
        return jiraUsername;
    }

    @Override
    public void setJiraUsername(String jiraUsername) {
        this.jiraUsername = jiraUsername;
    }

    @Override
    public String getJiraPassword() {
        return jiraPassword;
    }

    @Override
    public void setJiraPassword(String jiraPassword) {
        this.jiraPassword = jiraPassword;
    }

    @Override
    public String getCycleName() {
        return cycleName;
    }

    @Override
    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

}
