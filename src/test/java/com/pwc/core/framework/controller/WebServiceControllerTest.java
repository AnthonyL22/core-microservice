package com.pwc.core.framework.controller;

import com.jayway.restassured.path.json.JsonPath;
import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.command.WebServiceCommand;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.OAuthKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebServiceControllerTest extends BaseWebServiceTest {

    private static final JsonPath JSON_USER_RESPONSE = new JsonPath("{\n" +
            "  \"id\": 23850994,\n" +
            "  \"qcguid\": 478182,\n" +
            "  \"firstName\": \"John N.\",\n" +
            "  \"lastName\": \"Doe\",\n" +
            "  \"emailAddress\": \"johnd@mywebsite.com\",\n" +
            "  \"fullName\": \"John N. Doe\",\n" +
            "  \"userName\": \"johnd\",\n" +
            "  \"displayFullName\": \"John N. Doe\",\n" +
            "  \"identifyingValue\": \"23850994\",\n" +
            "  \"displayValue\": \"John N. Doe\"\n" +
            "}");

    private HashMap<String, Object> parameters;
    private WebServiceController mockWebServiceController;
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

    @Before
    public void setUp() {
        parameters = new HashMap<>();
        parameters.put("lastName", "Doe");
        mockWebServiceController = mock(WebServiceController.class);
        mockCredentials = new Credentials("foo", "bar");
        mockOAuthKey = mock(OAuthKey.class);
    }

    @Test
    public void webServiceTest() {
        when(mockWebServiceController.webServiceAction(mockCredentials, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, null, parameters)).thenReturn(JSON_USER_RESPONSE);
        JsonPath response = (JsonPath) mockWebServiceController.webServiceAction(mockCredentials, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, null, parameters);
        Assert.assertNotNull("Verify id field", response.getInt("id"));
        Assert.assertNotNull("Verify qcquid field", response.getInt("qcguid"));
        Assert.assertNotNull("Verify identifyingValue field", response.getInt("identifyingValue"));
        Assert.assertEquals("Verify lastName field", response.get("lastName"), parameters.get("lastName").toString());
        Assert.assertEquals("Verify userName field", response.get("userName"), "johnd");
        Assert.assertEquals("Verify emailAddress field", response.get("emailAddress"), "johnd@mywebsite.com");
        Assert.assertEquals("Verify fullName field", response.get("fullName"), "John N. Doe");
        Assert.assertEquals("Verify displayValue field", response.get("displayValue"), "John N. Doe");
        Assert.assertEquals("Verify displayFullName field", response.get("displayFullName"), "John N. Doe");
        Assert.assertEquals("Verify firstName field", response.get("firstName"), "John N.");
    }

    @Test
    public void webServiceOAuthTest() {
        when(mockWebServiceController.webServiceAction(mockOAuthKey, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME)).thenReturn(JSON_USER_RESPONSE);
        JsonPath response = (JsonPath) mockWebServiceController.webServiceAction(mockOAuthKey, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME);
        Assert.assertNotNull("Verify id field", response.getInt("id"));
    }

    @Test
    public void webServiceOAuthWithRequestBodyTest() {
        when(mockWebServiceController.webServiceAction(mockOAuthKey, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, "foobar")).thenReturn(JSON_USER_RESPONSE);
        JsonPath response = (JsonPath) mockWebServiceController.webServiceAction(mockOAuthKey, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, "foobar");
        Assert.assertNotNull("Verify id field", response.getInt("id"));
    }

}
