package com.pwc.core.framework.listeners;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.MicroserviceTestSuite;
import com.pwc.core.framework.annotations.Issue;
import com.pwc.core.framework.annotations.TestCase;
import com.pwc.core.framework.data.PropertiesFile;
import com.pwc.core.framework.data.TestExecute;
import com.pwc.core.framework.type.Tunnel;
import com.pwc.core.framework.util.BrowserStackREST;
import com.pwc.core.framework.util.JsonUtils;
import com.pwc.core.framework.util.PropertiesUtils;
import com.pwc.logging.helper.LoggerHelper;
import com.saucelabs.saucerest.SauceREST;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.pwc.logging.service.LoggerService.LOG;

@Listeners
public class MicroserviceTestListener extends TestListenerAdapter implements ITestListener, IInvokedMethodListener {

    private static final String TEST_CASE_ID_REGEX = "[\\s,;]+";
    private static final Pattern TEST_CASE_PATTERN = Pattern.compile(TEST_CASE_ID_REGEX);
    private static final String SAUCE_LABS_API = "https://api.us-west-1.saucelabs.com/rest/v1";

    private MicroserviceTestSuite sessionIdProvider;
    private SauceREST sauceInstance;
    private BrowserStackREST browserStackInstance;
    private boolean gridEnabled;
    private String gridUrl;

    @Override
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult testResult) {

        String gridToUse = PropertiesUtils.getPropertyFromPropertiesFile(
                        String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.GRID_PROPERTIES_FILE.fileName), "grid.hub.url");

        if (sauceInstance == null && StringUtils.containsIgnoreCase(gridToUse, "saucelabs")) {
            String username = PropertiesUtils.getPropertyFromPropertiesFile(
                            String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName),
                            "saucelabs.username");
            String accessKey = PropertiesUtils.getPropertyFromPropertiesFile(
                            String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName),
                            "saucelabs.accesskey");
            sauceInstance = new SauceREST(username, accessKey);
            setSauceLabsTunnel();
        } else if (browserStackInstance == null && StringUtils.containsIgnoreCase(gridToUse, "browserstack")) {
            String username = PropertiesUtils.getPropertyFromPropertiesFile(
                            String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName),
                            "browserstack.username");
            String accessKey = PropertiesUtils.getPropertyFromPropertiesFile(
                            String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName),
                            "browserstack.accesskey");
            browserStackInstance = new BrowserStackREST(username, accessKey);
        }
        gridUrl = PropertiesUtils.getPropertyFromPropertiesFile(
                        String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.GRID_PROPERTIES_FILE.fileName), "grid.hub.url");
        gridEnabled = Boolean.valueOf(PropertiesUtils.getPropertyFromPropertiesFile(
                        String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.GRID_PROPERTIES_FILE.fileName), "grid.enabled"));
    }

    /**
     * Set the Sauce Labs tunnelIdentifier according to that found in the 'automation.properties' file and the active tunnel
     * in Sauce Labs.  The automation.properties "saucelabs.username" must match the Jenkins Sauce Connect account used in the
     * Jenkins pipeline.
     */
    private void setSauceLabsTunnel() {

        boolean isTunnelSet = StringUtils.isNotEmpty(System.getProperty(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY));
        boolean isGridEnabled = Boolean.parseBoolean(getAutomationPropertiesFileProperty(FrameworkConstants.GRID_PROPERTIES_FILE, FrameworkConstants.GRID_ENABLED_SETTING));
        if (!isTunnelSet && isGridEnabled) {
            try (CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(new SSLContextBuilder().build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build()) {

                LOG(true, "Preparing to set Sauce Labs %s", "tunnel");
                String sauceLabsUsername = getAutomationPropertiesFileProperty(FrameworkConstants.AUTOMATION_PROPERTIES_FILE, FrameworkConstants.SAUCE_LABS_USERNAME_SETTING);
                String accessKey = getAutomationPropertiesFileProperty(FrameworkConstants.AUTOMATION_PROPERTIES_FILE, FrameworkConstants.SAUCE_LABS_ACCESSKEY_SETTING);
                String apiUrl = String.format(SAUCE_LABS_API + "/%s/tunnels?full=true", sauceLabsUsername);
                HttpGet httpGet = new HttpGet(apiUrl);
                httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                String auth = "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", sauceLabsUsername, accessKey).getBytes());
                httpGet.setHeader(HttpHeaders.AUTHORIZATION, auth);
                HttpResponse response = httpClient.execute(httpGet);

                JSONArray tunnelList = new JSONArray(EntityUtils.toString(response.getEntity()));
                LOG(true, "%s Active Sauce Lab tunnels detected", tunnelList.length());
                List<Tunnel> activeTunnelList = new ArrayList<>();
                for (int i = 0; i < tunnelList.length(); i++) {
                    activeTunnelList.add((Tunnel) JsonUtils.convertJSONToObject((JSONObject) tunnelList.get(i), Tunnel.class));
                }
                Optional<Tunnel> matchingTunnel = activeTunnelList.stream().filter(tunnel -> StringUtils.equalsIgnoreCase(tunnel.getOwner(), sauceLabsUsername)).findFirst();
                if (matchingTunnel.isPresent()) {
                    LOG(true, "Found Matching Sauce Labs tunnelIdentifier=%s", matchingTunnel.get().getTunnelIdentifier());
                    System.setProperty(FrameworkConstants.TUNNEL_IDENTIFIER_CAPABILITY, matchingTunnel.get().getTunnelIdentifier());
                } else if (tunnelList.length() == 0) {
                    LOG(true, "%s Active Sauce Lab tunnels detected", "NO");
                }

            } catch (Exception e) {
                LOG(true, "Failed to fetch all active Sauce Labs tunnels due to %s", e);
            }
        }
    }

    /**
     * Fetch the property from the automation.properties file in the active test.env.
     *
     * @param propertyFileName property file name to analyze
     * @param propertyName     property to get from properties file
     * @return property
     */
    private String getAutomationPropertiesFileProperty(final String propertyFileName, final String propertyName) {

        return PropertiesUtils.getPropertyFromPropertiesFile(String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), propertyFileName), propertyName);
    }

    @Override
    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult testResult) {
    }

    @Override
    public void onTestStart(ITestResult testResult) {
    }

    @Override
    public void onFinish(ITestContext testContext) {

        LOG(true, StringUtils.repeat("\n", 2));
        LOG(true, StringUtils.repeat("-", 100));
        LOG(true, "TEST EXECUTION COMPLETE");
        LOG(true, "%s--Executed in Environment: %s", testContext.getName(), System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT).toUpperCase());
        LOG(true, "%s--Executed on Date/Time: %s", testContext.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, StringUtils.repeat("-", 100));
    }

    @Override
    public void onTestFailure(ITestResult testResult) {

        sessionIdProvider = (MicroserviceTestSuite) testResult.getInstance();
        logIssueAnnotationInformation(testResult);
        publishJiraTestResults(testResult);
        LOG(true, "%s--Description: %s", testResult.getName(), !StringUtils.isEmpty(testResult.getMethod().getDescription()) ? testResult.getMethod().getDescription() : "N/A");
        LOG(true, "%s--Executed on Date/Time: %s", testResult.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, "%s--Test Failed", testResult.getName());
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {

        sessionIdProvider = (MicroserviceTestSuite) testResult.getInstance();
        markJobResults(testResult, sessionIdProvider.getCurrentJobId(), false);
        logIssueAnnotationInformation(testResult);
        publishJiraTestResults(testResult);
        LOG(true, "%s--Description: %s", testResult.getName(), !StringUtils.isEmpty(testResult.getMethod().getDescription()) ? testResult.getMethod().getDescription() : "N/A");
        LOG(true, "%s--Executed on Date/Time: %s", testResult.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, "%s--Test Skipped", testResult.getName());
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {

        sessionIdProvider = (MicroserviceTestSuite) testResult.getInstance();
        markJobResults(testResult, sessionIdProvider.getCurrentJobId(), true);
        logIssueAnnotationInformation(testResult);
        publishJiraTestResults(testResult);
        LOG(true, "%s--Description: %s", testResult.getName(), !StringUtils.isEmpty(testResult.getMethod().getDescription()) ? testResult.getMethod().getDescription() : "N/A");
        LOG(true, "%s--Executed on Date/Time: %s", testResult.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, "%s--Test Passed", testResult.getName());
    }

    /**
     * Get the issue or story information from the @Issue annotation.
     *
     * @param testResult active test result
     */
    protected void logIssueAnnotationInformation(ITestResult testResult) {

        try {
            Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
            Issue issueMetadata = method.getAnnotation(Issue.class);
            if (StringUtils.isNotEmpty(issueMetadata.value())) {
                LOG(true, "%s--Issue(s): %s", testResult.getName(), issueMetadata.value());
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Report test results to Saucelabs via their REST api.
     *
     * @param testResult  current test result
     * @param didTestPass test result status
     */
    private void markJobResults(ITestResult testResult, String jobId, boolean didTestPass) {

        try {
            String testName = LoggerHelper.getClassNameFromClasspath(testResult.getMethod());
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", testName);
            updates.put("passed", didTestPass);
            if (StringUtils.isEmpty(jobId)) {
                LOG(true, "Failed to set SauceOnDemandSessionID for job-name=%s", testName);
            } else if (jobExistsInSaucelabs(jobId)) {
                LOG(true, "SauceOnDemandSessionID=%s job-name=%s", jobId, testName);
                sauceInstance.updateJobInfo(jobId, updates);
            } else if (jobExistsInBrowserStack(jobId)) {
                updates.clear();
                updates = new HashMap<>();
                if (didTestPass) {
                    updates.put("status", "passed");
                } else {
                    updates.put("status", "failed");
                }
                LOG(true, "BrowserStackSessionID=%s job-name=%s", jobId, testName);
                browserStackInstance.updateJobInfo(jobId, updates);
            }
        } catch (Exception eatMessage) {
            eatMessage.getMessage();
        }
    }

    /**
     * Check if the current job ID exists in Saucelabs environment.  If it does then the
     * job will be updated with the current test results for this test execution.
     *
     * @return job exists in Saucelabs
     */
    private boolean jobExistsInSaucelabs(String jobId) {
        try {
            if (gridEnabled && StringUtils.containsIgnoreCase(gridUrl, "saucelabs")) {
                if (sauceInstance != null && !StringUtils.isEmpty(jobId)) {
                    return !StringUtils.isEmpty(sauceInstance.getJobInfo(jobId));
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Check if the current job ID exists in BrowserStack environment.  If it does then the
     * job will be updated with the current test results for this test execution.
     *
     * @return job exists in BrowserStack
     */
    private boolean jobExistsInBrowserStack(String jobId) {
        try {
            if (gridEnabled && StringUtils.containsIgnoreCase(gridUrl, "browserstack")) {
                if (browserStackInstance != null && !StringUtils.isEmpty(jobId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Report Zephyr results to Jira.  Comma or space seperated list of Zephyr IDs must be defined in the
     * TestCase() annotation.
     *
     * @param tr current test result
     */
    private void publishJiraTestResults(final ITestResult tr) {

        Method method = tr.getMethod().getConstructorOrMethod().getMethod();
        TestCase testCaseMetadata = method.getAnnotation(TestCase.class);
        if (null != MicroserviceTestSuite.getJiraController() && null != testCaseMetadata && StringUtils.isNotEmpty(testCaseMetadata.value())) {
            List<String> testCaseIdList = Arrays.asList(TEST_CASE_PATTERN.split(testCaseMetadata.value()));
            if (MicroserviceTestSuite.getJiraController().isJiraEnabled() && CollectionUtils.isNotEmpty(testCaseIdList)) {
                testCaseIdList.forEach(testCaseId -> {
                    String jiraId = MicroserviceTestSuite.getJiraController().getJiraStoryId(testCaseId);
                    HashMap cycle = MicroserviceTestSuite.getJiraController().getTestCycleByName(MicroserviceTestSuite.getJiraController().getCycleName());
                    if (StringUtils.isNotEmpty(jiraId) && null != cycle) {
                        TestExecute testExecute = MicroserviceTestSuite.getJiraController().includeTestInCycle(jiraId, cycle);
                        testExecute.setStatus(tr.getStatus());
                        MicroserviceTestSuite.getJiraController().reportJiraResult(tr, testExecute, testCaseId);
                    }
                });
            }
        }
    }

    public void setSauceInstance(SauceREST sauceInstance) {
        this.sauceInstance = sauceInstance;
    }

    public void setBrowserStackInstance(BrowserStackREST browserStackInstance) {
        this.browserStackInstance = browserStackInstance;
    }

    public void setGridUrl(String gridUrl) {
        this.gridUrl = gridUrl;
    }

    public void setSessionIdProvider(MicroserviceTestSuite sessionIdProvider) {
        this.sessionIdProvider = sessionIdProvider;
    }

    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
    }

}
