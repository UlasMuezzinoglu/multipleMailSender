package com.ulas.springcoretemplate.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "asset")
public class AssetProperties {
    private String language;
}
