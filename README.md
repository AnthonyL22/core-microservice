[![Maven Central](https://img.shields.io/maven-central/v/com.pacificwebconsulting.core/core-microservice.svg)](https://img.shields.io/maven-central/v/com.pacificwebconsulting.core/core-microservice.svg)

[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

Got a question?  [Email us](http://www.pacificwebconsulting.com/contact/) or reach out on [LinkedIn](https://www.linkedin.com/in/alombardo/) 

# Introduction

The Core Automated Testing microservice is used to the perform functional system tests on any desktop or mobile browser, REST service and database endpoints.   

This service is an on-ramp for entire engineering teams to contribute to the functional testing of a product offering.  Also, to unify our testing teams with one automated testing solution.

Current capabilities include:

* Browser (Internet Explorer, Firefox, Headless Firefox, Chrome, Headless Chrome, Edge, Headless Edge, Safari)
* Mobile Browser (Android, iOS using Appium)
* REST Web Services (SiteMinder Authenticated) 
* Database (Oracle, MongoDB, MS SQL, any JDBC connection)

# Questions and issues

The [github issue tracker](https://github.com/AnthonyL22/core-microservice/issues) is **_only_** for bug reports and 
feature requests. Anything else, such as questions for help in using the library, should be [emailed to our team](http://www.pacificwebconsulting.com/contact/).

# Prerequisites

1. Java 11
2. Maven 3.x

# Architecture Diagram
![Sample](docs/core-microservice-flow-diagram.png "Microservice Flow Diagram")

# Maven Dependency

[![Maven Central](https://img.shields.io/maven-central/v/com.pacificwebconsulting.core/core-microservice.svg)](https://img.shields.io/maven-central/v/com.pacificwebconsulting.core/core-microservice.svg)

```
<dependency>
    <groupId>com.pacificwebconsulting.core</groupId>
    <artifactId>core-microservice</artifactId>
    <version>2.0.x</version>
</dependency>
```

# Integration

There are two classes to choose from when implementing this service.  You must choose either of the following
classes to extend depending on the nomenclature you would prefer in your test scripts:

* *WebTestCase* - uses Selenium-like terms and expressions (Ex: webElementExists();)
* *DeclarativeTestCase* - more direct description of the actions being performed (Ex: exists();)

Simply, extend the class WebTestCase OR DeclarativeTestCase as shown below:

```
public abstract class MyTestCase extends WebTestCase {
    /**
     * Product specific functionality should go in here
     */
}
```

# Browsers support

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt="IE / Edge" width="24px" height="24px" />](https://www.selenium.dev/documentation/webdriver/browsers/)<br/>IE / Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](https://www.selenium.dev/documentation/webdriver/browsers/)<br/>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](https://www.selenium.dev/documentation/webdriver/browsers/)<br/>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](https://www.selenium.dev/documentation/webdriver/browsers/)<br/>Safari |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Latest                                                                                                                                                                                                                    | Latest                                                                                                                                                                                                                      | Latest                                                                                                                                                                                                                  | Latest                                                                                                                                                                                                                  |


# Special Features
The Core Automation Service has many special features that have been born from years of necessity.  Here are just a few.  

## Retry Failed Test Feature
Using TestNG's retryAnalyzer, you can retry those Flaky tests that maybe require a quick re-run.  By default, if the
retryAnalyzer is set your test will run 1 time.  However, you can use the OPTIONAL, custom @MaxRetryCount annotation if you'd 
like to retry N number of times.

### Usage
```
@MaxRetryCount(5)
@Test(retryAnalyzer = Retry.class, groups = {Groups.WEB_SERVICE_TEST, Groups.REGRESSION_TEST})
public void testSolrSearch() {
    My Test Here
}
```

## Zephyr Test Case Annotation & ZAPI Connectivity
The @TestCase annotation is a custom annotation for automatically updating Zephyr test case status' in Jira based on the test case
ID(s) defined in individual tests.  The test case ID(s) defined in the annotation must be comma or space separated and match the 
exact ID displayed in Jira/Zephyr.  For example:
```
@TestCase("CIA-14677, JIRA-12245 ANT-777")
@Test(retryAnalyzer = Retry.class, groups = {Groups.WEB_SERVICE_TEST, Groups.REGRESSION_TEST})
public void testSolrSearch() {
    My Test Here
}
```

Simply, add the following items in the automation.properties file in your configuration to begin updating your Zephyr test cases status':
```
jira.zapi.enabled=true
jira.zapi.url=https://jira.pwc.com/rest
jira.zapi.username=anthony
jira.zapi.password=pass1234
jira.zapi.cycle.name=MyProject-1.0.0
```

## Issue Annotation(s)
The @Issue annotation is a customized annotation for automatically adding logging information which identify the 
issue or story numbers relevant to your test.  Very useful for Jira integration.  

```
@Issue("STORY-1234")
@Test(retryAnalyzer = Retry.class, groups = {Groups.WEB_SERVICE_TEST, Groups.REGRESSION_TEST})
public void testSolrSearch() {
    My Test Here
}
```

### Sample Log Output
```
2017-10-31 15:29:55.629  BookmarkTest                        testBookmark--Issue(s): STORY-1234
2017-10-31 15:29:55.630  BookmarkTest                        testBookmark--Executed on Date/Time: 2017-10-31 15:29:55.630
2017-10-31 15:29:55.630  BookmarkTest                        testBookmark--Test Passed
```

## Performance Feature
The Core Automation Service provides highly useful performance metrics to the end user (you).  Using the *webAction(), 
httpAction(), webServiceAction()* methods will return the execution response times of each activity.  Only mouse-based GUI, web actions
are measured for performance and will return a response time while using *webAction()*.

For example: A timer is started before the mouse click, next there is logic that waits for the AUT to finish all 
ajax requests, then the timer is stopped, and finally this response time metric is returned to the calling test.

All web service activities are measured and the returned JSON response holds the response time as a field you can then
interrogate.  See example below.

### Web UI Usage
```
long responseTime = webAction("//a[@id='MyAnchor']")
// Assert if the returned response time is acceptable
```

#### WebElement Appear OR Disappear
There are also two helpful methods that calculate the time (in seconds) how long it takes for a WebElement to 
appear OR disappear from being visible to the user.  These utility methods rely on the .isDisplayed() property in 
Selenium for registering when an element is actually visible or not visible to the user.

```
int duration = durationForElementToAppear("//div[text()='Home Page']")
// Assert if the returned response time is acceptable
```

```
int duration = durationForElementToDisappear("//div[text()='Wait while Loading...']")
// Assert if the returned response time is acceptable
```


#### Web Diagnostics Usage
We also provide the ability to validate the Console & Network tab activities in your browser.  These are increasingly handy 
if you need to monitor/test your web application at a lower level.
```
webDiagnosticsConsole...() - validate your browser's Console tab Activities
webDiagnosticsRequest...() - validate your browser's Network tab Activities
```

### HTML Web Usage
Utilizes the Apache HttpClient to open and optionally authenticate a given URL headlessly.  Next this operation returns
the entire HTML DOM object as a String.  The HTML response object is returned for the tester to then 
operate on accordingly.

```
Credentials myCredentials = new Credentials("anthony", "password");
String rawPageHtml = htmlAction(myCredentials, "www.mywebsite.com");
// Perform any String-based operations on the resultig page HTML
``` 

```
String rawPageHtml = htmlAction(null, "www.mywebsite.com");
// Perform any String-based operations on the resultig page HTML
``` 

### HTTP Web Usage
Utilizes the Apache HttpClient to open and optionally authenticate a given URL headlessly.  A JSON response object is
returned for the tester to then operate on accordingly.

```
Credentials myCredentials = new Credentials("anthony", "password");
JsonPath response = (JsonPath) httpAction(myCredentials, "http://www.myemail.com");
long responseTime = response.get(FrameworkConstants.HTTP_RESPONSE_TIME_KEY)
```

```
JsonPath response = (JsonPath) httpAction(null, "http://www.myemail.com");
long responseTime = response.get(FrameworkConstants.HTTP_RESPONSE_TIME_KEY)
```

### Web Service Usage
```
JsonPath response = (JsonPath) webServiceAction(SolrWebServiceCommand.SEARCH);
long responseTime = response.get(FrameworkConstants.HTTP_RESPONSE_TIME_KEY)
// Assert if the returned response time is acceptable
```

#### Web Service Authentication
The following security options are available to webServiceAction() users:

* OAuth2
* Custom Header key/value pair authentication
* Site Minder
* Basic (Base64 Encoded)
* Open

##### OAuth2 Security
To use OAuth security simply provide the OAuth key when calling webServiceAction().  The service will decorate your 
request header with a Authorization header of 'Bearer YOUR_OAUTH_KEY' prior to calling the web service.

##### Custom Header Security
To use custom header security simply provide a hydrated HeaderKeysMap object contains a HashMap of unique Header Key/Value
pairs that will be added to the requests when calling webServiceAction().  The service will decorate your request headers
with all key/value pairs prior to calling the web service.

##### Site Minder Security
To use Site Minder security simply provide the Site Minder key when calling webServiceAction().  The service will 
decorate your request header with a 'Cookie' header of 'SMSESSION= YOUR_SM_SESSION_KEY_KEY' prior to calling the web service.

##### Basic Security
To use basic security simply provide a decorated Credentials object when calling webServiceAction().  The service 
will decorate your request with Base64 encoded credentials in the Authorization header of 'Basic YOUR_BASE64_ENCODED_CREDENTIALS' 
prior to calling the web service.


## Utility Features
This service provides a [diverse Utilities set](https://github.com/AnthonyL22/core-microservice/tree/master/src/main/java/com/pwc/core/framework/util) 
to assist in nearly every need of a quality initiative.

* CollectionUtils
* DateUtils
* DebuggingUtils 
* EmailUtils 
* ExcelUtils 
* FileUtils
* GridUtils
* JsonUtils
* ObjectUtils
* PropertiesUtils
* RandomStringUtils
* StringUtils
* TableUtils
* WebElementUtils

## Video Playback Feature
This service records the execution of an automated test and creates a video that users can then playback to watch the automated test.  
This service is capable of recording all concurrent tests which are executed simultaneously.  The resulting video files 
are stored in directories matching each test name in directory *target/test-classes/screenshots*.  Simply, enable the 
following setting in the automation.properties file in your configuration to begin recording test execution(s):

```
capture.video=false
```

## Log Export Feature

#### Prerequisite
* [Logging Service](https://github.com/AnthonyL22/logging-microservice) 

#### Usage
This utility interrogates all your test classes and translates your Gherkin logging into a manual test case document that
you can share with the rest of your team(s).  The utility also converts any variables you might have integrated into
your logging into the 'readable' variable values.

Mandatory Arguments:
```
"[0] = Source Test Directory (ex: com.google.automation.tests)
"[1] = Manual Test Report Name (ex: Manual_Test_Report.txt)
```

Optional Arguments:
Be sure to provide the optional arguments as seen below if you'd like to replace the 'readable' variable value defined
in potentially an interface such as:  String USER_NAME = "Anthony";
```
"[2] = OPTIONAL - Class that contain Constant 'Name=Value' pairs
"[3] = OPTIONAL - Class that contain Constant 'Name=Value' pairs
"[4] = ect...
```

#### Maven Configuration
Simply add this to a profile or goal in your POM.xml to leverage in your build process.  The Maven command is:

```
mvn clean install -PoutputManualTestCaseReport
```

**Required arguments:**
1. Complete classpath to where your test classes reside
2. Complete classpath to where your classes that contain Constant 'Name=Value' pairs reside
3. Report file name
4. OPTIONAL - Classes that contain Constant 'Name=Value' pairs

```
<profile>
  <id>outputManualTestCaseReport</id>
  <activation>
    <activeByDefault>false</activeByDefault>
  </activation>
  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>${exec-maven-plugin.version}</version>
        <executions>
          <execution>
            <id>output-manual-test-cases</id>
            <goals>
              <goal>java</goal>
            </goals>
            <phase>install</phase>
            <configuration>
              <arguments>
                <argument>src.test.java.nz.google.special.automation.tests</argument>
                <argument>src.main.java.nz.google.special.automation.framework</argument>
                <argument>Manual_Test_Cases.txt</argument>
                <argument>Constants.java</argument>
              </arguments>
              <mainClass>com.pwc.core.framework.ci.ContinuousIntegrationLogExporter</mainClass>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</profile>
```

#### Sample Manual Test Export Output
Depending on your configuration the default **Manual_Test_Cases.txt** file output will resemble the following:

```
  Feature: Smoke Test
  Scenario: Basic Functionality
    Given I am logged in page=home and authenticated user=TestConstants.USER_NAME
    When I view the Landing page without doing a search for env=Data.ENVIRONMENT
    Then Basic components are present in body of message=MY_MESSAGE
    And I can click the BACK button
    But I go back to the Home page
    Or I am blocked by a popup
    If I can click the BACK button
    Not Able to go forward
    Finally I am able to complete the transaction
------------------------------------------------------------

------------------------------------------------------------
MANUAL TEST CASE REPORT SUMMARY
------------------------------------------------------------
1 Tests Processed
Packages Processed:
  package com.pwc.core.framework.ci.tests;
  package com.pwc.core.framework.ci.tests.inner;
```

# Adjustable Sauce Labs Execution Settings
The following settings can be modified at any stage of the automation execution process to run tests on the desired 
system.  This is a necessary feature to enable users to run their tests in Sauce Labs on different browser and operating
system configurations.  

Simply define the following at runtime via **-D** system variables:

| User Defined Command | Options                                                                                      | Default     | Example                      |
|----------------------|----------------------------------------------------------------------------------------------|-------------|------------------------------|
| -Dbrowser            | chrome, headless_ch, firefox, headless_ff, ie, safari, edge, headless_edge                   | Chrome      | -Dbrowser=ff                 |
| -Dversion            | any available                                                                                | latest      | -Dversion=38.0               |
| -Dbrowser.resolution | 800x600, 1024x768, 1152x864, 1280x800, 1280x960, 1280x1024, all mobile resolutions in Chrome | 1024x768    | -Dbrowser.resolution=360x640 |
| -DplatformName       | macOS, osx, linux, windows, windows 10, windows 8, windows 8.1, windows 7, windows xp, xp    | Windows     | -Dplatform=xp                |
| -Dtime.zone          | Los Angeles, Honolulu, New_York                                                              | Los Angeles | -Dtime.zone=Los Angeles      |

**IMPORTANT:**
Defining any of the following variables will result in your test being executed using default Sauce Labs settings:

| User Defined Command           | Conversion                        |
|--------------------------------|-----------------------------------|
| -DplatformName=macOS 11.00     | -Dplatform=OS X 11.0              |
| -DplatformName=osx             | -Dplatform=OS X 10.11             |
| -Dbrowser=ch -DplatformName=xp | -Dbrowser=ch -DplatformName=linux |

### Default Runtime Settings - Sauce Labs
If you choose not to override ANY of the settings above the following runtime settings are used by default in Sauce Labs:

* Linux
* Chrome (latest version) 

## Experitest Execution
To utilize Experitest as your test execution platform the following configurations must be set:
1. **automation.properties** must contain 
```
experitest.accesskey=YOUR_KEY_HERE
```
2. Your Experitest grid url must be defined in **grid.properties**
```
grid.hub.url=https://my_company.experitest.com/wd/hub
```
3. Optional:  [Local tunnel configuration](https://docs.experitest.com/display/LT/Network+Tunnel+Client)  




## Adjustable BrowserStack Execution Settings
The following settings can be modified at any stage of the automation execution process to run tests in the desired
testing environment.  Full list of options can be found in the 
[Capabilities builder](https://www.browserstack.com/automate/capabilities).  

Simply define the following at runtime via **-D** system variables.  Use double quotes around properties that contain spaces.

| User Defined Command    | Options                                                                           | Default        | Mandatory | Example                     |
|-------------------------|-----------------------------------------------------------------------------------|----------------|-----------|-----------------------------|
| -Dos                    | Windows, OS X                                                                     | Windows        | No        | -Dos="OS X"                 |
| -Dos                    | Windows, OS X                                                                     | Windows        | No        | -Dos="OS X"                 |
| -Dos_version            | 10, 8.1, 8, 7, XP, Mojave, Lion, etc...                                           | latest Windows | No        | -Dos_version=xp             |
| -Dbrowser               | Chrome, Firefox, Safari, Opera, Edge                                              | Chrome         | No        | -Dbrowser=Firefox           |
| -Dbrowser_version       | 72.0, 64.0, etc...                                                                | latest version | No        | -Dbrowser_version=71.0      |
| -Dresolution            | 1024x768, 1280x960, 1280x1024, 1600x1200, 1920x1080                               | 1024x768       | No        | -Dresolution=1920x1080      |
| -Dproject               | Specify a name for a logical group of builds                                      | BLANK          | No        | -Dproject="RC Testing"      |
| -Dbuild                 | Specify a name for a logical group of tests                                       | BLANK          | No        | -Dbuild="API Tests"         |
| -Dname                  | Specify an identifier for the test run                                            | BLANK          | No        | -Dname="Smoke Tests"        |
| -Dbrowserstack.local    | Test localhost / internal servers in your network                                 | false          | No        | -Dbrowserstack.local=false  |
| -Dbrowserstack.timezone | Configure tests to run on a custom time zone                                      | UTC            | No        | -Dbrowserstack.timezone=PCT |
| -DcommandTimeout        | Sauce Labs sets command timeout in seconds                                        | 300            | No        | -DcommandTimeout=600        |
| -DidleTimeout           | Sauce Labs sets idle test timeout in seconds. Prevent tests from running too long | 90             | No        | -DidleTimeout=600           |
| -DrecordVideo           | Sauce Labs use this to disable video recording                                    | false          | No        | -DrecordVideo=false         |
| -DrecordScreenshots     | Sauce Labs use this to disable screen shot recording                              | false          | No        | -DrecordScreenshots=false   |

### BrowserStack Jenkins Integration
In order to execute tests from a Jenkins system to the BrowserStack service you normally should create a unique tunnel
to pass all tests through.  This proves to be necessary especially when your AUT is behind a VPN.  

To create a unique tunnel for your tests to execute you simply add the following line of code to your automation 
framework before you initiate the web browser.  This must happen before the WebDriver's capabilities are set by the
*core-microservice*.  Typically, you would this logic to a setup method with the TestNG `@BeforeClass(alwaysRun = true)`
annotation.

```
private static String localTunnelIdentifierInstance;

if (null == localTunnelIdentifierInstance) {
    localTunnelIdentifierInstance = combine("%sMyUniqueTunnel", RandomStringUtils.randomNumeric(5));
}
System.setProperty(FrameworkConstants.BROWSER_STACK_LOCAL_IDENTIFIER_PROPERTY, localTunnelIdentifierInstance);
``` 

## Properties Files Settings
There are three properties files used to drive all automated tests.  The following .properties files are required to be defined in your application's config directory.

### automation.properties
Properties used for connections to the web application and web services under test.

Example:

```
web.url=http://www.google.com
web.services.url=http://www.google.com/api/
web.services.user=
web.services.password=
enable.hard.assert=false
default.wait.for.sleep.millis=3
element.wait.timeout.seconds=55
browser.wait.timeout.seconds=10
enable.ajax.requests.waiting=true
enable.siteMinder=false
siteminder.open.url=
saucelabs.username=my-user
saucelabs.accesskey=12345900-7f51-4185-a3d7-5d2b12342efa
capture.video=false
browserstack.username=
browserstack.accesskey=123456789
browserstack.local=false
```

Settings:

| Property                      | Default Value | Description                               |
|-------------------------------|---------------|-------------------------------------------|
| web.url                       |               | website url to test                       |
| web.services.url              |               | web service url if available              |
| web.services.user             |               | web service credentials                   |
| web.services.password         |               | web service credentials                   |
| enable.hard.assert            | false         | boolean: fail on first failure            |
| default.wait.for.sleep.millis | 1000          | driver wait for element retry (ms)        |
| element.wait.timeout.seconds  | 180           | driver timeout waiting for element (sec)  |
| browser.wait.timeout.seconds  | 10            | browser timeout wait (sec)                |
| enable.ajax.requests.waiting  | true          | boolean: enable active ajax req's waiting |
| enable.siteMinder             | false         | boolean: enable Site Minder auth          |
| siteminder.open.url           |               | Site Minder auth URL                      |
| saucelabs.username            |               | Sauce Labs username                       |
| saucelabs.accesskey           |               | Sauce Labs key                            |
| browserstack.username         |               | BrowserStack username                     |
| browserstack.accesskey        |               | BrowserStack key                          |
| browserstack.local            | false         | Connect to BrowserStack locally?          |
| capture.video                 | false         | record test execution video               |
| jira.zapi.enabled             | false         | Jira Zapi Enabled                         |
| jira.zapi.url                 |               | Jira Zapi url                             |
| jira.zapi.username            |               | Jira Zapi api username                    |
| jira.zapi.password            |               | Jira Zapi api password                    |
| jira.zapi.cycle.name          |               | Jira Zapi cycle name                      |

### database.properties
Properties used for connections to the database under test. 

Example:

```
db.driver=oracle.jdbc.OracleDriver
db.url=jdbc:oracle:thin:@${db.host}:${db.port}/${db.name}
db.host=myhost
db.name=DEV_SERVER
db.port=1234
db.username=me
db.password=password
```

### grid.properties
Properties used for connections to any grid (Sauce Labs, Selenium GRID, ect...) 

Example:

```
grid.enabled=false
grid.hub.url=http://${saucelabs.username}:${saucelabs.accesskey}@ondemand.saucelabs.com:80/wd/hub
```

Settings:

| Property     | Default Value | Description                |
|--------------|---------------|----------------------------|
| grid.enabled | true          | boolean: GRID enabled mode |
| grid.hub.url |               | url to runtime GRID        |


# Runtime
The Core Automation Service supports the following abilities to run your tests.  The **grid.properties** file which 
must be part of your project defines the following self-explanatory properties:

```
grid.enabled=true
grid.hub.url=http://<YOUR PRIVATE GRID IP ADDRESS>:4444/wd/hub
#grid.hub.url=http://<YOUR_SAUCELABS_USER>:<YOUR_SAUCELABS_KEY>@ondemand.saucelabs.com:80/wd/hub

OR

#http://${browserstack.username}:${browserstack.accesskey}@hub-cloud.browserstack.com/wd/hub
```

## IDE
Simply leveraging the TestNG plugin in your IDE of choice you are able to run any test from IntelliJ or Eclipse
 
## Shared GRID
If you have a shared machine with a potentially static IP address follow the same instructions as the previous section
to setup a shared GRID.  Be sure to define the **grid.hub.url** in your grid.properties file.

## Headless Automation Execution
The following headless browsers are supported.  It is recommended that you use the [Video Feature](https://github.com/AnthonyL22/core-microservice#video-playback-feature) for debugging tests.

### Headless Chrome
The Chrome Headless browser is supported with a default browser resolution of 1920,1080.  

Important: the headless browser(s) are executed on the system that is executing Maven or the IDE.  Distributed execution in Selenium GRIDs is also supported for the headless Chrome browser.
 
The following system environment variables must be set to run the Headless Chrome browser in a runtime environment

```
-Dbrowser=headless_ch OR headless_ff OR headless_edge
```

## Sauce Labs
If you have a Sauce Labs account, define the **grid.hub.url** in your grid.properties file according to the settings
defined in the setup instructions provided by Sauce Labs.

Be sure to download and run the [Sauce Connect plugin](https://docs.saucelabs.com/reference/sauce-connect/) in your 
local environment to execute your tests in Sauce Labs.  (see next section for details)

Add the following section to your **settings.xml** in order to connect your local Maven profile to Sauce Labs:
```
<profile>
    <id>saucelabs</id>
    <properties>
        <sauce.username>YOUR_SAUCELABS_USER</sauce.username>
        <sauce.key>YOUR_SAUCELABS_KEY</sauce.key>
    </properties>
</profile>
```

### Sauce Connect Plugin from your Local Test Environment
To execute your tests from your local environment to Sauce Labs you will need to configure via:

1. Follow instructions for [Sauce Connect proxy](https://app.saucelabs.com/tunnels/)
2. Get the latest Sauce Connect Proxy
3. Install to a location on your computer without spaces.  Example: **\dev\tools\sc-4.3.11-win32**
4. Open a command prompt and change directory to the dir in step 3
5. After extracting, go to the install directory and run the command displayed at the bottom of [Sauce Connect proxy](https://app.saucelabs.com/tunnels/)
6. Wait for tunnel to start
7. Verify your grid.properties file for the AUT is set to **grid.enabled=true**
8. Open the automation.properties file for the AUT and set the **saucelabs.username=USER_FROM_STEP_1**
9. Open the automation.properties file for the AUT and set the **saucelabs.accesskey=ACCESS_KEY_FROM_STEP_1**
10. `mvn clean install`

**IMPORTANT**: The automation.properties 'saucelabs.username' value must match the Jenkins Sauce Connect account used in the Jenkins pipeline.

# External Dependencies

## Logging Microservice
Gherkin-Style logger used for automated testing of TestNG-based automation solutions.

[Logging Service](https://github.com/AnthonyL22/logging-microservice)

## Assertion Microservice
A 100% Hamcrest and TestNG-based automated testing Assertion service.

[Assertion Service](https://github.com/AnthonyL22/assert-microservice)


# Tips and Tricks
* You must close your local Sauce Labs tunnel if you are running completely locally and don't want to report Sauce Labs
results for tests running on your local machine (ex: grid.enabled=false)
