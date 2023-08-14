package com.pwc.core.framework.processors.mobile.gestures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoubleTap {

    private WebElement element;
    private float x;
    private float y;

}
