package com.pwc.core.framework;


import com.pwc.core.framework.data.CssProperty;
import com.pwc.core.framework.data.WebElementAttribute;
import org.openqa.selenium.logging.LogEntry;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

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
