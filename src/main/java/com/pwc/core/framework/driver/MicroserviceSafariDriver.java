package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class MicroserviceSafariDriver extends SafariDriver implements MicroserviceWebDriver {

    public MicroserviceSafariDriver() {
        super();
    }

    public MicroserviceSafariDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public MicroserviceSafariDriver(SafariOptions options) {
        super(options);
    }

}
