package com.ulas.springcoretemplate.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "onesignal")
public class OnesignalProperties {
    private String REST_API_KEY;
    private String APP_ID;
    private String ANDROID_CHANNEL_ID;
}
