package com.pwc.core.framework.processors.mobile.gestures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scroll {

    private String element;
    private String name;
    private String direction;
    private String predicateString;
    private boolean toVisible;

}
