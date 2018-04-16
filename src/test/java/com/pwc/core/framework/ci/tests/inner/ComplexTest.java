package com.pwc.core.framework.ci.tests.inner;

import com.pwc.core.framework.ci.parent.Data;
import com.pwc.core.framework.ci.parent.TestConstants;

import static com.pwc.logging.service.LoggerService.FEATURE;
import static com.pwc.logging.service.LoggerService.GIVEN;
import static com.pwc.logging.service.LoggerService.SCENARIO;
import static com.pwc.logging.service.LoggerService.THEN;
import static com.pwc.logging.service.LoggerService.WHEN;

public class ComplexTest {

    public void testComplex() {

        FEATURE("Inner Complex Test");
        SCENARIO("Basic Functionality");
        GIVEN("I am logged in page=%s and authenticated user=%s", "home", "anthony");

        WHEN("I view the page named=%s", "HOME");

        THEN("Complex component=%s in page=%s are present in body of page=%s", Data.BUTTON, "loadit", TestConstants.PAGE_TITLE);

    }

}
