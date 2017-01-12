package com.pwc.core.framework.listeners;

import com.pwc.core.framework.annotations.MaxRetryCount;

public class SampleTest {

    private String name;

    public String testLoginNoAnnotationTest() {
        return name;
    }

    @MaxRetryCount(3)
    public String testLoginWithAnnotationTest() {
        return name;
    }

}
