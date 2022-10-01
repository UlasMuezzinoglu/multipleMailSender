package com.ulas.springcoretemplate.config;

import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.util.MethodUtils;
import org.slf4j.MDC;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class AuditorAwareConfig implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String phoneNumber = MDC.get("username");
        if (!MethodUtils.isBlank(phoneNumber))
            return Optional.of(phoneNumber);
        return Optional.of(Role.VISITOR.name());
    }
}
