package com.pwc.core.framework.driver;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;

public class MicroserviceInternetExplorerDriver extends InternetExplorerDriver implements MicroserviceWebDriver {

    public MicroserviceInternetExplorerDriver() {
        super();
    }

    public MicroserviceInternetExplorerDriver(InternetExplorerOptions internetExplorerOptions) {
        super(internetExplorerOptions);
    }

    public MicroserviceInternetExplorerDriver(InternetExplorerDriverService service) {
        super(service);
    }

    public MicroserviceInternetExplorerDriver(InternetExplorerDriverService service, InternetExplorerOptions internetExplorerOptions) {
        super(service, internetExplorerOptions);
    }

}
