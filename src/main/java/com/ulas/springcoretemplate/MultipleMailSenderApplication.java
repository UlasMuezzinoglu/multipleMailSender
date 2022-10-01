package com.ulas.springcoretemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@Configuration
@RestController
@ServletComponentScan
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, keyspaceNotificationsConfigParameter = "")
@EntityScan(basePackages = {"com.ulas.springCoreTemplate.model.entity"})
@EnableMongoRepositories(basePackages = "com.ulas.springcoretemplate.interfaces.repository")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, QuartzAutoConfiguration.class}, scanBasePackages = "com.ulas.springcoretemplate")
public class MultipleMailSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultipleMailSenderApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
