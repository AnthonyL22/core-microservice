package com.pwc.core.framework;


import java.util.List;

public abstract class MobileTestCase extends MicroserviceTestSuite {

    /**
     * ToDo: future methods here
     *
     * @return unique List of network tab requests
     */
    protected List<String> webNetworkRequests() {
        return webEventController.getWebEventService().getPageRequests();
    }

}
