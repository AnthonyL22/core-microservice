package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.List;


public class MicroserviceRemoteMobileDriver extends RemoteWebDriver implements MicroserviceMobileDriver {

    protected MicroserviceRemoteMobileDriver() {
        super();
    }

    public MicroserviceRemoteMobileDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
        super(executor, desiredCapabilities);
    }

    public MicroserviceRemoteMobileDriver(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }

    public MicroserviceRemoteMobileDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    public WebElement findElement(String by, String using) {
        return null;
    }

    @Override
    public List findElements(String by, String using) {
        return null;
    }

    @Override
    public WebElement findElementByAccessibilityId(String using) {
        return null;
    }

    @Override
    public List findElementsByAccessibilityId(String using) {
        return null;
    }

    @Override
    public WebElement findElementByAndroidUIAutomator(String using) {
        return null;
    }

    @Override
    public List findElementsByAndroidUIAutomator(String using) {
        return null;
    }

    @Override
    public WebElement findElementByIosClassChain(String using) {
        return null;
    }

    @Override
    public List findElementsByIosClassChain(String using) {
        return null;
    }

    @Override
    public WebElement findElementByIosNsPredicate(String using) {
        return null;
    }

    @Override
    public List findElementsByIosNsPredicate(String using) {
        return null;
    }

    @Override
    public WebElement findElementByWindowsUIAutomation(String selector) {
        return null;
    }

    @Override
    public List findElementsByWindowsUIAutomation(String selector) {
        return null;
    }
}
