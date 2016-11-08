package qcom.itlegal.ipit.framework.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;


public class QualcommRemoteWebDriver extends RemoteWebDriver implements QualcommWebDriver {
    protected QualcommRemoteWebDriver() {
        super();
    }

    public QualcommRemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
    }

    public QualcommRemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
        super(executor, desiredCapabilities);
    }

    public QualcommRemoteWebDriver(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }

    public QualcommRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(remoteAddress, desiredCapabilities, requiredCapabilities);
    }

    public QualcommRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }
}
