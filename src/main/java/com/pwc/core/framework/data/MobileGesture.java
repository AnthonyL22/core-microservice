package com.pwc.core.framework.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MobileGesture {

    SWIPE("swipe"),
    SCROLL("scroll"),
    PINCH("pinch"),
    DOUBLE_TAP("doubleTap"),
    TOUCH_AND_HOLD("touchAndHold"),
    TWO_FINGER_TAP("twoFingerTap"),
    TAP("tap"),
    DRAG_FROM_TO_FOR_DURATION("dragFromToForDuration"),
    SELECT_PICKER_WHEEL_VALUE("selectPickerWheelValue"),
    ALERT("alert");

    public String gesture;

}
