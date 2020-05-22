package com.pwc.core.framework.processors.mobile.gestures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DragFromToForDuration {

    private String element;
    private float duration;
    private float fromX;
    private float fromY;
    private float toX;
    private float toY;

}
