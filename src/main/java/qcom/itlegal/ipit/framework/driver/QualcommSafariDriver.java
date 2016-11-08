package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class QualcommSafariDriver extends SafariDriver implements QualcommWebDriver {

    public QualcommSafariDriver() {
        super();
    }

    public QualcommSafariDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public QualcommSafariDriver(SafariOptions options) {
        super(options);
    }

}
