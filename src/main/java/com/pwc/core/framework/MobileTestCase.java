package com.pwc.core.framework;


public abstract class MobileTestCase extends MicroserviceTestSuite {

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
