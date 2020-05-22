package com.pwc.core.framework.driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.DeviceRotation;

public class MicroserviceAndroidMobileDriver extends AndroidDriver implements MicroserviceMobileDriver {

    public MicroserviceAndroidMobileDriver(Capabilities capabilities) {
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
