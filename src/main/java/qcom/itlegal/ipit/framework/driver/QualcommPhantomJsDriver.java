package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

public class QualcommPhantomJsDriver extends PhantomJSDriver implements QualcommWebDriver {

    public QualcommPhantomJsDriver() {
        super();
    }

    public QualcommPhantomJsDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public QualcommPhantomJsDriver(PhantomJSDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

}
