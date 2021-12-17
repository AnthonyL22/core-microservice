package com.pwc.core.framework.driver;

/**
 * Common driver to talk to encapsulate all the different Selenium Drivers we can use (FF, IE, PhantomJS, etc...)
 */
public interface MicroserviceWebDriver extends org.openqa.selenium.WebDriver, org.openqa.selenium.JavascriptExecutor, org.openqa.selenium.HasCapabilities {
}
