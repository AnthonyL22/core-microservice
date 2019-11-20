package com.pwc.core.framework.processors.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class JiraProcessorTest {

    private final String URL_PATH = "http://jira.pwc.com/api/2/issue/ANT-14677";
    private final String POST_PAYLOAD = "{\"issueId\":\"328613\",\"versionId\":\"18596\",\"cycleId\":\"9420\",\"assignee\":\"\",\"assigneeType\":\"\",\"projectId\":\"12400\"}";
    private JiraProcessor jiraProcessor;

    @Before
    public void setUp() {
        jiraProcessor = mock(JiraProcessor.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void executePostTest() {
        Object response = jiraProcessor.executePost(URL_PATH, POST_PAYLOAD);
        Assert.assertNull(response);
    }

    @Test
    public void executePutTest() {
        Object response = jiraProcessor.executePut(URL_PATH, POST_PAYLOAD);
        Assert.assertNull(response);
    }

    @Test
    public void executeGetTest() {
        Object response = jiraProcessor.executeGet(URL_PATH);
        Assert.assertNull(response);
    }

    @Test
    public void generateCredentialsTest() {
        jiraProcessor.setJiraUsername("admin");
        jiraProcessor.setJiraPassword("password");
        String encodedResponse = jiraProcessor.generateCredentials();
        Assert.assertEquals("Basic YWRtaW46cGFzc3dvcmQ=", encodedResponse);
    }

}
