package com.pwc.core.framework.driver;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.DeviceRotation;

public class MicroserviceIOSMobileDriver extends IOSDriver implements MicroserviceMobileDriver {

    public MicroserviceIOSMobileDriver(Capabilities capabilities) {
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
