package qcom.itlegal.ipit.framework.driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Capabilities;

public class QualcommAndroidDriver extends AndroidDriver implements QualcommWebDriver {

    public QualcommAndroidDriver(Capabilities capabilities) {
        super(capabilities);
    }

}
