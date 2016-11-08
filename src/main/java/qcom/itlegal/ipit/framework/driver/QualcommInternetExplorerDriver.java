package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public class QualcommInternetExplorerDriver extends InternetExplorerDriver implements QualcommWebDriver {

    public QualcommInternetExplorerDriver() {
        super();
    }

    public QualcommInternetExplorerDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public QualcommInternetExplorerDriver(int port) {
        super(port);
    }

    public QualcommInternetExplorerDriver(InternetExplorerDriverService service) {
        super(service);
    }

    public QualcommInternetExplorerDriver(InternetExplorerDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

}
