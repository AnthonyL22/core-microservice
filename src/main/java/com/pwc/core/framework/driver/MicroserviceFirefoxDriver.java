package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class MicroserviceFirefoxDriver extends FirefoxDriver implements MicroserviceWebDriver {

    public MicroserviceFirefoxDriver() {
        super();
    }

    public MicroserviceFirefoxDriver(FirefoxProfile profile) {
        super(profile);
    }

    public MicroserviceFirefoxDriver(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }

    public MicroserviceFirefoxDriver(Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(desiredCapabilities, requiredCapabilities);
    }

    public MicroserviceFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile) {
        super(binary, profile);
    }

    public MicroserviceFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile, Capabilities capabilities) {
        super(binary, profile, capabilities);
    }

    public MicroserviceFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(binary, profile, desiredCapabilities, requiredCapabilities);
    }
}
