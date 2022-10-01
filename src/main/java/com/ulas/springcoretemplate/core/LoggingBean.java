package com.ulas.springcoretemplate.core;


import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class LoggingBean {
    public Logger
            service = LoggerFactory.getLogger("service"),
            activity = LoggerFactory.getLogger("activity"),
            performance = LoggerFactory.getLogger("performance"),
            admin = LoggerFactory.getLogger("admin"),
            util = LoggerFactory.getLogger("util"),
            mail = LoggerFactory.getLogger("mail"),
            notification = LoggerFactory.getLogger("notification"),
            database = LoggerFactory.getLogger("database"),
            sms = LoggerFactory.getLogger("SMS"),
            s3 = LoggerFactory.getLogger("S3"),
            core = LoggerFactory.getLogger("com.springCoreTemplate"),
            attempt = LoggerFactory.getLogger("attempt"),
            login = LoggerFactory.getLogger("login"),
            iyzico = LoggerFactory.getLogger("iyzico"),
            register = LoggerFactory.getLogger("register"),
            filter = LoggerFactory.getLogger("filter");
}
