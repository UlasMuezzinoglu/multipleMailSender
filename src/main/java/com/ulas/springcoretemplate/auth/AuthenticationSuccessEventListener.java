package com.ulas.springcoretemplate.auth;


import com.ulas.springcoretemplate.enums.RedisMapName;
import com.ulas.springcoretemplate.service.AttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AttemptService attemptService;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            attemptService.delete(request.getRemoteAddr(), RedisMapName.LOGIN_ATTEMPT);
        } else {
            attemptService.delete(xfHeader.split(",")[0], RedisMapName.LOGIN_ATTEMPT);
        }
    }
}
