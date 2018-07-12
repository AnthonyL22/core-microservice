package com.pwc.core.framework.processors.rest;

import com.jayway.restassured.path.json.JsonPath;
import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.command.WebServiceCommand;
import com.pwc.core.framework.data.OAuthKey;
import com.pwc.core.framework.data.SmSessionKey;
import com.pwc.core.framework.util.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebServiceProcessorTest {

    private final String URL_PATH = "http://foobar-web-services.com";
    private final String USER = "foo";
    private final String PASS = "foo";

    private WebServiceProcessor webServiceProcessor;
    private HashMap<String, Object> mockUserNameMap = new HashMap<>();
    private List<String> mockUserNameList = new ArrayList<>();

    private CloseableHttpClient mockCloseableHttpClient;
    private CloseableHttpResponse mockCloseableHttpResponse;
    private HttpEntity mockHttpEntity;

    private final String EXPECTED_JSON_RESPONSE = "{\n" +
            "    \"id\": 23850994,\n" +
            "    \"lastName\": \"Doe\",\n" +
            "    \"qcguid\": 478182,\n" +
            "    \"userName\": \"johnd\",\n" +
            "    \"emailAddress\": \"johnd@mywebsite.com\",\n" +
            "    \"identifyingValue\": \"23850994\",\n" +
            "    \"fullName\": \"John N. Doe\",\n" +
            "    \"displayValue\": \"John N. Doe\",\n" +
            "    \"displayFullName\": \"John N. Doe\",\n" +
            "    \"firstName\": \"John N.\"\n" +
            "}";
    private HttpRequestBase mockHttpRequestBase;
    private OAuthKey oAuthKey;
    private SmSessionKey smSessionKey;

    public enum UsersWebServiceWebServiceCommand implements WebServiceCommand {

        POST_ADD_USER_ID(FrameworkConstants.POST_REQUEST, "rest/users", "addUser"),
        GET_ADD_USER_ID(FrameworkConstants.GET_REQUEST, "rest/users", "addUser"),
        GET_BY_LAST_NAME(FrameworkConstants.GET_REQUEST, "rest/users", "byLastName"),
        PUT_BY_LAST_NAME(FrameworkConstants.PUT_REQUEST, "rest/users", "byLastName"),
        DELETE_BY_LAST_NAME(FrameworkConstants.DELETE_REQUEST, "rest/users", "byLastName"),
        POST_LAST_NAME(FrameworkConstants.POST_REQUEST, "rest/users", "byLastName");

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

    public enum APIWebServiceCommand implements WebServiceCommand {

        SEARCH(FrameworkConstants.GET_REQUEST, "jira/rest/api/2/search?jql=", "key="),
        MY_CLAIMS(FrameworkConstants.GET_REQUEST, "myClaims", "");

        private String requestMethodType;
        private String requestMapping;
        private String methodName;

        APIWebServiceCommand(String type, String mapping, String name) {
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

        mockCloseableHttpClient = mock(CloseableHttpClient.class);
        mockCloseableHttpResponse = mock(CloseableHttpResponse.class);
        mockHttpEntity = mock(HttpEntity.class);
        webServiceProcessor = mock(WebServiceProcessor.class, Mockito.CALLS_REAL_METHODS);

        mockUserNameList.add("Jones");
        mockUserNameList.add("32137");

        mockUserNameMap.put("lastName", "Jones");
        mockUserNameMap.put("zip", 32137);

        mockHttpRequestBase = mock(HttpRequestBase.class);
        URI expectedURI = null;
        try {
            expectedURI = new URI(URL_PATH + "/rest/users/byLastName");
        } catch (URISyntaxException e) {
            e.getMessage();
        }
        when(mockHttpRequestBase.getURI()).thenReturn(expectedURI);

        oAuthKey = new OAuthKey("oijkjlkjkj43lkjjuyoiyfghuiopkl67");
        smSessionKey = new SmSessionKey("UYTYIUHIJOKL6t5ryuhiujj576576768798");

    }

    @Test
    public void executeWithSmSessionKeyTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", smSessionKey, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID);
        Assert.assertNotNull(response);
    }

    @Test
    public void executeWithSmSessionKeyWithPathParameterTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", smSessionKey, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, "foobar");
        Assert.assertNotNull(response);
    }

    @Test
    public void executeWithSmSessionKeyWithAllParametersTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", smSessionKey, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, "foobar", Arrays.asList("hello"));
        Assert.assertNotNull(response);
    }

    @Test
    public void executeWithOAuthTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", oAuthKey, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID);
        Assert.assertNotNull(response);
    }

    @Test
    public void executeWithOAuthWithPathParameterTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", oAuthKey, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, "foobar");
        Assert.assertNotNull(response);
    }

    @Test
    public void executeWithOAuthWithAllParametersTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", oAuthKey, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, "foobar", Arrays.asList("hello"));
        Assert.assertNotNull(response);
    }

    @Test()
    public void setHeaderCredentialsAllInfoTest() {
        HttpGet mockHttpGet = new HttpGet();
        mockHttpGet = (HttpGet) webServiceProcessor.setHeaderCredentials(USER, PASS, mockHttpGet);
        Assert.assertEquals(mockHttpGet.getAllHeaders().length, 2);
        Assert.assertTrue(mockHttpGet.getHeaders("Authorization")[0].getValue().contains("Basic"));
    }

    @Test()
    public void setHeaderCredentialsMissingUsernameTest() {
        HttpGet httpGet = (HttpGet) webServiceProcessor.setHeaderCredentials(USER, PASS, null);
        Assert.assertNull(httpGet);
    }

    @Test
    public void getWebServiceResponseHttpEntityTest() {
        StatusLine mockStatusLine = mock(StatusLine.class);
        when(mockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        when(mockStatusLine.getReasonPhrase()).thenReturn("Bad Request");
        when(mockCloseableHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        JsonPath response = (JsonPath) webServiceProcessor.getWebServiceResponse(mockCloseableHttpResponse, mockHttpEntity, null);
        Assert.assertEquals("400", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Bad Request", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test
    public void getWebServiceResponseTest() {
        StatusLine mockStatusLine = mock(StatusLine.class);
        when(mockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(mockStatusLine.getReasonPhrase()).thenReturn("OK");
        when(mockCloseableHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        JsonPath response = (JsonPath) webServiceProcessor.getWebServiceResponse(mockCloseableHttpResponse, null, null);
        Assert.assertEquals("200", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("OK", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeHashMapPOSTTest() {
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, 1234, mockUserNameMap);
        Assert.assertEquals("404", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeListPOSTTest() {
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, 1234, mockUserNameList);
        Assert.assertEquals("404", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeJSONStringPOSTTest() {
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.POST_ADD_USER_ID, 1234, EXPECTED_JSON_RESPONSE);
        Assert.assertEquals("404", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeLongArgsGETTest() {
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.GET_ADD_USER_ID, 1234, mockUserNameMap);
        Assert.assertEquals("404", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test
    public void constructUriFromPayloadMapEncodedExceptionTest() throws URISyntaxException {
        HashMap<String, Object> mockUserNameMap = new HashMap<>();
        mockUserNameMap.put("lastName", "Lombardo Ant");
        mockUserNameMap.put("zip", 32137);
        URI encodedURI = new URI(URL_PATH + "/rest/users/byLastName?collection%3Dfoobar&type%3DMyProjectDocument");
        HttpRequestBase baseHttpRequest = mock(HttpRequestBase.class);
        when(baseHttpRequest.getURI()).thenReturn(encodedURI);
        URI result = webServiceProcessor.constructUriFromPayloadMap(baseHttpRequest, mockUserNameMap);

        Assert.assertEquals(result.toString(), mockHttpRequestBase.getURI() + "?collection%3Dfoobar&type%3DMyProjectDocument&zip=32137&lastName=Lombardo+Ant");
    }

    @Test
    public void constructUriFromPayloadMapEncodedTest() throws URISyntaxException {
        URI encodedURI = new URI(URL_PATH + "/rest/users/byLastName?collection%3Dfoobar&type%3DMyProjectDocument");
        HttpRequestBase baseHttpRequest = mock(HttpRequestBase.class);
        when(baseHttpRequest.getURI()).thenReturn(encodedURI);
        URI result = webServiceProcessor.constructUriFromPayloadMap(baseHttpRequest, mockUserNameMap);
        Assert.assertEquals(result.toString(), mockHttpRequestBase.getURI() + "?collection=foobar&type=MyProjectDocument&zip=32137&lastName=Jones");
    }

    @Test
    public void constructUriFromPayloadMapDecodedTest() throws URISyntaxException {
        URI decodedURI = new URI(URL_PATH + "/rest/users/byLastName?collection=foobar&type=MyProjectDocument");
        HttpRequestBase decodedHttpRequest = mock(HttpRequestBase.class);
        when(decodedHttpRequest.getURI()).thenReturn(decodedURI);
        URI result = webServiceProcessor.constructUriFromPayloadMap(decodedHttpRequest, mockUserNameMap);
        Assert.assertEquals(result.toString(), mockHttpRequestBase.getURI() + "?collection=foobar&type=MyProjectDocument&zip=32137&lastName=Jones");
    }

    @Test
    public void constructUriFromPayloadMapTest() {
        URI result = webServiceProcessor.constructUriFromPayloadMap(mockHttpRequestBase, mockUserNameMap);
        Assert.assertEquals(result.toString(), mockHttpRequestBase.getURI() + "?zip=32137&lastName=Jones");
    }

    @Test
    public void constructUriFromNullPayloadMapTest() {
        URI result = webServiceProcessor.constructUriFromPayloadMap(mockHttpRequestBase, null);
        Assert.assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void executeWithParametersExceptionTest() {
        when(webServiceProcessor.getAuthenticatedClient("", USER, PASS)).thenReturn(mockCloseableHttpClient);
        JsonPath response = (JsonPath) webServiceProcessor.execute("", USER, PASS, UsersWebServiceWebServiceCommand.POST_LAST_NAME, "jones");
        Assert.assertEquals("404", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeWithParametersPOSTTest() {
        when(webServiceProcessor.getAuthenticatedClient(URL_PATH, USER, PASS)).thenReturn(mockCloseableHttpClient);
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.POST_LAST_NAME, "jones");
        Assert.assertEquals("405", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Allowed", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeWithParametersGETTest() {
        when(webServiceProcessor.getAuthenticatedClient(URL_PATH, USER, PASS)).thenReturn(mockCloseableHttpClient);
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, "jones");
        Assert.assertEquals("405", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeWithParametersPUTTest() {
        when(webServiceProcessor.getAuthenticatedClient(URL_PATH, USER, PASS)).thenReturn(mockCloseableHttpClient);
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.PUT_BY_LAST_NAME, "jones");
        Assert.assertEquals("405", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Method Not Allowed", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test(expected = AssertionError.class)
    public void executeWithParametersDELETETest() throws IOException {
        when(webServiceProcessor.getAuthenticatedClient(URL_PATH, USER, PASS)).thenReturn(mockCloseableHttpClient);
        HttpDelete mockHttpDelete = mock(HttpDelete.class);
        when(mockCloseableHttpClient.execute(mockHttpDelete)).thenReturn(mockCloseableHttpResponse);
        webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.DELETE_BY_LAST_NAME, "jones");
    }

    @Test(expected = AssertionError.class)
    public void executeWithParametersMapEmptyTest() {
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME);
        Assert.assertTrue(Integer.valueOf(response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY)) > 400);
    }

    @Test(expected = AssertionError.class)
    public void executeWithoutParametersTest() {
        JsonPath response = (JsonPath) webServiceProcessor.execute(URL_PATH, USER, PASS, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, null);
        Assert.assertEquals("404", response.getString(FrameworkConstants.HTTP_STATUS_VALUE_KEY));
        Assert.assertEquals("Not Found", response.getString(FrameworkConstants.HTTP_STATUS_REASON_PHRASE_KEY));
    }

    @Test
    public void executeUrlOnlyWithCredsTest() {
        Object response = webServiceProcessor.execute("http://www.foobar.com", USER, PASS);
        Assert.assertNotNull(response);
    }

    @Test
    public void isValidJsonGoodJsonTest() {
        boolean result = webServiceProcessor.isValidJson(EXPECTED_JSON_RESPONSE);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidJsonTextTest() {
        boolean result = webServiceProcessor.isValidJson(RandomStringUtils.randomAlphabetic(100));
        Assert.assertFalse(result);
    }

    @Test
    public void isValidJsonExceptionTest() {
        boolean result = webServiceProcessor.isValidJson("{abc");
        Assert.assertFalse(result);
    }

    @Test
    public void constructRestUrlTest() {
        String result = webServiceProcessor.constructRestUrl(URL_PATH, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, null);
        Assert.assertEquals(URL_PATH + "/rest/users/byLastName", result);
    }

    @Test
    public void constructRestUrlWithPathParameterTest() {
        String result = webServiceProcessor.constructRestUrl(URL_PATH, UsersWebServiceWebServiceCommand.GET_BY_LAST_NAME, "jones");
        Assert.assertEquals(URL_PATH + "/rest/users/byLastName/jones", result);
    }

    @Test
    public void constructRestUrlEqualsSignsInQueryStringTest() {
        String result = webServiceProcessor.constructRestUrl(URL_PATH, APIWebServiceCommand.SEARCH, "MYNUMBER-17");
        Assert.assertEquals(URL_PATH + "/jira/rest/api/2/search?jql=key=MYNUMBER-17", result);
    }

    @Test
    public void constructRestUrlQuestionMarkTest() {
        String result = webServiceProcessor.constructRestUrl(URL_PATH, APIWebServiceCommand.MY_CLAIMS, "size=1");
        Assert.assertEquals(URL_PATH + "/myClaims?size=1", result);
    }

    @Test
    public void constructRestUrlNoQuestionMarkTest() {
        String result = webServiceProcessor.constructRestUrl(URL_PATH, APIWebServiceCommand.MY_CLAIMS, "1");
        Assert.assertEquals(URL_PATH + "/myClaims/1", result);
    }

}
