package com.ulas.springcoretemplate.config;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import static com.ulas.springcoretemplate.util.MethodUtils.getCurrentDate;

@Component("dateTimeProvider")
public class CustomDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(getCurrentDate().toInstant());
    }
}
