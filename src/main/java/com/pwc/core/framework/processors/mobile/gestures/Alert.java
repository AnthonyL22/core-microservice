package com.pwc.core.framework.processors.mobile.gestures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    private String element;
    private String buttonLabel;

}
