package com.pwc.core.framework.controller;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.command.WebServiceCommand;

public class BaseWebServiceTest extends WebServiceController {

    protected enum UnitTestWebServiceCommand implements WebServiceCommand {
        UNIT_TEST_COMMAND(FrameworkConstants.GET_REQUEST, "", "foobar");
        private String requestMethodType;
        private String requestMapping;
        private String methodName;

        UnitTestWebServiceCommand(String type, String mapping, String name) {
            requestMethodType = type;
            requestMapping = mapping;
            methodName = name;
        }

        @Override
        public String methodType() {
            return this.requestMethodType;
        }

        @Override
        public String mappingName() {
            return this.requestMapping;
        }

        @Override
        public String methodName() {
            return this.methodName;
        }
    }
}
