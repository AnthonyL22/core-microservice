package com.pwc.core.framework.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SeleniumArgument {

    START_MAXIMIZED("--start-maximized"),
    DISABLE_SHM("--disable-dev-shm-usage"),
    DISABLE_WEB_SECURITY("--disable-web-security"),
    IGNORE_CERTIFICATE_ERRORS("--ignore-certificate-errors"),
    ALLOW_INSECURE_CONTENT("--allow-running-insecure-content"),

    private final String value;

}
