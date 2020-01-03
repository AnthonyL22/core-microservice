package com.pwc.core.framework.processors.mobile.gestures;

import com.pwc.core.framework.data.MobileGesture;
import io.appium.java_client.MobileElement;

import java.util.Map;

public interface MicroserviceMobileElementGesture {

    public Map buildParameters(final MobileElement webElement, final MobileGesture gesture, Object parameters);

}
