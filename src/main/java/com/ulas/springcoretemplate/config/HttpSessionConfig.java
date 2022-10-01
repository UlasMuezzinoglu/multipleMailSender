package com.ulas.springcoretemplate.config;


import com.ulas.springcoretemplate.property.SessionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class HttpSessionConfig {

    @Autowired
    private SessionProperties sessionProperties;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setUseHttpOnlyCookie(sessionProperties.isHttpCookieOnly());
        serializer.setSameSite(sessionProperties.getSameSite());
        serializer.setCookieMaxAge(sessionProperties.getCookieMaxAge());
        serializer.setUseSecureCookie(sessionProperties.isSecureCookie());
        serializer.setCookiePath(sessionProperties.getCookiePath());
        serializer.setCookieName(sessionProperties.getCookieName());
        return serializer;
    }
}
