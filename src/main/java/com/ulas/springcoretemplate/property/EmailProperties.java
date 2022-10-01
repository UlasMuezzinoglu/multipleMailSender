package com.ulas.springcoretemplate.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "emailproperties")
@Component
public class EmailProperties {
    private String from;
    private String personal;
}
