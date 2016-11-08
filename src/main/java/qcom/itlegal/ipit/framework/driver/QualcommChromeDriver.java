package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class QualcommChromeDriver extends ChromeDriver implements QualcommWebDriver {

    public QualcommChromeDriver() {
        super();
    }

    public QualcommChromeDriver(ChromeDriverService service) {
        super(service);
    }

    public QualcommChromeDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public QualcommChromeDriver(ChromeOptions options) {
        super(options);
    }

    public QualcommChromeDriver(ChromeDriverService service, ChromeOptions options) {
        super(service, options);
    }

    public QualcommChromeDriver(ChromeDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }
}
