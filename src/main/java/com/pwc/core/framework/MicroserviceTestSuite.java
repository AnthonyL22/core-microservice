package com.pwc.core.framework;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.pwc.assertion.AssertService;
import com.pwc.core.framework.command.DatabaseCommand;
import com.pwc.core.framework.command.WebServiceCommand;
import com.pwc.core.framework.controller.DatabaseController;
import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.controller.WebServiceController;
import com.pwc.core.framework.data.Credentials;
import com.pwc.core.framework.data.OAuthKey;
import com.pwc.core.framework.data.PropertiesFile;
import com.pwc.core.framework.listeners.MicroserviceTestListener;
import com.pwc.core.framework.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.util.HashMap;
import java.util.List;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

@Category(SystemTestCategory.class)
@Listeners(MicroserviceTestListener.class)
public abstract class MicroserviceTestSuite {

    private AbstractApplicationContext ctx;

    @Autowired
    protected AssertService assertService;

    @Autowired
    protected WebEventController webEventController;

    @Autowired
    protected WebServiceController webServiceController;

    @Autowired
    protected DatabaseController databaseController;

    private String currentJobId;

    public abstract void beforeMethod();

    public abstract void afterMethod();

    public void beforeClass() {
    }

    public void afterClass() {
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpMethod() {
        try {
            this.beforeMethod();
        } catch (Exception e) {
            e.printStackTrace();
            LOG(true, "setUpMethod() FAILED. Test='%s' Exception='%s'", StringUtils.substringAfterLast(this.getClass().getName(), "."), e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod() {
        try {
            this.afterMethod();
        } catch (Exception e) {
            e.printStackTrace();
            LOG(true, "tearDownMethod() FAILED. Test='%s' Exception='%s'", StringUtils.substringAfterLast(this.getClass().getName(), "."), e.getMessage());
        }
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = "setUpRunner")
    public void setUpClass() {

        setEnvironmentDefaults();

        try {
            LOG(true, StringUtils.repeat("-", 78));
            LOG(true, String.format("\n Currently Running Test='%s'\n", StringUtils.substringAfterLast(this.getClass().getName(), ".")));
            LOG(true, StringUtils.repeat("-", 78));
        } catch (Exception e) {
            e.printStackTrace();
            LOG(true, "setUpClass() FAILED. Test='%s' Exception='%s'", StringUtils.substringAfterLast(this.getClass().getName(), "."), e.getMessage());
            throw e;
        }
    }

    /**
     * Set any system environment variables to default settings
     */
    private void setEnvironmentDefaults() {
        if (StringUtils.isEmpty(System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT))) {
            System.setProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT, FrameworkConstants.DEV_ENVIRONMENT_KEY);
        }
    }

    @BeforeClass(alwaysRun = true)
    public void setUpRunner() {
        try {
            String[] automationContext = {FrameworkConstants.AUTOMATION_CONTEXT_XML};
            ctx = new ClassPathXmlApplicationContext(automationContext);
            ctx.registerShutdownHook();
            setUpAssertionModel();
        } catch (Throwable e) {
            e.printStackTrace();
            LOG(true, "setUpRunner() FAILED. TEST='%s' EXCEPTION='%s'", StringUtils.substringAfterLast(this.getClass().getName(), "."), e.getMessage());
            throw e;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        try {
            if (webEventController != null) {
                webEventController.performQuit();
            }
            if (databaseController != null) {
                if (databaseController.getDatabaseEventService() != null) {
                    databaseController.closeDatabaseConnection();
                }
            }

            //ToDo: kill httpEventController sessions?

        } catch (Throwable e) {
            LOG(true, "tearDownClass() FAILED. TEST='%s' EXCEPTION='%s'", StringUtils.substringAfterLast(this.getClass().getName(), "."), e.getMessage());
        }
    }

    /**
     * Initialize a web page with no default interaction. Typically used for SiteMinder log-in page
     *
     * @param credentials Credentials to authenticate with
     */
    protected void webAction(final Credentials credentials) {
        if (webEventController == null) {
            webEventController = (WebEventController) ctx.getBean("webEventController");
            webEventController.initiateBrowser(credentials);
            setCurrentJobId(webEventController.getCurrentJobId());
        }
    }

    /**
     * DOM action on an element that does not require any supporting
     * data.  Typically a button click
     *
     * @param elementIdentifier unique identifier for an element
     * @return time in milliseconds for mouse-based action
     */
    protected long webAction(final String elementIdentifier) {
        return webAction(elementIdentifier, null);
    }

    /**
     * DOM action on an element that requires a true/false switch.  Typically,
     * a checkbox or radio button element type
     *
     * @param elementIdentifier unique identifier for an element
     * @param attributeValue    toggled state of an element
     * @return time in milliseconds for mouse-based action
     */
    protected long webAction(final String elementIdentifier, final boolean attributeValue) {
        return webAction(elementIdentifier, String.valueOf(attributeValue));
    }

    /**
     * DOM action for all elements
     *
     * @param elementIdentifier unique identifier for an element
     * @param attributeValue    element value to alter in the active DOM
     * @return time in milliseconds for mouse-based action
     */
    protected long webAction(final String elementIdentifier, final Object attributeValue) {
        if (webEventController == null) {
            webEventController = (WebEventController) ctx.getBean("webEventController");
            webEventController.initiateBrowser(null);
            setCurrentJobId(webEventController.getCurrentJobId());
        }
        webEventController.getWebEventService().waitForBrowserToLoad();
        webEventController.getWebEventService().waitForElementToDisplay(elementIdentifier);
        WebElement webElement = webEventController.getWebEventService().findWebElement(elementIdentifier);
        if (webElement != null) {
            return webEventController.webAction(webElement, attributeValue);
        } else {
            assertFail(String.format("Unable to find element=%s", elementIdentifier));
        }
        return 0L;
    }

    /**
     * Initialize a headless page based on user defined URL.  Typically used for testing custom web page endpoints
     *
     * @param credentials Credentials to authenticate with
     * @param url         web service URL
     * @return http/web service response
     */
    protected Object httpAction(final Credentials credentials, final String url) {
        if (webServiceController == null) {
            webServiceController = (WebServiceController) ctx.getBean("webServiceController");
        }
        return webServiceController.httpServiceAction(credentials, url);
    }

    /**
     * Initialize a headless page based on the web.services.url property defined in automation.properties file.
     * Typically used for testing a single page endpoint(s)
     *
     * @param credentials Credentials to authenticate with
     * @param url         web service URL
     * @return web service response
     */
    protected Object webServiceAction(final Credentials credentials, final String url) {
        if (webServiceController == null) {
            webServiceController = (WebServiceController) ctx.getBean("webServiceController");
        }
        return webServiceController.webServiceAction(credentials, url);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param command BaseGetCommand command type
     * @return web service response
     */
    protected Object webServiceAction(final WebServiceCommand command) {
        return webServiceAction(command, null);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param command     BaseGetCommand command type
     * @param requestBody POST request body
     * @return web service response
     */
    protected Object webServiceAction(final WebServiceCommand command, final Object requestBody) {
        if (requestBody instanceof HashMap || requestBody instanceof List) {
            return webServiceAction(command, null, requestBody);
        } else {
            return webServiceAction(command, requestBody, null);
        }
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param command      BaseGetCommand command type
     * @param parameterMap Name-Value pair filled map of parameters to send in HTTP request
     * @return web service response
     */
    protected Object webServiceAction(final WebServiceCommand command, final HashMap<String, Object> parameterMap) {
        return webServiceAction((Credentials) null, command, null, parameterMap);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param command       BaseGetCommand command type
     * @param pathParameter web service path parameter(s)
     * @param parameter     HashMap or simple request body arg to send in HTTP request
     * @return web service response
     */
    protected Object webServiceAction(final WebServiceCommand command, final Object pathParameter, final Object parameter) {
        return webServiceAction((Credentials) null, command, pathParameter, parameter);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param credentials Credentials to authenticate with
     * @param command     BaseGetCommand command type
     * @return web service response
     */
    protected Object webServiceAction(final Credentials credentials, final WebServiceCommand command) {
        return webServiceAction(credentials, command, null);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param credentials Credentials to authenticate with
     * @param command     BaseGetCommand command type
     * @param requestBody POST request body
     * @return web service response
     */
    protected Object webServiceAction(final Credentials credentials, final WebServiceCommand command, final Object requestBody) {
        if (requestBody instanceof HashMap || requestBody instanceof List) {
            return webServiceAction(credentials, command, null, requestBody);
        } else {
            return webServiceAction(credentials, command, requestBody, null);
        }
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param credentials  Credentials to authenticate with
     * @param command      BaseGetCommand command type
     * @param parameterMap Name-Value pair filled map of parameters to send in HTTP request
     * @return web service response
     */
    protected Object webServiceAction(final Credentials credentials, final WebServiceCommand command, final HashMap<String, Object> parameterMap) {
        return webServiceAction(credentials, command, null, parameterMap);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param credentials   Credentials to authenticate with
     * @param command       BaseGetCommand command type
     * @param pathParameter web service path parameter(s)
     * @param parameter     HashMap or simple request body arg to send in HTTP request
     * @return web service response
     */
    protected Object webServiceAction(final Credentials credentials, final WebServiceCommand command, final Object pathParameter, final Object parameter) {
        if (webServiceController == null) {
            webServiceController = (WebServiceController) ctx.getBean("webServiceController");
        }
        return webServiceController.webServiceAction(credentials, command, pathParameter, parameter);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param oAuthKey OAuth key
     * @param command  BaseGetCommand command type
     * @return web service response
     */
    protected Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command) {
        return webServiceAction(oAuthKey, command, null);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param oAuthKey    OAuth key
     * @param command     BaseGetCommand command type
     * @param requestBody POST request body
     * @return web service response
     */
    protected Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command, final Object requestBody) {
        if (requestBody instanceof HashMap || requestBody instanceof List) {
            return webServiceAction(oAuthKey, command, null, requestBody);
        } else {
            return webServiceAction(oAuthKey, command, requestBody, null);
        }
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param oAuthKey     OAuth key
     * @param command      BaseGetCommand command type
     * @param parameterMap Name-Value pair filled map of parameters to send in HTTP request
     * @return web service response
     */
    protected Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command, final HashMap<String, Object> parameterMap) {
        return webServiceAction(oAuthKey, command, null, parameterMap);
    }

    /**
     * Send a REST ws action to a service End Point
     *
     * @param oAuthKey      OAuth key
     * @param command       BaseGetCommand command type
     * @param pathParameter web service path parameter(s)
     * @param parameter     HashMap or simple request body arg to send in HTTP request
     * @return web service response
     */
    protected Object webServiceAction(final OAuthKey oAuthKey, final WebServiceCommand command, final Object pathParameter, final Object parameter) {
        if (webServiceController == null) {
            webServiceController = (WebServiceController) ctx.getBean("webServiceController");
        }
        return webServiceController.webServiceAction(oAuthKey, command, pathParameter, parameter);
    }

    /**
     * Ability to update, insert, delete, and query the database under test
     *
     * @param sqlCommand      Parameter-decorated SQL Statement
     * @param queryParameters query parameters for substitution
     * @return result set
     */
    protected Object databaseAction(final DatabaseCommand sqlCommand, final Object... queryParameters) {
        if (databaseController == null) {
            databaseController = (DatabaseController) ctx.getBean("databaseController");
        }
        databaseController.establishDatabaseConnection(ctx);
        return databaseController.getDatabaseEventService().executeParameterQuery(sqlCommand.sql(), queryParameters);
    }

    /**
     * Ability to query a MongoDB database under test
     *
     * @param collectionName database collection name
     * @param query          BasicDBObject query
     * @return DBCursor database result cursor
     */
    protected DBCursor databaseAction(final String collectionName, final BasicDBObject query) {
        if (databaseController == null) {
            databaseController = (DatabaseController) ctx.getBean("databaseController");
        }
        databaseController.establishMongoDatabaseConnection(ctx);
        return databaseController.getDatabaseEventService().executeQuery(collectionName, query);
    }

    /**
     * Set the assertion to be either HARD or SOFT assertions based on automation.properties file settings for
     * the profile under test
     */
    private void setUpAssertionModel() {
        assertService = (AssertService) ctx.getBean("assertService");
        StringBuilder props = new StringBuilder("config/");
        props.append(System.getProperty(FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT));
        props.append("/");
        props.append(PropertiesFile.AUTOMATION_PROPERTIES_FILE.fileName);
        assertService.setEnableHardAssertions(Boolean.valueOf(PropertiesUtils.getPropertyFromPropertiesFile(props.toString(), "enable.hard.assert")));
    }

    public void setCtx(AbstractApplicationContext ctx) {
        this.ctx = ctx;
    }

    public AbstractApplicationContext getCtx() {
        return ctx;
    }

    public String getCurrentJobId() {
        return currentJobId;
    }

    public void setCurrentJobId(String currentJobId) {
        this.currentJobId = currentJobId;
    }

}
