package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class MicroserviceChromeDriver extends ChromeDriver implements MicroserviceWebDriver {

    public MicroserviceChromeDriver() {
        super();
    }

    public MicroserviceChromeDriver(ChromeDriverService service) {
        super(service);
    }

    public MicroserviceChromeDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public MicroserviceChromeDriver(ChromeOptions options) {
        super(options);
    }

    public MicroserviceChromeDriver(ChromeDriverService service, ChromeOptions options) {
        super(service, options);
    }

    public MicroserviceChromeDriver(ChromeDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }
}
