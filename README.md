## Introduction

The Core Automated Testing microservice is used to the perform functional system tests on any desktop or mobile browser, REST service and database endpoints.  

This service is an on-ramp for entire engineering teams to contribute to the functional testing of a product offering.  Also, to unify our testing teams with one automated testing solution.

Currently capabilities include:

* Browser (Internet Explorer, Firefox, Chrome, Safari, PhantomJS)
* Mobile Browser (Android, iOS using Appium)
* REST Web Services (SiteMinder Authenticated) 
* Database (Oracle, MongoDB, MS SQL, JDBC)

## Prerequisites

1. Java 1.8
2. Maven 3.x

## Maven Dependency

```
<dependency>
    <groupId>com.pwc.core</groupId>
    <artifactId>core-microservice</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

## Integration

Simply, extend the class WebTestCase defined in this service.  For example:

```
public abstract class MyTestCase extends WebTestCase {

    /**
     * Product specific functionality should go in here
     */

}
```

## Adjustable Settings
The following settings can be modified at any stage of the automation execution process to run tests on the desired 
system.  This is a necessary feature to enable users to run their tests in Sauce Labs on different browser and operating
system configurations.  

Simply define the following at runtime via **-D** system variables:

| User Defined Command  | Options                                                                                 | Default     | Example                      |
| ----------------------|-----------------------------------------------------------------------------------------|-------------|------------------------------|
| -Dbrowser             | ch, ff, ie, safari                                                                      | Chrome      | -Dbrowser=ff                 |
| -Dbrowser.version     | any available                                                                           | 44          | -Dbrowser.version=38.0       |
| -Dbrowser.resolution  | 800x600, 1024x768, 1152x864, 1280x800, 1280x960, 1280x1024, others                      | 1024x768    | -Dbrowser.resolution=800x600 |
| -Dplatform            | mac, osx, linux, windows, windows 10, windows 8, windows 8.1, windows 7, windows xp, xp | linux       | -Dplatform=xp                |
| -Dtime.zone           | Los Angeles, Honolulu, New_York                                                         | Los Angeles | -Dtime.zone=Los Angeles      |


**IMPORTANT:**
Defining any of the following variables will result in your test being executed using default Sauce Labs settings:

| User Defined Command        | Conversion                    |
| ----------------------------|-------------------------------|
| -Dplatform=mac              | -Dplatform=OS X 10.11         |
| -Dplatform=osx              | -Dplatform=OS X 10.11         |
| -Dbrowser=ch -Dplatform=xp  | -Dbrowser=ch -Dplatform=linux |


### Default Runtime Settings - Sauce Labs
If you choose not to override ANY of the settings above the following runtime settings are used by default in Sauce Labs:

* Linux
* Chrome (latest version) 

### Default Runtime Settings - non Sauce Labs
If you choose not to override ANY of the settings above the following runtime settings are used by default in Sauce Labs:

* Your Operating System
* Chrome (your installed version) 


### Properties Files Settings
There are three properties files used to drive all automated tests.  The following .properties files are required to be defined in your application's config directory.

#### automation.properties
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
enable.siteMinder=false
siteminder.open.url=
saucelabs.username=my-user
saucelabs.accesskey=12345900-7f51-4185-a3d7-5d2b12342efa
```

Settings:

| Property                      | Default Value    | Description                                |
| ------------------------------|------------------|--------------------------------------------|
| web.url                       |                  | website url to test                        |
| web.services.url              |                  | web service url if available               |
| web.services.user             |                  | web service credentials                    |
| web.services.password         |                  | web service credentials                    |
| enable.hard.assert            | false            | boolean: fail on first failure             |
| default.wait.for.sleep.millis | 1000             | driver wait for element retry (ms)         |
| element.wait.timeout.seconds  | 180              | driver timeout waiting for element (sec)   |
| browser.wait.timeout.seconds  | 10               | browser timeout wait (sec)                 |
| enable.siteMinder             | false            | boolean: enable Site Minder auth           |
| siteminder.open.url           |                  | Site Minder auth URL                       |
| saucelabs.username            |                  | Sauce Labs username (see grid.properties)  |
| saucelabs.accesskey           |                  | Sauce Labs key (see grid.properties)       |

#### database.properties
Properties used for connections to the database under test. 

Example:

```
db.driver=oracle.jdbc.OracleDriver
db.url=jdbc:oracle:thin:@${db.host}:${db.port}/${db.name}
db.host=myhost
db.name=DEV_SRV
db.port=1522
db.username=me
db.password=password
mongo.host=
mongo.databasename=
mongo.port=1
mongo.username=
mongo.password=
```

#### grid.properties
Properties used for connections to any grid (Sauce Labs, Selenium GRID, ect...) 

Example:

```
grid.enabled=false
grid.hub.url=http://${saucelabs.username}:${saucelabs.accesskey}@ondemand.saucelabs.com:80/wd/hub
```

Settings:

| Property          | Default Value    | Description                                |
| ------------------|------------------|--------------------------------------------|
| grid.enabled      | true             | boolean: GRID enabled mode                 |
| grid.hub.url      |                  | url to runtime GRID                        |


## Runtime
The Core Automation Service supports the following abilities to run your tests.  The **grid.properties** file which 
must be part of your project defines the following self-explanatory properties:

```
grid.enabled=true
grid.hub.url=http://<YOUR PRIVATE GRID IP ADDRESS>:4444/wd/hub
#grid.hub.url=http://<YOUR_SAUCELABS_USER>:<YOUR_SAUCELABS_KEY>@ondemand.saucelabs.com:80/wd/hub
```

### IDE
Simply leveraging the TestNG plugin in your IDE of choice you are able to run any test from IntelliJ or Eclipse

### Local GRID
Including the [Runtime Microservice Components](https://github.com/AnthonyL22/runtime-microservice) in your 
Maven project will give you all the necessary Selenium GRID components needed to build a local GRID environment.  
Once you have included the Maven dependency in your project and have done a **mvn clean install** you will see a 
directory labeled 'grid' in your project **.../target/test-classes**.  Be sure to define the **grid.hub.url** 
in your grid.properties file.

See the [Selenium GRID instructions](https://github.com/AnthonyL22/runtime-microservice) for more details.
 
### Shared GRID
If you have a shared machine with a potentially static IP address follow the same instructions as the previous section
to setup a shared GRID.  Be sure to define the **grid.hub.url** in your grid.properties file.

#### PhantomJS
If you would like to use a headless PhantomJS browser you must use Selenium GRID with the PhantomJS drivers enabled.

The following system environment variables must be set to run PhantomJS browsers in your GRID environment
```
-Dbrowser=phantomjs
-Dphantomjs.binary.path=c:/grid/drivers/phantomjs/phantomjs.exe
```

The *phantomjs.binary.path* path correlates to the exact location of your PhantomJS drivers on your NODES not your local
development environment.


### Sauce Labs
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

#### Sauce Connect Plugin Step-By-Step
To execute your tests from your local environment to Sauce Labs you will need to configure the 
[Sauce Connect plugin](https://docs.saucelabs.com/reference/sauce-connect/).  Follow the steps below to configure on a PC.

1. Download [Sauce Connect plugin](https://docs.saucelabs.com/reference/sauce-connect/)
2. Install to a location on your PC without spaces.  Example: **C:\dev\tools\sc-4.3.11-win32**
3. Create empty batch file in this directory
4. Add the command below to your batch file.
5. Add a unique tunnel identifier in place of **YOUR_TUNNEL_NAME**.  Any name will suffice without spaces.
6. Edit the path of your executable to match your environment. Example: **cd C:\dev\tools\sc-4.3.11-win32**
7. Add your Sauce labs user in place of **YOUR_USERNAME**
8. Add your Sauce labs access key in place of **YOUR_ACCESS_KEY**

YOU MUST BE ON THE YOUR COMPANIES VPN FOR THIS TO WORK

##### Starting Sauce Connect - PC

Create a batch file with the following

```
set tunnelId=YOUR_TUNNEL_NAME
setx -m TUNNEL_IDENTIFIER %tunnelId%

cd C:\dev\tools\sc-4.3.11-win32\bin

sc -u YOUR_USERNAME -k YOUR_ACCESS_KEY -i %tunnelId%
```

##### Starting Sauce Connect - LINUX

1. Open/edit .tcshrc
2. Add an environment variable
``` 
setenv TUNNEL_IDENTIFIER YOUR_TUNNEL_NAME 
```
3. Save changes
4. Run command - source .tcshrc
5. Create a shell script or run command 
```
sc -u YOUR_USERNAME -k YOUR_ACCESS_KEY -i $TUNNEL_IDENTIFIER
```
 
## External Dependencies

###Runtime Microservice
This is an optional dependency that you could include in your project if using the Core Automation Microservice.  

[Runtime Binary Components](https://github.com/AnthonyL22/runtime-microservice)

###Logging Microservice
Gherkin-Style logger used for automated testing of TestNG-based automation solutions.

[Logging Service](https://github.com/AnthonyL22/logging-microservice)

###Assertion Microservice
A 100% Hamcrest and TestNG-based automated testing Assertion service.

[Assertion Service](https://github.com/AnthonyL22/assert-microservice)


## Tips and Tricks
* You must close your local Sauce Labs tunnel if you are running completely locally and don't want to report Sauce Labs
results for tests running on your local machine (ex: grid.enabled=false)
