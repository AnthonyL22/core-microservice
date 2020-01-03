package com.pwc.core.framework;


import com.pwc.core.framework.data.MobileGesture;

import java.util.Map;

public abstract class MobileTestCase extends MicroserviceTestSuite {

    /**
     * Executes JSONWP command and returns a response.
     *
     * @param elementIdentifier unique element identifying locator
     * @param mobileGesture     Mobile Gesture to perform
     * @param parameters        gesture parameters to execute
     * @return a result response
     */
    public Object executeJavascript(final String elementIdentifier, MobileGesture mobileGesture, Map<String, Object> parameters) {

        return mobileEventController.getMobileEventService().executeJavascript(elementIdentifier, mobileGesture, parameters);
    }

    /**
     * Executes JSONWP command and returns a response.
     *
     * @param mobileGesture Mobile Gesture to perform
     * @param parameters    gesture parameters to execute
     * @return a result response
     */
    public Object executeJavascript(MobileGesture mobileGesture, Map<String, Object> parameters) {

        return mobileEventController.getMobileEventService().executeJavascript(mobileGesture, parameters);
    }

    /**
     * Activates the given app if it installed, but not running or if it is running in the
     * background.
     *
     * @param bundleId the bundle identifier (or app id) of the app to activate.
     */
    protected void activateApp(final String bundleId) {

        mobileEventController.getMobileEventService().activateApp(bundleId);
    }

    /**
     * Resets the currently running app together with the session.
     */
    protected void resetApp() {

        mobileEventController.getMobileEventService().resetApp();
    }

    /**
     * Remove the specified app from the device (uninstall).
     *
     * @param bundleId the bundle identifier (or app id) of the app to remove.
     */
    protected void removeApp(final String bundleId) {

        mobileEventController.getMobileEventService().removeApp(bundleId);
    }

}
