package com.pwc.core.framework.driver;

/**
 * Common driver to talk to encapsulate all the different Appium Drivers we can use (iOS, Android, etc...)
 */
public interface MicroserviceMobileDriver extends org.openqa.selenium.internal.FindsByXPath, io.appium.java_client.InteractsWithApps, io.appium.java_client.InteractsWithFiles,
                io.appium.java_client.FindsByAccessibilityId, io.appium.java_client.FindsByAndroidUIAutomator, io.appium.java_client.FindsByIosClassChain,
                io.appium.java_client.FindsByWindowsAutomation, io.appium.java_client.FindsByIosNSPredicate {
}
