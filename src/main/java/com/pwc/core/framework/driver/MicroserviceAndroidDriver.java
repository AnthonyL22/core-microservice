package com.pwc.core.framework.driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.DeviceRotation;

public class MicroserviceAndroidDriver extends AndroidDriver implements MicroserviceWebDriver {

    public MicroserviceAndroidDriver(Capabilities capabilities) {
        super(capabilities);
    }

    @Override
    public void rotate(DeviceRotation deviceRotation) {

    }

    @Override
    public DeviceRotation rotation() {
        return null;
    }
}
