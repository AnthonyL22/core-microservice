package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

public class QualcommEdgeDriver extends EdgeDriver implements QualcommWebDriver {

    public QualcommEdgeDriver() {
        super();
    }

    public QualcommEdgeDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public QualcommEdgeDriver(EdgeOptions edgeOptions) {
        super(edgeOptions);
    }

    public QualcommEdgeDriver(EdgeDriverService service) {
        super(service);
    }

    public QualcommEdgeDriver(EdgeDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

}
