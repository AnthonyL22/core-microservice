# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

The Core Automation Microservice is a Java-based test automation framework for web, mobile, and API testing. It's built on top of Selenium WebDriver, Appium, TestNG, and Spring, providing a comprehensive testing solution with support for multiple browsers, mobile platforms, and cloud testing services (Sauce Labs, BrowserStack, Experitest).

## Build Commands

This project uses Maven with Java 21. Key commands:

```bash
# Build the project
mvn clean install

# Build without tests
mvn clean install -DskipTests=true

# Run tests
mvn test

# Code formatting (using formatter-maven-plugin with PWC style)
mvn formatter:format

# Code validation (checkstyle) - Note: May fail with Java 21 switch expressions
mvn checkstyle:check

# Sort POM dependencies
mvn sortpom:sort

# Run tests with specific browser
mvn test -Dbrowser=chrome
mvn test -Dbrowser=headless_ch
mvn test -Dbrowser=firefox
mvn test -Dbrowser=safari
mvn test -Dbrowser=edge

# Run tests with specific platform
mvn test -DplatformName=windows
mvn test -DplatformName="OS X"

# Generate manual test case report
mvn clean install -PoutputManualTestCaseReport

# Deploy snapshots (requires proper permissions)
mvn clean install deploy -DskipTests=true
```

**Build Issues Fixed:**
- Updated Selenium version from 4.35.0 to 4.24.0 with compatible selenium-devtools-v127
- Added explicit HTTPS repository configuration to prevent HTTP 501 errors
- Updated Maven checkstyle plugin from 2.17 to 3.5.0 with checkstyle core 10.18.2 for Java 21 compatibility
- Updated checkstyle configuration parameters for newer plugin version (`sourceDirectories` vs `sourceDirectory`)
- Removed deprecated checkstyle properties (`scope`, `maxLineLength`, `allowMissingJavadoc`, etc.)

**Deployment Configuration:**
- Configured for Central Portal publishing with credentials in `~/.m2/settings.xml`
- Server ID: `central` with username `QQ2WRU` and token-based authentication
- SNAPSHOT repository: `https://central.sonatype.com/repository/maven-snapshots/`
- **Note**: Currently getting 403 Forbidden on deployment - may require repository permissions review or alternative deployment approach

**GPG Signing Configuration:**
- GPG signing enabled for Maven releases (`<skip.sign>false</skip.sign>`)
- Uses GPG key `9BF28DC852F97E38` (Anthony Lombardo Maven) with no passphrase for automated builds
- GPG plugin configured with `--pinentry-mode loopback --batch --yes` arguments
- All Maven artifacts (JAR, sources, javadoc, POM) are now properly signed for Central Portal publishing

## Project Architecture

### Core Test Base Classes
- **`WebTestCase`**: Selenium-style methods (e.g., `webElementExists()`)  
- **`DeclarativeTestCase`**: Direct action methods (e.g., `exists()`)
- **`MobileTestCase`**: Mobile-specific testing capabilities
- **`MicroserviceTestSuite`**: Base suite class that all test cases extend

### Key Framework Components

#### Driver Management
- Multiple WebDriver implementations in `src/main/java/com/pwc/core/framework/driver/`
- Support for Chrome, Firefox, Edge, Safari, IE, and headless variants
- Mobile drivers for Android and iOS via Appium
- Remote driver support for cloud services

#### Test Configuration
Configuration files in `src/test/resources/config/dev-env/`:
- **`automation.properties`**: Web URLs, timeouts, browser settings, cloud credentials
- **`grid.properties`**: Selenium Grid and cloud service configuration
- **`database.properties`**: Database connection settings
- **`mobile.properties`**: Mobile testing configuration

#### Utility Classes
Comprehensive utilities in `src/main/java/com/pwc/core/framework/util/`:
- `DateUtils`, `StringUtils`, `FileUtils`
- `ExcelUtils`, `CollectionUtils`, `TableUtils`
- `PropertiesUtils`, `ObjectUtils`, `RandomStringUtils`
- `WebElementUtils`, `GridUtils`

#### Annotations
Custom TestNG annotations in `src/main/java/com/pwc/core/framework/annotations/`:
- **`@TestCase("JIRA-123")`**: Links test to Jira/Zephyr test case IDs
- **`@Issue("STORY-456")`**: Associates test with story/issue numbers
- **`@MaxRetryCount(3)`**: Sets retry count for flaky tests

### Cloud Testing Support

#### Sauce Labs
- Configure via `automation.properties` with `saucelabs.username` and `saucelabs.accesskey`
- Set `grid.enabled=true` and appropriate `grid.hub.url`
- Supports Sauce Connect for local tunnel

#### BrowserStack  
- Configure via `automation.properties` with `browserstack.username` and `browserstack.accesskey`
- Runtime parameters: `-Dos`, `-Dos_version`, `-Dbrowser_version`, `-Dresolution`
- Local testing support with `-Dbrowserstack.local=true`

#### Experitest
- Configure `experitest.accesskey` in `automation.properties`
- Set grid URL in `grid.properties`

### Performance and Monitoring Features
- Built-in response time measurement for web actions
- `durationForElementToAppear()` and `durationForElementToDisappear()` timing utilities
- Browser console and network diagnostics (`webDiagnosticsConsole...()`, `webDiagnosticsRequest...()`)
- Video recording capability (`capture.video=true` in automation.properties)

### Web Service Testing
- REST API testing with multiple authentication methods (OAuth2, Basic, Site Minder, Custom Headers)
- JSON and XML response validation
- Built-in response time tracking

### Code Quality
- **Formatter**: Uses `eclipse-java-pwc-style.xml` with 200 character line length
- **Checkstyle**: Configured via `checkstyle.xml` with custom rules
- **Exclusions**: Framework code in `/data/`, `/driver/`, `/command/` directories excluded from some checks

## Common Development Patterns

### Test Structure
Tests typically extend either `WebTestCase` or `DeclarativeTestCase`:
```java
public class MyTest extends WebTestCase {
    @TestCase("JIRA-123")
    @Test(retryAnalyzer = Retry.class, groups = {Groups.SMOKE_TEST})
    public void testLogin() {
        // Test implementation
    }
}
```

### Element Location
Use XPath or CSS selectors for element identification:
```java
webElementClick("//button[@id='login-btn']");
webElementExists("input[name='username']");
```

### Property Access
Access configuration via `PropertiesUtils`:
```java
String webUrl = PropertiesUtils.getPropertyValue("web.url");
```

### Mobile Testing
Extend `MobileTestCase` for mobile-specific functionality:
```java
public class MobileTest extends MobileTestCase {
    @Test
    public void testMobileGesture() {
        executeJavascript(MobileGesture.SWIPE, parameters);
    }
}
```