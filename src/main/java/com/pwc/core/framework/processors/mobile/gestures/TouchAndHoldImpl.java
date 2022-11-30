package com.pwc.core.framework.processors.mobile.gestures;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pwc.core.framework.data.MobileGesture;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class TouchAndHoldImpl {

    public static boolean applies(MobileGesture gesture) {
        return (StringUtils.equalsIgnoreCase(gesture.gesture, MobileGesture.TOUCH_AND_HOLD.gesture));
    }

    public Map buildParameters(final WebElement element, MobileGesture mobileGesture, TouchAndHold touchAndHoldParameters) {

        Map<String, Object> convertedParameters = new HashMap();
        try {
            LOG(true, "Perform '%s' GESTURE to '%s' element", mobileGesture.gesture, element.getText());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            if (null == touchAndHoldParameters) {
                touchAndHoldParameters = TouchAndHold.builder() //
                                .element(element.getId()) //
                                .duration(3.0f) //
                                .x(1.0f) //
                                .y(1.0f) //
                                .build();
            } else {
                touchAndHoldParameters.setElement(element.getId());
            }
            convertedParameters = mapper.convertValue(touchAndHoldParameters, Map.class);
        } catch (Exception e) {
            assertFail("Failed to perform GESTURE '%s' due to exception=%s", mobileGesture.gesture, e.getMessage());
        }
        return convertedParameters;
    }

}
