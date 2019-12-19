package com.pwc.core.framework.service;

import com.pwc.core.framework.controller.WebEventController;
import com.pwc.core.framework.driver.MicroserviceMobileDriver;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Component
public class MobileEventService extends WebEventController {

    private MicroserviceMobileDriver microserviceMobileDriver;
    private long timeOutInSeconds;
    private long sleepInMillis;
    private long pageTimeoutInSeconds;
    private boolean videoCaptureEnabled;
    private boolean waitForAjaxRequestsEnabled;
    private String url;

    public MobileEventService() {
    }

    public MobileEventService(MicroserviceMobileDriver driver) {
        this.microserviceMobileDriver = driver;
    }

    /**
     * Resets the currently running app together with the session.
     */
    public void setApp() {

        //ToDo: add here in the future
        //this.microserviceMobileDriver.blah

    }


}
