package com.pwc.core.framework.listeners;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.MicroserviceTestSuite;
import com.saucelabs.saucerest.SauceREST;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.*;
import org.testng.internal.IConfiguration;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceTestListenerTest extends MicroserviceTestListener {

    public static final String TEST_NAME = "MyUnitTest";
    private static final String MOCK_JOB_LIST_JSON = "[{\"id\": \"6149cdd32e19491e8510b7348f73f9c4\"}, {\"id\": \"0020bcaa00eb4867ac1bc39cdd55e832\"}, {\"id\": \"7478386e9d204814865b2dfe0b45f6aa\"}, {\"id\": \"43b82b9bdff54c5086ff5de86dcfbda5\"}, {\"id\": \"244470bff9a0431fb6a71210aad8b81d\"}, {\"id\": \"a7c2a7f798764ae9ad3c1d6c9e9a281d\"}, {\"id\": \"8bb74e539dc84357af9ad1406261e80c\"}, {\"id\": \"625a1dc441a0424e84408a9b8ead4d8c\"}, {\"id\": \"449ccfd7a95149a3b0a573976c84db89\"}, {\"id\": \"08932b9ff1b548d7a92aa047e77c54cc\"}, {\"id\": \"bfd609e21ffa4cff817349d5cd8bf5f7\"}, {\"id\": \"173c58f54d6e45d3a2bb89c5752b3b65\"}, {\"id\": \"e5d18ca76f154b29af7d15bd953af421\"}, {\"id\": \"63d5b112fc0941029c0d3031559ec6c3\"}, {\"id\": \"e6b48922305d4610ac1ed37db2aaf90b\"}, {\"id\": \"07659748825c4a1db70c23e2fa716fb4\"}, {\"id\": \"5dcaa9f9dd6c4f23b4cd0b934fc1633c\"}, {\"id\": \"4ddca972e87240ccb07b4660761b9469\"}, {\"id\": \"5f37789791004dd2869953bbd453f885\"}, {\"id\": \"1a432c58511244d79667bdb076c261a8\"}, {\"id\": \"3b7decca3ccd43129a45c3d76021aee6\"}, {\"id\": \"d251a16cf5254b2081dc750266380bd2\"}, {\"id\": \"b4504a7c973b4b1bbd8974a40c880076\"}, {\"id\": \"6b3501cf2cb94ffea5fe8194b003a8a1\"}, {\"id\": \"5582bfdd9e624d0c8fdc817dfa933386\"}, {\"id\": \"e716b9158b1b4f13bc51bb91b30991bf\"}, {\"id\": \"8c304cb48130412b90601addd003a0fa\"}, {\"id\": \"e876ac3e799649fbb7163ce10b2a2463\"}, {\"id\": \"9886a3ead20648089d7eb8ee896a3e19\"}, {\"id\": \"2c933ff0b87c44369cac8c86d3a3d1d9\"}, {\"id\": \"5cbbd0e350e84ae387405a765608942f\"}, {\"id\": \"3ff23212eb0245b38da8355f245a73ac\"}, {\"id\": \"d355ec22c4fe4feeb22262bce4d35691\"}, {\"id\": \"7dbe305d519f4352a0da2ff2c69ffbbd\"}, {\"id\": \"9e7666e7604b47749682271f9520bfab\"}, {\"id\": \"28f291d1d9b94c889c8915ea4fad0b0d\"}, {\"id\": \"1e13ce89224c429ab523095ee5bad88a\"}, {\"id\": \"401dae0cb1a04ef798ecd73565ad3640\"}, {\"id\": \"d0f9f7334c2f422997fa1f8f93136fcd\"}, {\"id\": \"a9861b80a643441ea6e8d1c6315b28b9\"}, {\"id\": \"530e0bf1e6804efeb00941cce15e29c5\"}, {\"id\": \"119a5f53594748c38bb977633d4919d8\"}, {\"id\": \"31443489dad24c2bbe3d81b6cb1d8154\"}, {\"id\": \"a7b78a0dd197425ca98341799a903dd7\"}, {\"id\": \"7c5f3095de404dcba99c2ce25e0b24b7\"}, {\"id\": \"04342b13cf9a4b0ca36ee7883fbbb6c1\"}, {\"id\": \"ff971ae661a74cafa5a53515c8c94f6c\"}, {\"id\": \"a579cb3fbfd5423085e7e0a6c56e346e\"}, {\"id\": \"922958f7f77743608bf6784c3ca61584\"}, {\"id\": \"ed4e1bbbbf534a8286421c9322ae1021\"}, {\"id\": \"2f21a3af9ec141c089da02c283d9210f\"}, {\"id\": \"b60b9ceab6314ebe9a4a61f43c1f9f51\"}, {\"id\": \"d30e63ea6edc4e9cb51e10267b69c166\"}, {\"id\": \"146b58feea7746d996de71ee43553e76\"}, {\"id\": \"f2affa72553d476ea67583ddcb74717e\"}, {\"id\": \"441fa1f4e6e74e9c8368b0913969b7e1\"}, {\"id\": \"7763b6360769482d97629e88610c9a5e\"}, {\"id\": \"9fe455131b484a56b594ec18da481077\"}, {\"id\": \"6d5ec9e2f5cf47dfa3c6bb99b6ea21e8\"}, {\"id\": \"ef5b885c41104789a7ae0a61fa3e405a\"}, {\"id\": \"94cd4e68fdb04b18a7b84196af9141a4\"}, {\"id\": \"18a11dbc64f7495ba47aa09992954919\"}, {\"id\": \"b7bbd65dabde4ef8a5d64bc0ed8e2134\"}, {\"id\": \"cf422c554dd04e6d8c6c79aff2ea65da\"}, {\"id\": \"4cd1480330e7477080bcfcf7a29a3859\"}, {\"id\": \"67425214b6a5433c8dd1b172a195a8b0\"}, {\"id\": \"647d035aa9964d37b975007bca52e930\"}, {\"id\": \"a19c348e678e45b0bbe6bfd9fbfeec7f\"}, {\"id\": \"53b08df3bfab44bdbfa2c77035113ac0\"}, {\"id\": \"f9d9e344470d4668b284800d403c94aa\"}, {\"id\": \"b2933a6dcd354f0da52b5d804518ab92\"}, {\"id\": \"ab0726b68d5342d5aed2e1960c2738f9\"}, {\"id\": \"435d09b7d8e1454b9ddc1191ce663239\"}, {\"id\": \"666bc57306574ad1bdfd8dc769055dd1\"}, {\"id\": \"aaf7f4a5372d4b68a516efe0ae61cf40\"}, {\"id\": \"77ae5c98d8074966a12d2edde131f53e\"}, {\"id\": \"cb6cc8ca1f7b4bc1afb10fa12a5f33c4\"}, {\"id\": \"2b645a1e433046d589cb420cf3535074\"}, {\"id\": \"2a499f550afd4ba68a0825682e252d5d\"}, {\"id\": \"a7feba1fd6d44d7488fdccb5fc0899e3\"}, {\"id\": \"53df304a3ec444dc8eaa14d71fbf2a45\"}, {\"id\": \"bce4c9bfc50346f49327d30f259ea81c\"}, {\"id\": \"c6f57aebce7241feb081ab7c84dae692\"}, {\"id\": \"fea26b4fa9234a9a85c04290258725af\"}, {\"id\": \"75b9b8c475214199ac930f69210b060d\"}, {\"id\": \"0dfec67e61f84ce28fe458eaa65d9304\"}, {\"id\": \"47ce15480f0c4ed69eceb39208b9cf29\"}, {\"id\": \"3004413c01fa4fafa17972b03260f419\"}, {\"id\": \"4f178b6eda0f4c07ad3564692f568954\"}, {\"id\": \"ed3752ecee85408a9e01b459596f6f6f\"}, {\"id\": \"c2975e9eb75d4cc4a94a471779f87c18\"}, {\"id\": \"157f971305fc4006beb15429f4e01f08\"}, {\"id\": \"3251c69b575640c6af347873d2d3af97\"}, {\"id\": \"634b11630e984cccbf073e76f2140379\"}, {\"id\": \"c94158cb74044bcd8e6b4be621c9cb53\"}, {\"id\": \"848b2b76c42f4a93809615c66a9b64a5\"}, {\"id\": \"fa77aecc73e341339f0bdfd8f55cb964\"}, {\"id\": \"a7f72840688a4e988045eac8bc0a8e60\"}, {\"id\": \"f879ed1bf225465084088e35aea42c2d\"}, {\"id\": \"5da1597934304411a760713978de8d14\"}]";
    private static final String VALID_JOB_ID = "43b82b9bdff54c5086ff5de86dcfbda5";
    private static final String INVALID_JOB_ID = "123456789";
    private ITestResult mockITestResult;
    private TestRunner testRunner;
    private ITestNGMethod mockITestNGMethod;
    private MicroserviceTestSuite mockMicroserviceTestSuite;

    @Before
    public void setUp() {

        mockMicroserviceTestSuite = mock(MicroserviceTestSuite.class);
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
        testRunner = new TestRunner(mockIConfiguration, iSuite, xmlTest, true, methodListeners, null);

        ITestClass mockITestClass = mock(ITestClass.class);
        when(mockITestClass.getTestName()).thenReturn("[TestClass name=class com.pwc.automation.tests.web.BasicTest]");

        mockITestNGMethod = mock(ITestNGMethod.class);
        when(mockITestResult.getInstance()).thenReturn(mockMicroserviceTestSuite);
        when(mockITestResult.getTestClass()).thenReturn(mockITestClass);
        when(mockITestResult.getMethod()).thenReturn(mockITestNGMethod);
        when(mockITestResult.getName()).thenReturn("UnitTestCase");
        when(mockITestNGMethod.getDescription()).thenReturn("PAD-777");
        when(mockITestNGMethod.getTestClass()).thenReturn(mockITestClass);

        doNothing().when(mockSauceREST).updateJobInfo(VALID_JOB_ID, null);

        setGridEnabled(true);

        when(mockMicroserviceTestSuite.getCurrentJobId()).thenReturn(VALID_JOB_ID);

    }

    @Test
    public void jobDoesNotExistInSauceLabsTest() {
        when(mockMicroserviceTestSuite.getCurrentJobId()).thenReturn(INVALID_JOB_ID);
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void jobDoesExistsInSauceLabsTest() {
        when(mockMicroserviceTestSuite.getCurrentJobId()).thenReturn(VALID_JOB_ID);
        onTestSkipped(mockITestResult);
        verify(mockITestResult, times(3)).getName();
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
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void onTestSkippedTest() {
        onTestSkipped(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void onTestSuccessTest() {
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void onTestSuccessNullDescriptionTest() {
        when(mockITestNGMethod.getDescription()).thenReturn(null);
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

    @Test
    public void onTestSuccessEmptyDescriptionTest() {
        when(mockITestNGMethod.getDescription()).thenReturn("");
        onTestSuccess(mockITestResult);
        verify(mockITestResult, times(3)).getName();
    }

}
