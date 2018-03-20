package com.pwc.core.framework;

import com.mongodb.BasicDBObject;
import com.pwc.core.framework.command.DatabaseCommand;
import com.pwc.core.framework.command.WebServiceCommand;
import com.pwc.core.framework.controller.DatabaseController;
import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.controller.WebServiceController;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.OAuthKey;
import com.pwc.core.framework.service.DatabaseEventService;
import com.pwc.core.framework.service.WebEventService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import org.springframework.context.support.AbstractApplicationContext;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceTestSuiteTest extends MicroserviceTestSuiteBaseTest {

    private static final String IDENTIFIER = "//div[@id='nav-bar']";
    private static final String SQL_TEMPLATE_QUERY = "select * from model_recommendation where id = ?";

    private WebElement mockWebElement;
    private WebEventService mockWebEventService;
    private WebEventController mockWebEventController;
    private DatabaseController mockDatabaseController;
    private WebServiceController mockWebServiceController;
    private DatabaseEventService mockDatabaseEventService;
    private Credentials mockCredentials;
    private OAuthKey mockOAuthKey;

    private enum UsersWebServiceWebServiceCommand implements WebServiceCommand {

        GET_BY_LAST_NAME(FrameworkConstants.GET_REQUEST, "rest/users", "byLastName");

        private String requestMethodType;
        private String requestMapping;
        private String methodName;

        UsersWebServiceWebServiceCommand(String type, String mapping, String name) {
            requestMethodType = type;
            requestMapping = mapping;
            methodName = name;
        }

        @Override
        public String methodType() {
            return this.requestMethodType;
        }

        @Override
        public String mappingName() {
            return this.requestMapping;
        }

        @Override
        public String methodName() {
            return this.methodName;
        }

    }

    private enum QueryCommand implements DatabaseCommand {

        QUERY_MODEL_RECOMMENDATION_BY_ID("select * from model_recommendation where id = ?"),
        QUERY_MODEL_RECOMMENDATION("select * from model_recommendation");

        private String sqlStatement;

        QueryCommand(String sql) {
            sqlStatement = sql;
        }

        @Override
        public String sql() {
            return this.sqlStatement;
        }

    }

    @Before
    public void setUp() {

        Map mockHttpResultMap = new HashMap();
        mockHttpResultMap.put(FrameworkConstants.STATUS_KEY, "HTTP/1.1 200 OK");
        mockHttpResultMap.put(FrameworkConstants.ENTITY_KEY, "\"ENTITY\" -> \"{\"status\":400,\"result\":\"Widget has been set to read-only.\",\"widgetId\":null}\"");

        mockCredentials = new Credentials("foo", "bar");

        mockWebElement = mock(WebElement.class);
        mockWebEventService = mock(WebEventService.class);
        mockWebEventController = mock(WebEventController.class);
        mockWebServiceController = mock(WebServiceController.class);
        mockDatabaseEventService = mock(DatabaseEventService.class);
        mockDatabaseController = mock(DatabaseController.class);

        mockOAuthKey = mock(OAuthKey.class);

    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockWebElement);
        verifyNoMoreInteractions(mockDatabaseEventService);
    }

    @Test
    public void setUpMethodTest() {
        setUpMethod();
    }

    @Test
    public void tearDownMethodTest() {
        tearDownMethod();
    }

    @Test
    public void setUpClassTest() {
        setUpClass();
    }

    @Test
    public void webActionCredentialsTest() {
        AbstractApplicationContext mockAbstractApplicationContext = mock(AbstractApplicationContext.class);
        setCtx(mockAbstractApplicationContext);
        when(mockAbstractApplicationContext.getBean("webEventController")).thenReturn(mockWebEventController);
        webAction(mockCredentials);
        verify(mockWebEventService, times(0)).findWebElement(IDENTIFIER);
        verify(mockAbstractApplicationContext, times(1)).getBean("webEventController");
    }

    @Test
    public void databaseActionTest() {
        databaseController = mockDatabaseController;
        when(databaseController.getDatabaseEventService()).thenReturn(mockDatabaseEventService);
        when(mockDatabaseEventService.executeParameterQuery(SQL_TEMPLATE_QUERY, new Object[]{1})).thenReturn(new Object());
        databaseAction(QueryCommand.QUERY_MODEL_RECOMMENDATION_BY_ID, 1);
        verify(mockDatabaseEventService, times(1)).executeParameterQuery(SQL_TEMPLATE_QUERY, new Object[]{1});
    }

    @Test(expected = NullPointerException.class)
    public void databaseActionNullControllerTest() {
        when(mockDatabaseEventService.executeParameterQuery(SQL_TEMPLATE_QUERY, new Object[]{1})).thenReturn(new Object());
        databaseAction(QueryCommand.QUERY_MODEL_RECOMMENDATION_BY_ID, 1);
        verify(mockDatabaseEventService, times(0)).executeParameterQuery(SQL_TEMPLATE_QUERY, new Object[]{1});
    }

    @Test(expected = NullPointerException.class)
    public void webActionNullControllerTest() {
        when(mockWebEventService.findWebElement(IDENTIFIER)).thenReturn(mockWebElement);
        webAction(IDENTIFIER);
        verify(mockWebEventService, times(0)).findWebElement(IDENTIFIER);
    }

    @Test
    public void webActionCredentialsNonNullWebEventControllerTest() {
        webEventController = mockWebEventController;
        when(webEventController.getWebEventService()).thenReturn(mockWebEventService);
        when(mockWebEventService.findWebElement(IDENTIFIER)).thenReturn(mockWebElement);
        webAction(mockCredentials);
        verify(mockWebEventService, times(0)).findWebElement(IDENTIFIER);
    }

    @Test
    public void webActionFoundWebElementTest() {
        webEventController = mockWebEventController;
        when(webEventController.getWebEventService()).thenReturn(mockWebEventService);
        when(mockWebEventService.findWebElement(IDENTIFIER)).thenReturn(mockWebElement);
        webAction(IDENTIFIER);
        verify(mockWebEventService, times(1)).findWebElement(IDENTIFIER);
    }

    @Test
    public void webActionBooleanAttributeTest() {
        webEventController = mockWebEventController;
        when(webEventController.getWebEventService()).thenReturn(mockWebEventService);
        when(mockWebEventService.findWebElement(IDENTIFIER)).thenReturn(mockWebElement);
        webAction(IDENTIFIER, true);
        verify(mockWebEventService, times(1)).findWebElement(IDENTIFIER);
    }

    @Test
    public void webActionStringAttributeTest() {
        webEventController = mockWebEventController;
        when(webEventController.getWebEventService()).thenReturn(mockWebEventService);
        when(mockWebEventService.findWebElement(IDENTIFIER)).thenReturn(mockWebElement);
        webAction(IDENTIFIER, true);
        verify(mockWebEventService, times(1)).findWebElement(IDENTIFIER);
    }

    @Test(expected = AssertionError.class)
    public void webActionNotFoundWebElementTest() {
        webEventController = mockWebEventController;
        when(webEventController.getWebEventService()).thenReturn(mockWebEventService);
        webAction(IDENTIFIER);
        verify(mockWebEventService, times(1)).findWebElement(IDENTIFIER);
    }

    @Test
    public void setUpClassDefaultSystemUnderTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT, "");
        setUpClass();
        Assert.assertEquals(System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT),
                FrameworkConstants.DEV_ENVIRONMENT_KEY, "Verify default env is DEV");
    }

    @Test
    public void setUpClassDefinedSystemUnderTest() {
        System.setProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT, "FOOBAR");
        setUpClass();
        Assert.assertEquals(System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT),
                "FOOBAR", "Verify default env is DEV");
    }

    @Test
    public void tearDownClassExistingDatabaseControllerTest() {
        databaseController = mockDatabaseController;
        when(mockDatabaseController.getDatabaseEventService()).thenReturn(mockDatabaseEventService);
        tearDownClass();
    }

    @Test
    public void tearDownClassSiteMinderTest() {
        when(mockWebEventController.isSiteMinderEnabled()).thenReturn(true);
        webEventController = mockWebEventController;
        when(webEventController.getWebEventService()).thenReturn(mockWebEventService);
        tearDownClass();
    }

    @Test
    public void tearDownClassMissingDatabaseControllerTest() {
        databaseController = null;
        when(mockDatabaseController.getDatabaseEventService()).thenReturn(null);
        tearDownClass();
    }

    @Test
    public void webServiceActionBaseCommandTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME);
        Assert.assertNull(response);
    }

    @Test
    public void webServiceActionBaseCommandAndBodyTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, new ArrayList<>(Arrays.asList("foo", "bar")));
        Assert.assertNull(response);
    }

    @Test
    public void webServiceActionWitCredentialsCommandTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(mockCredentials, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME);
        Assert.assertNull(response);
    }

    @Test
    public void webServiceActionWithRequestListBodyBaseCommandTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(mockCredentials, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, new ArrayList<>(Arrays.asList("foo", "bar")));
        Assert.assertNull(response);
    }

    @Test
    public void webServiceActionWithRequestStringBodyBaseCommandTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(mockCredentials, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, "bar");
        Assert.assertNull(response);
    }

    @Test
    public void webServiceActionOAuthBaseCommandTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(mockOAuthKey, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME);
        Assert.assertNull(response);
    }

    @Test
    public void webServiceActionOAuthWithRequestBodyBaseCommandTest() {
        webServiceController = mockWebServiceController;
        Object response = webServiceAction(mockOAuthKey, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, new ArrayList<>(Arrays.asList("foo", "bar")));
        Assert.assertNull(response);
    }

    @Test(expected = NullPointerException.class)
    public void databaseActionInvalidQueryTest() {
        databaseController = mockDatabaseController;
        Object response = databaseAction(QueryCommand.QUERY_MODEL_RECOMMENDATION, 1);
        Assert.assertNull(response);
    }

    @Test(expected = NullPointerException.class)
    public void databaseActionMongoTest() {
        databaseController = mockDatabaseController;
        databaseAction("Patents", new BasicDBObject("docket", "1234"));
    }

}
