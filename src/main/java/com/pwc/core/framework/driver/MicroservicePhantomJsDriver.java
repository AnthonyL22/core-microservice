package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

public class MicroservicePhantomJsDriver extends PhantomJSDriver implements MicroserviceWebDriver {

    public MicroservicePhantomJsDriver() {
        super();
    }

    public MicroservicePhantomJsDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public MicroservicePhantomJsDriver(PhantomJSDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

}
