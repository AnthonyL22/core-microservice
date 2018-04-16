package com.pwc.core.framework.listeners;

import com.pwc.core.framework.MicroserviceTestSuite;
import com.pwc.logging.helper.LoggerHelper;
import com.saucelabs.saucerest.SauceREST;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SuiteRunner;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.IConfiguration;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetryListenerTest extends MicroserviceTestListener {

    public static final String TEST_NAME = "MyUnitRetryTest";
    private ITestResult mockITestResult;
    private ConstructorOrMethod mockConstructorOrMethod;
    private ITestNGMethod mockITestNGMethod;
    private Retry mockRetry;
    private Method mockMethod;

    @Before
    public void setUp() {

        mockRetry = mock(Retry.class);
        MicroserviceTestSuite mockMicroserviceTestSuite = mock(MicroserviceTestSuite.class);
        setSessionIdProvider(mockMicroserviceTestSuite);

        SauceREST mockSauceREST = mock(SauceREST.class);
        setSauceInstance(mockSauceREST);
        mockITestResult = mock(ITestResult.class);

        IConfiguration mockIConfiguration = mock(IConfiguration.class);
        XmlSuite xmlSuite = new XmlSuite();
        xmlSuite.setName("unitTestSuiteName");
        ISuite iSuite = new SuiteRunner(mockIConfiguration, xmlSuite, "/");

        XmlTest xmlTest = new XmlTest();
        xmlTest.setName(TEST_NAME);
        xmlTest.setVerbose(0);
        xmlTest.setPreserveOrder(XmlSuite.CONTINUE);
        xmlTest.setSuite(xmlSuite);

        IInvokedMethodListener listener = new MicroserviceTestListener();
        List<IInvokedMethodListener> methodListeners = new ArrayList<>();
        methodListeners.add(listener);

        ITestClass mockITestClass = mock(ITestClass.class);
        when(mockITestClass.getTestName()).thenReturn("[TestClass name=class com.pwc.automation.tests.web.BasicTest]");

        mockConstructorOrMethod = mock(ConstructorOrMethod.class);
        mockITestNGMethod = mock(ITestNGMethod.class);
        when(mockITestResult.getInstance()).thenReturn(mockMicroserviceTestSuite);
        when(mockITestResult.getTestClass()).thenReturn(mockITestClass);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);
        when(mockITestResult.getName()).thenReturn("UnitTestCase");
        when(mockITestNGMethod.getDescription()).thenReturn("MYSTORY-777");
        when(mockITestNGMethod.getTestClass()).thenReturn(mockITestClass);

        setGridEnabled(true);

    }

    @Test
    public void retryNoMaxRetryAnnotationTest() {
        try {
            SampleTest sample = new SampleTest();
            mockMethod = sample.getClass().getMethod("testLoginNoAnnotationTest");
        } catch (NoSuchMethodException e) {
            Assert.fail("Failed to create mock Method obj");
        }

        when(mockITestResult.getStatus()).thenReturn(2);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);
        when(mockConstructorOrMethod.getMethod()).thenReturn(mockMethod);
        when(mockRetry.retry(mockITestResult)).thenCallRealMethod();
        mockRetry.retry(mockITestResult);
        verify(mockRetry, times(1)).retry(mockITestResult);
    }

    @Test
    public void retryMaxRetryAnnotationTest() {
        try {
            SampleTest sample = new SampleTest();
            mockMethod = sample.getClass().getMethod("testLoginWithAnnotationTest");
        } catch (NoSuchMethodException e) {
            Assert.fail("Failed to create mock Method obj");
        }

        when(LoggerHelper.getClassName(mockITestResult)).thenReturn("LoginWithAnnotationTest");
        when(LoggerHelper.getClassNameFromClasspath(mockITestResult.getMethod())).thenReturn("LoginWithAnnotationTest");

        when(mockITestResult.getStatus()).thenReturn(2);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);
        when(mockConstructorOrMethod.getMethod()).thenReturn(mockMethod);
        when(mockRetry.retry(mockITestResult)).thenCallRealMethod();
        mockRetry.retry(mockITestResult);
        verify(mockRetry, times(1)).retry(mockITestResult);
    }

    @Test
    public void successGetTestResultStatusTest() {
        when(mockITestResult.getStatus()).thenReturn(1);
        when(mockRetry.getTestResultStatus(mockITestResult.getStatus())).thenCallRealMethod();
        String result = mockRetry.getTestResultStatus(mockITestResult.getStatus());
        Assert.assertEquals(result, "SUCCESS");
        verify(mockRetry, times(1)).getTestResultStatus(mockITestResult.getStatus());
    }

    @Test
    public void failGetTestResultStatusTest() {
        when(mockITestResult.getStatus()).thenReturn(2);
        when(mockRetry.getTestResultStatus(mockITestResult.getStatus())).thenCallRealMethod();
        String result = mockRetry.getTestResultStatus(mockITestResult.getStatus());
        Assert.assertEquals(result, "FAILURE");
        verify(mockRetry, times(1)).getTestResultStatus(mockITestResult.getStatus());
    }

    @Test
    public void skipGetTestResultStatusTest() {
        when(mockITestResult.getStatus()).thenReturn(3);
        when(mockRetry.getTestResultStatus(mockITestResult.getStatus())).thenCallRealMethod();
        String result = mockRetry.getTestResultStatus(mockITestResult.getStatus());
        Assert.assertEquals(result, "SKIP");
        verify(mockRetry, times(1)).getTestResultStatus(mockITestResult.getStatus());
    }

}
