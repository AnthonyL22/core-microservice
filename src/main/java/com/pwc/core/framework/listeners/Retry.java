package com.pwc.core.framework.listeners;

import com.pwc.core.framework.annotations.MaxRetryCount;
import com.pwc.logging.helper.LoggerHelper;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.lang.reflect.Method;

import static com.pwc.logging.service.LoggerService.LOG;

public class Retry implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult testResult) {

        MaxRetryCount maxRetryCount;
        int maxRetry;
        try {
            Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
            maxRetryCount = method.getAnnotation(MaxRetryCount.class);
            maxRetry = maxRetryCount.value();
        } catch (Exception e) {
            maxRetry = 1;
        }

        if (retryCount < maxRetry) {
            LOG(true, "RETRYING TEST='%s' with status='%s' for the %s time(s)", LoggerHelper.getClassName(testResult), getTestResultStatus(testResult.getStatus()), (retryCount + 1));
            retryCount++;
            return true;
        }
        return false;
    }

    public String getTestResultStatus(final int status) {

        String resultName = null;
        if (status == 1) {
            resultName = "SUCCESS";
        }
        if (status == 2) {
            resultName = "FAILURE";
        }
        if (status == 3) {
            resultName = "SKIP";
        }
        return resultName;
    }

}
