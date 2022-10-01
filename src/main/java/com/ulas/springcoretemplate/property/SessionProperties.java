package com.ulas.springcoretemplate.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sessionproperties")
public class SessionProperties {
    private int objectTimeout;
    private int userTimeout;
    private int verificationTimeout;
    private int cookieMaxAge;
    private boolean secureCookie;
    private String cookieName;
    private String cookiePath;
    private boolean httpCookieOnly;
    private String sameSite;
}
