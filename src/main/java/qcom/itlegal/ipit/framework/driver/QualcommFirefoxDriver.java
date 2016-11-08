package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class QualcommFirefoxDriver extends FirefoxDriver implements QualcommWebDriver {

    public QualcommFirefoxDriver() {
        super();
    }

    public QualcommFirefoxDriver(FirefoxProfile profile) {
        super(profile);
    }

    public QualcommFirefoxDriver(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }

    public QualcommFirefoxDriver(Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(desiredCapabilities, requiredCapabilities);
    }

    public QualcommFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile) {
        super(binary, profile);
    }

    public QualcommFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile, Capabilities capabilities) {
        super(binary, profile, capabilities);
    }

    public QualcommFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(binary, profile, desiredCapabilities, requiredCapabilities);
    }
}
