package com.pwc.core.framework.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SeleniumArgument {

    NO_SANDBOX("--no-sandbox"),
    START_MAXIMIZED("--start-maximized"),
    DISABLE_SHM("--disable-dev-shm-usage"),
    DISABLE_SETUID_SANDBOX("--disable-setuid-sandbox"),
    DISABLE_WEB_SECURITY("--disable-web-security"),
    IGNORE_CERTIFICATE_ERRORS("--ignore-certificate-errors"),
    ALLOW_INSECURE_CONTENT("--allow-running-insecure-content"),
    REMOTE_ALLOW_ORIGINS("--remote-allow-origins=*");

    private final String value;

}
