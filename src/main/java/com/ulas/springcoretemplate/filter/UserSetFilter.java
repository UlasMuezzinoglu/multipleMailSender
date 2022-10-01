package com.ulas.springcoretemplate.filter;


import com.ulas.springcoretemplate.constant.ErrorConstants;
import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.DeviceTypeEnum;
import com.ulas.springcoretemplate.enums.Status;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.util.MethodUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.ulas.springcoretemplate.constant.ErrorConstants.INVALID_OPERATION;
import static com.ulas.springcoretemplate.constant.ErrorConstants.INVALID_TOKEN;
import static com.ulas.springcoretemplate.constant.GeneralConstants.ACTIVE_USER;
import static com.ulas.springcoretemplate.enums.Role.VISITOR;
import static com.ulas.springcoretemplate.util.MethodUtils.requestSourceDevice;
import static com.ulas.springcoretemplate.util.MethodUtils.setAttribute;

@Component
public class UserSetFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LoggingBean LOG;

    @Autowired
    public UserSetFilter(UserRepository userRepository, JwtService jwtService, LoggingBean log) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.LOG = log;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put("clientIp", MethodUtils.getClientIpAddress(request));
        request.getSession().setAttribute("clientIp", MethodUtils.getClientIpAddress(request));
        boolean isRequestFromMobile = requestSourceDevice(request);
        if (isRequestFromMobile) {
            request.getSession().setAttribute("device", "Web");
            MDC.put("device", DeviceTypeEnum.WEB.name());
        } else {
            request.getSession().setAttribute("device", "Mobile");
            MDC.put("device", DeviceTypeEnum.MOBILE.name());
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        MDC.put("sessionId", request.getSession().getId());
        MDC.put("requestId", UUID.randomUUID().toString());
        request.getSession().setAttribute("sessionId", request.getSession().getId());
        request.getSession().setAttribute("requestId", UUID.randomUUID().toString());

        // Put the active user in the request.
        try {
            if (!MethodUtils.isBlank(token)) {
                String username = null;

                try {
                    username = jwtService.getUsernameFromToken(token);
                } catch (Exception e) {
                    throw new CustomException(INVALID_TOKEN);
                }

                UserEntity activeUser = (UserEntity) request.getSession().getAttribute(ACTIVE_USER);
                if (activeUser == null) {
                    activeUser = userRepository.findByUsername(username);
                    if (activeUser == null)
                        throw new CustomException(ErrorConstants.USER_NOT_EXIST);
                    request.getSession().setAttribute(ACTIVE_USER, activeUser);
                }

                if (!activeUser.getUsername().equals(username))
                    throw new CustomException(INVALID_OPERATION);

                if (activeUser.getStatus().equals(Status.BANNED)) {
                    throw new CustomException(ErrorConstants.THIS_USER_IS_BANNED);
                }
                if (activeUser.getStatus().equals(Status.DELETED)) {
                    throw new CustomException(ErrorConstants.THIS_USER_IS_DELETED);
                }

                MDC.put("username", activeUser.getUsername());

            } else {
                MDC.put("role", VISITOR.name());
                setAttribute("role", VISITOR.name());
            }
        } catch (Exception e) {
            LOG.filter.error("UserSet filter exception, message: " + e.getMessage());
            throw e;
        }

        filterChain.doFilter(request, response);
    }
}
