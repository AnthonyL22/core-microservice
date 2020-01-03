package com.pwc.core.framework.processors.mobile.gestures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TouchAndHold {

    private String element;
    private float duration;
    private float x;
    private float y;

}
