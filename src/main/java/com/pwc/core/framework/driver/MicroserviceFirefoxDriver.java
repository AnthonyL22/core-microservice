package com.pwc.core.framework.driver;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class MicroserviceFirefoxDriver extends FirefoxDriver implements MicroserviceWebDriver {

    public MicroserviceFirefoxDriver() {
        super();
    }

    public MicroserviceFirefoxDriver(FirefoxOptions firefoxOptions) {
        super(firefoxOptions);
    }

}
