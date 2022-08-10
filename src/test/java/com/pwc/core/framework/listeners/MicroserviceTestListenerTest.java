package com.pwc.core.framework.listeners;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.MicroserviceTestSuite;
import com.pwc.core.framework.util.BrowserStackREST;
import com.saucelabs.saucerest.SauceREST;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SuiteRunner;
import org.testng.TestRunner;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.IConfiguration;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceTestListenerTest extends MicroserviceTestListener {

    private static final String TEST_NAME = "MyUnitTest";
    private static final String VALID_JOB_ID = "43b82b9bdff54c5086ff5de86dcfbda5";
    private static final String INVALID_JOB_ID = "123456789";
    private ITestResult mockITestResult;
    private SauceREST mockSauceREST;
    private TestRunner testRunner;
    private ITestNGMethod mockITestNGMethod;
    private MicroserviceTestSuite mockMicroserviceTestSuite;

    @Before
    public void setUp() throws NoSuchMethodException {

        mockMicroserviceTestSuite = mock(MicroserviceTestSuite.class);

        mockSauceREST = mock(SauceREST.class);
        setSauceInstance(mockSauceREST);

        BrowserStackREST mockBrowserStackREST = mock(BrowserStackREST.class);
        setBrowserStackInstance(mockBrowserStackREST);

        mockITestResult = mock(ITestResult.class);

        IConfiguration mockIConfiguration = mock(IConfiguration.class);
        XmlSuite xmlSuite = new XmlSuite();
        xmlSuite.setName("unitTestSuiteName");
        ISuite suiteRunner = new SuiteRunner(mockIConfiguration, xmlSuite, "/");

        XmlTest xmlTest = new XmlTest();
        xmlTest.setName(TEST_NAME);
        xmlTest.setVerbose(0);
        xmlTest.setPreserveOrder(XmlSuite.CONTINUE);
        xmlTest.setSuite(xmlSuite);

        IInvokedMethodListener listener = new MicroserviceTestListener();
        List<IInvokedMethodListener> methodListeners = new ArrayList<>();
        methodListeners.add(listener);
        testRunner = new TestRunner(mockIConfiguration, suiteRunner, xmlTest, true, methodListeners);

        ITestClass mockITestClass = mock(ITestClass.class);
        when(mockITestClass.getTestName()).thenReturn("[TestClass name=class com.pwc.automation.tests.web.BasicTest]");

        mockITestNGMethod = mock(ITestNGMethod.class);
        when(mockITestResult.getInstance()).thenReturn(mockMicroserviceTestSuite);
        when(mockITestResult.getTestClass()).thenReturn(mockITestClass);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);
        when(mockITestResult.getName()).thenReturn("UnitTestCase");

        when(mockITestNGMethod.getDescription()).thenReturn("MYSTORY-777");
        when(mockITestNGMethod.getTestClass()).thenReturn(mockITestClass);

        ConstructorOrMethod mockConstructorOrMethod = mock(ConstructorOrMethod.class);
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);

        SampleTest mockTest = new SampleTest();
        Method mockMethod = mockTest.getClass().getMethod("testLoginWithAnnotationTest");
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);
        when(mockConstructorOrMethod.getMethod()).thenReturn(mockMethod);

        //        TestCase mockTestCase = mockMethod.getAnnotation(TestCase.class);
        //        when(mockMethod.getAnnotation(TestCase.class)).thenReturn(mockTestCase);

        doNothing().when(mockSauceREST).updateJobInfo(VALID_JOB_ID, null);

        setGridEnabled(true);

    }

    @Test
    public void sauceLabsMarkJobResultsTest() throws NoSuchMethodException {

        when(mockSauceREST.getJobInfo(VALID_JOB_ID)).thenReturn(VALID_JOB_ID);

        setGridUrl("http://bruce:wilson1234567@ondemand.saucelabs.com:80/wd/hub");
        SampleTest sample = new SampleTest();
        Method mockMethod = sample.getClass().getMethod("testLoginNoAnnotationTest");

        ConstructorOrMethod mockConstructorOrMethod = mock(ConstructorOrMethod.class);
        when(mockConstructorOrMethod.getMethod()).thenReturn(mockMethod);
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);

        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void browserStackMarkJobResultsTest() throws NoSuchMethodException {

        setGridUrl("http://brian:bar1234567@hub-cloud.browserstack.com/wd/hub");
        SampleTest sample = new SampleTest();
        Method mockMethod = sample.getClass().getMethod("testLoginNoAnnotationTest");

        ConstructorOrMethod mockConstructorOrMethod = mock(ConstructorOrMethod.class);
        when(mockConstructorOrMethod.getMethod()).thenReturn(mockMethod);
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);

        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void storyIssueLoggingTest() throws NoSuchMethodException {

        SampleTest sample = new SampleTest();
        Method mockMethod = sample.getClass().getMethod("testLoginNoAnnotationTest");

        ConstructorOrMethod mockConstructorOrMethod = mock(ConstructorOrMethod.class);
        when(mockConstructorOrMethod.getMethod()).thenReturn(mockMethod);
        when(mockITestNGMethod.getConstructorOrMethod()).thenReturn(mockConstructorOrMethod);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);

        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void jobDoesNotExistInSauceLabsTest() {
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

    @Test
    public void jobDoesExistsInSauceLabsTest() {
        onTestSkipped(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

    @Test
    public void onFinishTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT, "dev-env");
        onFinish(testRunner);
    }

    @Test
    public void onTestFailureTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT, "dev-env");
        onTestFailure(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

    @Test
    public void onTestSkippedTest() {
        onTestSkipped(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

    @Test
    public void onTestSuccessTest() {
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

    @Test
    public void onTestSuccessNullDescriptionTest() {
        when(mockITestNGMethod.getDescription()).thenReturn(null);
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

    @Test
    public void onTestSuccessEmptyDescriptionTest() {
        when(mockITestNGMethod.getDescription()).thenReturn("");
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(4)).getName();
    }

}
