package qcom.itlegal.ipit.framework.driver;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;

public class QualcommIOSDriver extends IOSDriver implements QualcommWebDriver {

    public QualcommIOSDriver(Capabilities capabilities) {
        super(capabilities);
    }

}
