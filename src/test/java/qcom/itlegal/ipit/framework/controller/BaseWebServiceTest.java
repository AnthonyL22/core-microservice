package qcom.itlegal.ipit.framework.controller;

import qcom.itlegal.ipit.framework.FrameworkConstants;
import qcom.itlegal.ipit.framework.command.WebServiceCommand;

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
