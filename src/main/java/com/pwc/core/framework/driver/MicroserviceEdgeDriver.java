package com.pwc.core.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

public class MicroserviceEdgeDriver extends EdgeDriver implements MicroserviceWebDriver {

    public MicroserviceEdgeDriver() {
        super();
    }

    public MicroserviceEdgeDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public MicroserviceEdgeDriver(EdgeOptions edgeOptions) {
        super(edgeOptions);
    }

    public MicroserviceEdgeDriver(EdgeDriverService service) {
        super(service);
    }

    public MicroserviceEdgeDriver(EdgeDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

}
