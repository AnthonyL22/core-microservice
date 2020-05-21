package com.pwc.core.framework.processors.mobile;

import com.pwc.core.framework.data.MobileGesture;
import com.pwc.core.framework.processors.mobile.gestures.Alert;
import com.pwc.core.framework.processors.mobile.gestures.AlertImpl;
import com.pwc.core.framework.processors.mobile.gestures.DoubleTap;
import com.pwc.core.framework.processors.mobile.gestures.DoubleTapImpl;
import com.pwc.core.framework.processors.mobile.gestures.DragFromToForDuration;
import com.pwc.core.framework.processors.mobile.gestures.DragFromToForDurationImpl;
import com.pwc.core.framework.processors.mobile.gestures.Pinch;
import com.pwc.core.framework.processors.mobile.gestures.PinchImpl;
import com.pwc.core.framework.processors.mobile.gestures.Scroll;
import com.pwc.core.framework.processors.mobile.gestures.ScrollImpl;
import com.pwc.core.framework.processors.mobile.gestures.SelectPickerWheelValue;
import com.pwc.core.framework.processors.mobile.gestures.SelectPickerWheelValueImpl;
import com.pwc.core.framework.processors.mobile.gestures.Swipe;
import com.pwc.core.framework.processors.mobile.gestures.SwipeImpl;
import com.pwc.core.framework.processors.mobile.gestures.Tap;
import com.pwc.core.framework.processors.mobile.gestures.TapImpl;
import com.pwc.core.framework.processors.mobile.gestures.TouchAndHold;
import com.pwc.core.framework.processors.mobile.gestures.TouchAndHoldImpl;
import com.pwc.core.framework.processors.mobile.gestures.TwoFingerTap;
import com.pwc.core.framework.processors.mobile.gestures.TwoFingerTapImpl;
import io.appium.java_client.MobileElement;

import java.util.HashMap;
import java.util.Map;

public class GestureActivityProcessor {

    private static GestureActivityProcessor instance = new GestureActivityProcessor();

    private GestureActivityProcessor() {
    }

    public static GestureActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(MobileGesture gesture) {
        return TapImpl.applies(gesture) || ScrollImpl.applies(gesture) || AlertImpl.applies(gesture) || DoubleTapImpl.applies(gesture) || DragFromToForDurationImpl.applies(gesture)
                        || PinchImpl.applies(gesture) || SelectPickerWheelValueImpl.applies(gesture) || SwipeImpl.applies(gesture) || TouchAndHoldImpl.applies(gesture)
                        || TwoFingerTapImpl.applies(gesture);
    }

    public Map buildParameters(MobileElement mobileElement, MobileGesture gesture, Object customParameters) {

        Map params = new HashMap();
        if (TapImpl.applies(gesture)) {
            TapImpl element = new TapImpl();
            params = element.buildParameters(mobileElement, gesture, (Tap) customParameters);
        } else if (ScrollImpl.applies(gesture)) {
            ScrollImpl element = new ScrollImpl();
            params = element.buildParameters(mobileElement, gesture, (Scroll) customParameters);
        } else if (AlertImpl.applies(gesture)) {
            AlertImpl element = new AlertImpl();
            params = element.buildParameters(mobileElement, gesture, (Alert) customParameters);
        } else if (DoubleTapImpl.applies(gesture)) {
            DoubleTapImpl element = new DoubleTapImpl();
            params = element.buildParameters(mobileElement, gesture, (DoubleTap) customParameters);
        } else if (DragFromToForDurationImpl.applies(gesture)) {
            DragFromToForDurationImpl element = new DragFromToForDurationImpl();
            params = element.buildParameters(mobileElement, gesture, (DragFromToForDuration) customParameters);
        } else if (PinchImpl.applies(gesture)) {
            PinchImpl element = new PinchImpl();
            params = element.buildParameters(mobileElement, gesture, (Pinch) customParameters);
        } else if (SelectPickerWheelValueImpl.applies(gesture)) {
            SelectPickerWheelValueImpl element = new SelectPickerWheelValueImpl();
            params = element.buildParameters(mobileElement, gesture, (SelectPickerWheelValue) customParameters);
        } else if (SwipeImpl.applies(gesture)) {
            SwipeImpl element = new SwipeImpl();
            params = element.buildParameters(mobileElement, gesture, (Swipe) customParameters);
        } else if (TouchAndHoldImpl.applies(gesture)) {
            TouchAndHoldImpl element = new TouchAndHoldImpl();
            params = element.buildParameters(mobileElement, gesture, (TouchAndHold) customParameters);
        } else if (TwoFingerTapImpl.applies(gesture)) {
            TwoFingerTapImpl element = new TwoFingerTapImpl();
            params = element.buildParameters(mobileElement, gesture, (TwoFingerTap) customParameters);
        }
        return params;
    }

}
