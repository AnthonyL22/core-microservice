package com.pwc.core.framework.util;

import org.openqa.selenium.WebElement;

import static com.pwc.logging.service.LoggerService.LOG;

public class WebElementUtils {

    /**
     * Utility method which returns the XPATH for a given WebElement.
     *
     * @param webElement WebElement to interrogate it's xPath out of
     * @return webElement xPath of WebElement
     */
    public static String getXPathOfWebElement(final WebElement webElement) {
        StringBuilder xpath = new StringBuilder();
        try {
            xpath.append(org.apache.commons.lang3.StringUtils.substringBetween(webElement.toString(), "xpath:", "]]").trim());
            xpath.append("]");
        } catch (Exception e) {
            LOG(false, "Exception e=%s", e);
        }
        return xpath.toString();
    }

    /**
     * Pause thread based on duration.
     *
     * @param pauseDuration time in MS to pause
     */
    public static void pauseForMilliseconds(final long pauseDuration) {

        long now = 0L;
        long start = System.currentTimeMillis();
        while (now < start + pauseDuration) {
            now = System.currentTimeMillis();
        }
    }

}
