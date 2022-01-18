package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public class MicroserviceInternetExplorerDriver extends InternetExplorerDriver implements MicroserviceWebDriver {

    public MicroserviceInternetExplorerDriver() {
        super();
    }

    public MicroserviceInternetExplorerDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public MicroserviceInternetExplorerDriver(InternetExplorerDriverService service) {
        super(service);
    }

    public MicroserviceInternetExplorerDriver(InternetExplorerDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

}
