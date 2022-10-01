package com.ulas.springcoretemplate.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    private String username;
    private String password;
    private String header;
    private String url;
}
