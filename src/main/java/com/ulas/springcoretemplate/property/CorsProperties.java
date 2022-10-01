package com.ulas.springcoretemplate.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String mapping;
    private String[] methods;
    private String[] origins;
    private boolean allowedCredentials;
    private String[] allowedHeaders;
    private String[] exposedHeaders;
}
