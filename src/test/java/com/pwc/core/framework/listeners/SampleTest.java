package com.pwc.core.framework.listeners;

import com.pwc.core.framework.annotations.Issue;
import com.pwc.core.framework.annotations.MaxRetryCount;

@Issue("STORY-1234")
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
