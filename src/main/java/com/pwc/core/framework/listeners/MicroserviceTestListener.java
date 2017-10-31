package com.pwc.core.framework.listeners;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.MicroserviceTestSuite;
import com.pwc.core.framework.annotations.Issue;
import com.pwc.core.framework.data.PropertiesFile;
import com.pwc.core.framework.util.PropertiesUtils;
import com.pwc.logging.helper.LoggerHelper;
import com.saucelabs.saucerest.SauceREST;
import org.apache.commons.lang3.StringUtils;
import org.testng.*;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.pwc.logging.service.LoggerService.LOG;

@Listeners
public class MicroserviceTestListener extends TestListenerAdapter implements ITestListener, IInvokedMethodListener {

    private MicroserviceTestSuite sessionIdProvider;
    private SauceREST sauceInstance;
    private boolean gridEnabled;
    private String gridUrl;

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (sauceInstance == null) {
            String username = PropertiesUtils.getPropertyFromPropertiesFile(
                    String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName), "saucelabs.username");
            String accessKey = PropertiesUtils.getPropertyFromPropertiesFile(
                    String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName), "saucelabs.accesskey");
            gridUrl = PropertiesUtils.getPropertyFromPropertiesFile(
                    String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.GRID_PROPERTIES_FILE.fileName), "grid.hub.url");
            gridEnabled = Boolean.valueOf(PropertiesUtils.getPropertyFromPropertiesFile(
                    String.format("config/%s/%s", System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT), PropertiesFile.GRID_PROPERTIES_FILE.fileName), "grid.enabled"));
            sauceInstance = new SauceREST(username, accessKey);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LOG(true, StringUtils.repeat("\n", 2));
        LOG(true, StringUtils.repeat("-", 100));
        LOG(true, "TEST EXECUTION COMPLETE");
        LOG(true, "%s--Executed in Environment: %s", iTestContext.getName(), System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT).toUpperCase());
        LOG(true, "%s--Executed on Date/Time: %s", iTestContext.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, StringUtils.repeat("-", 100));
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        sessionIdProvider = (MicroserviceTestSuite) tr.getInstance();
        logIssueAnnotationInformation(tr);
        LOG(true, "%s--Description: %s", tr.getName(), !StringUtils.isEmpty(tr.getMethod().getDescription()) ? tr.getMethod().getDescription() : "N/A");
        LOG(true, "%s--Executed on Date/Time: %s", tr.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, "%s--Test Failed", tr.getName());
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        sessionIdProvider = (MicroserviceTestSuite) tr.getInstance();
        markJobResults(tr, sessionIdProvider.getCurrentJobId(), false);
        logIssueAnnotationInformation(tr);
        LOG(true, "%s--Description: %s", tr.getName(), !StringUtils.isEmpty(tr.getMethod().getDescription()) ? tr.getMethod().getDescription() : "N/A");
        LOG(true, "%s--Executed on Date/Time: %s", tr.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, "%s--Test Skipped", tr.getName());
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        sessionIdProvider = (MicroserviceTestSuite) tr.getInstance();
        markJobResults(tr, sessionIdProvider.getCurrentJobId(), true);
        logIssueAnnotationInformation(tr);
        LOG(true, "%s--Description: %s", tr.getName(), !StringUtils.isEmpty(tr.getMethod().getDescription()) ? tr.getMethod().getDescription() : "N/A");
        LOG(true, "%s--Executed on Date/Time: %s", tr.getName(), LoggerHelper.getDateTime(FrameworkConstants.DATETIME_LOGGER_DATETIME_PATTER, FrameworkConstants.SYSTEM_DEFAULT_TIMEZONE, 0));
        LOG(true, "%s--Test Passed", tr.getName());
    }

    /**
     * Get the issue or story information from the @Issue annotation
     *
     * @param tr active test result
     */
    protected void logIssueAnnotationInformation(ITestResult tr) {
        try {
            Method method = tr.getMethod().getConstructorOrMethod().getMethod();
            Issue issueMetadata = method.getAnnotation(Issue.class);
            if (StringUtils.isNotEmpty(issueMetadata.value())) {
                LOG(true, "%s--Issue(s): %s", tr.getName(), issueMetadata.value());
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Report test results to Saucelabs via their REST api
     *
     * @param tr          current test result
     * @param didTestPass test result status
     */
    private void markJobResults(ITestResult tr, String jobId, boolean didTestPass) {
        try {
            String testName = LoggerHelper.getClassNameFromClasspath(tr.getMethod());
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", testName);
            updates.put("passed", didTestPass);
            if (jobExistsInSaucelabs(jobId)) {
                LOG(true, "SauceOnDemandSessionID=%s job-name=%s", jobId, testName);
                sauceInstance.updateJobInfo(jobId, updates);
            }
        } catch (Exception eatMessage) {
            eatMessage.getMessage();
        }
    }

    /**
     * Check if the current job ID exists in Saucelabs environment.  If it does then the
     * job will be updated with the current test results for this test execution
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

    public void setSauceInstance(SauceREST sauceInstance) {
        this.sauceInstance = sauceInstance;
    }

    public void setSessionIdProvider(MicroserviceTestSuite sessionIdProvider) {
        this.sessionIdProvider = sessionIdProvider;
    }

    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
    }

}
