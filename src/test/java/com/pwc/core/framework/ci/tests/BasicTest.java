package com.pwc.core.framework.ci.tests;

import com.pwc.core.framework.FrameworkConstants;

import static com.pwc.logging.service.LoggerService.*;

public class BasicTest {

    public void testBasic() {

        FEATURE("Smoke Test");
        SCENARIO("Basic Functionality");
        GIVEN("I am logged in page=%s and authenticated user=%s", "home", "anthony lombardo");

        WHEN("I view the Landing page without doing a search for env=%s", FrameworkConstants.AUTOMATION_TEST_ENVIRONMENT);

        THEN("Basic components are present in body of page");

        AND("I can click the BACK button");

        BUT("I go back to the Home page");

    }

}
