package com.ulas.springcoretemplate.config;


import com.ulas.springcoretemplate.property.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;

    @Bean
    public WebMvcConfigurer corsConfigurerForProd() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping(corsProperties.getMapping())
                        .allowedOrigins(corsProperties.getOrigins())
                        .allowCredentials(corsProperties.isAllowedCredentials())
                        .allowedMethods(corsProperties.getMethods())
                        .allowedHeaders(corsProperties.getAllowedHeaders())
                        .exposedHeaders(corsProperties.getExposedHeaders());
            }
        };
    }
}
