package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;


public class MicroserviceRemoteWebDriver extends RemoteWebDriver implements MicroserviceWebDriver {
    protected MicroserviceRemoteWebDriver() {
        super();
    }

    public MicroserviceRemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
    }

    public MicroserviceRemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
        super(executor, desiredCapabilities);
    }

    public MicroserviceRemoteWebDriver(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }

    public MicroserviceRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(remoteAddress, desiredCapabilities, requiredCapabilities);
    }

    public MicroserviceRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }
}
