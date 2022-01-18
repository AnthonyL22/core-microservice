package com.pwc.core.framework.driver;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

public class MicroserviceEdgeDriver extends EdgeDriver implements MicroserviceWebDriver {

    public MicroserviceEdgeDriver() {
        super();
    }

    public MicroserviceEdgeDriver(EdgeOptions edgeOptions) {
        super(edgeOptions);
    }

    public MicroserviceEdgeDriver(EdgeDriverService service) {
        super(service);
    }

    public MicroserviceEdgeDriver(EdgeDriverService service, EdgeOptions options) {
        super(service, options);
    }

}
