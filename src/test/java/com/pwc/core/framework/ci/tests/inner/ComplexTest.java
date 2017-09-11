package com.pwc.core.framework.ci.tests.inner;

import static com.pwc.logging.service.LoggerService.*;

public class ComplexTest {

    public void testComplex() {

        FEATURE("Inner Complex Test");
        SCENARIO("Basic Functionality");
        GIVEN("I am logged in page=%s and authenticated user=%s", "home", "anthony");

        WHEN("I view the page named=%s", "HOME");

        THEN("Complex component=%s in page=%s are present in body of page=%s", "Submit Button", "loadit", "login");

    }

}
