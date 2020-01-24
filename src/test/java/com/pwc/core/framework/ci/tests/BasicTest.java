package com.pwc.core.framework.ci.tests;

import com.pwc.core.framework.ci.parent.Data;
import com.pwc.core.framework.ci.parent.Groups;
import com.pwc.core.framework.ci.parent.TestConstants;
import org.testng.annotations.Test;

import static com.pwc.logging.service.LoggerService.AND;
import static com.pwc.logging.service.LoggerService.BUT;
import static com.pwc.logging.service.LoggerService.FEATURE;
import static com.pwc.logging.service.LoggerService.GIVEN;
import static com.pwc.logging.service.LoggerService.SCENARIO;
import static com.pwc.logging.service.LoggerService.THEN;
import static com.pwc.logging.service.LoggerService.WHEN;

public class BasicTest {

    private static final String MY_MESSAGE = "Hello";

    @Test(groups = {Groups.SMOKE})
    public void testBasic() {

        FEATURE("Smoke Test");
        SCENARIO("Basic Functionality");
        GIVEN("I am logged in page=%s and authenticated user=%s", "home", "nicole");

        WHEN("I view the Landing page without doing a search for env=%s", "dev");

        THEN("Basic components are present in body of message=%s", MY_MESSAGE);

        AND("I can click the BACK button");

        BUT("I go back to the Home page");

    }

}
