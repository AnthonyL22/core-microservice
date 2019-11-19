package com.pwc.core.framework.listeners;

import com.pwc.core.framework.annotations.Issue;
import com.pwc.core.framework.annotations.MaxRetryCount;
import com.pwc.core.framework.annotations.TestCase;

@Issue("STORY-1234")
public class SampleTest {

    private String name;

    public String testLoginNoAnnotationTest() {
        return name;
    }

    @TestCase("TC-1234")
    @Issue("ISS-12345")
    @MaxRetryCount(3)
    public String testLoginWithAnnotationTest() {
        return name;
    }

}
