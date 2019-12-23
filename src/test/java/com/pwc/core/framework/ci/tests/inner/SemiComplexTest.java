package com.pwc.core.framework.ci.tests.inner;

import com.pwc.core.framework.ci.parent.Data;
import com.pwc.core.framework.ci.parent.TestConstants;

import static com.pwc.logging.service.LoggerService.AND;
import static com.pwc.logging.service.LoggerService.BUT;
import static com.pwc.logging.service.LoggerService.FEATURE;
import static com.pwc.logging.service.LoggerService.FINALLY;
import static com.pwc.logging.service.LoggerService.GIVEN;
import static com.pwc.logging.service.LoggerService.IF;
import static com.pwc.logging.service.LoggerService.IMAGE;
import static com.pwc.logging.service.LoggerService.NOT;
import static com.pwc.logging.service.LoggerService.OR;
import static com.pwc.logging.service.LoggerService.SCENARIO;
import static com.pwc.logging.service.LoggerService.THEN;
import static com.pwc.logging.service.LoggerService.WHEN;

public class SemiComplexTest {

    public void testSemiComplex() {

        FEATURE("Semi Complex Test");
        SCENARIO("Basic Functionality");
        GIVEN("I am not logged in page=%s and authenticated user=%s", "home", "anthony");

        WHEN("I different page named=%s", "Login");

        THEN("Complex component=%s in page=%s are present in body of page=%s", Data.BUTTON, "loadit", TestConstants.PAGE_TITLE);

        BUT("I go back to the Home page");

        FINALLY("I am able to complete the transaction");

    }

}
