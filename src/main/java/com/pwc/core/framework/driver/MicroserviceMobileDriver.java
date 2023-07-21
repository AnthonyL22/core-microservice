package com.pwc.core.framework.driver;

/**
 * Common driver to talk to encapsulate all the different Appium Drivers we can use (iOS, Android, etc...)
 */
public interface MicroserviceMobileDriver extends org.openqa.selenium.WebDriver, org.openqa.selenium.JavascriptExecutor, org.openqa.selenium.HasCapabilities {
}
