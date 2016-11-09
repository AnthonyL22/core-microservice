package com.pwc.core.framework.driver;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.DeviceRotation;

public class MicroserviceIOSDriver extends IOSDriver implements MicroserviceWebDriver {

    public MicroserviceIOSDriver(Capabilities capabilities) {
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
