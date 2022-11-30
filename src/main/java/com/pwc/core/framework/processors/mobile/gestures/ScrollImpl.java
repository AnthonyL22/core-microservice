package com.pwc.core.framework.processors.mobile.gestures;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pwc.core.framework.data.MobileGesture;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

import static com.pwc.assertion.AssertService.assertFail;
import static com.pwc.logging.service.LoggerService.LOG;

public class ScrollImpl {

    public static boolean applies(MobileGesture gesture) {
        return (StringUtils.equalsIgnoreCase(gesture.gesture, MobileGesture.SCROLL.gesture));
    }

    public Map buildParameters(final WebElement element, MobileGesture mobileGesture, Scroll scrollParameters) {

        Map<String, Object> convertedParameters = new HashMap();
        try {
            LOG(true, "Perform '%s' GESTURE to '%s' element", mobileGesture.gesture, element.getText());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            if (null == scrollParameters) {

                TouchAction().press(el0).moveTo(el1).release()




                scrollParameters = Scroll.builder() //
                                .element(element.getId()) //
                                .direction("down") //
                                .build();
            } else {
                scrollParameters.setElement(element.getId());
            }
            convertedParameters = mapper.convertValue(scrollParameters, Map.class);
        } catch (Exception e) {
            assertFail("Failed to perform GESTURE '%s' due to exception=%s", mobileGesture.gesture, e.getMessage());
        }
        return convertedParameters;
    }

}
