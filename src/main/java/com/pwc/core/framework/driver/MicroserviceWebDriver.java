package com.pwc.core.framework.driver;

/**
 * Common driver to talk to encapsulate all the different Selenium Drivers we can use (FF, IE, PhantomJS, etc...)
 */
public interface MicroserviceWebDriver extends org.openqa.selenium.WebDriver, org.openqa.selenium.JavascriptExecutor, org.openqa.selenium.internal.FindsById,
                org.openqa.selenium.internal.FindsByLinkText, org.openqa.selenium.internal.FindsByXPath, org.openqa.selenium.internal.FindsByName, org.openqa.selenium.internal.FindsByCssSelector,
                org.openqa.selenium.internal.FindsByTagName, org.openqa.selenium.internal.FindsByClassName, org.openqa.selenium.HasCapabilities {
}
