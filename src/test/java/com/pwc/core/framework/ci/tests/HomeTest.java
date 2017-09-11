package com.pwc.core.framework.ci.tests;

import com.pwc.core.framework.FrameworkConstants;

import static com.pwc.logging.service.LoggerService.*;

public class HomeTest {

    public void testHome() {

        FEATURE("Home Test");
        SCENARIO("Home Functionality");
        GIVEN("I am logged in page=%s and authenticated user=%s", "home", "anthony lombardo");

        WHEN("I view the Home page without doing a search for env=%s", FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT);

        THEN("Basic components are present");

        AND("I can click the anything");

        BUT("I go back to the Home page");

    }

}
