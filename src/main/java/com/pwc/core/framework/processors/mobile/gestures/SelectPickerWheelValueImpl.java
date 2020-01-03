package com.pwc.core.framework.processors.mobile.gestures;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pwc.core.framework.data.MobileGesture;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class SelectPickerWheelValueImpl {

    public static boolean applies(MobileGesture gesture) {
        return (StringUtils.equalsIgnoreCase(gesture.gesture, MobileGesture.SELECT_PICKER_WHEEL_VALUE.gesture));
    }

    public Map buildParameters(final MobileElement element, MobileGesture mobileGesture, SelectPickerWheelValue selectPickerWheelValueParameters) {

        Map<String, Object> convertedParameters = new HashMap();
        try {
            LOG(true, "Perform '%s' GESTURE", mobileGesture.gesture);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            if (null == selectPickerWheelValueParameters) {
                selectPickerWheelValueParameters = SelectPickerWheelValue.builder()  //
                        .element(element.getId())  //
                        .order("next")  //
                        .offset(0.1f)  //
                        .build();
            } else {
                selectPickerWheelValueParameters.setElement(element.getId());
            }
            convertedParameters = mapper.convertValue(selectPickerWheelValueParameters, Map.class);
        } catch (Exception e) {
            assertFail("Failed to perform GESTURE '%s' due to exception=%s", mobileGesture.gesture, e.getMessage());
        }
        return convertedParameters;
    }

}
