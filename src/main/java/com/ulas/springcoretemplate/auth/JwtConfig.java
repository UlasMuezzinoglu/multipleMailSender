package com.ulas.springcoretemplate.auth;


import lombok.Data;
import org.apache.http.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private int tokenExpirationForVerification;
    private int tokenExpirationForLogin;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

}
