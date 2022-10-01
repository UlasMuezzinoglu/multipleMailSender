package com.ulas.springcoretemplate.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulas.springcoretemplate.auth.JwtConfig;
import com.ulas.springcoretemplate.auth.UsernameAndPasswordAuthenticationRequest;
import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.service.home.LoginService;
import com.ulas.springcoretemplate.interfaces.service.home.RegisterService;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.redis.RedisTokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.ulas.springcoretemplate.constant.GeneralConstants.SMS_CODE;
import static com.ulas.springcoretemplate.util.MethodUtils.removeAttribute;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String authenticationAttemptMessage =
            "New authentication attempt. Username: {} || Password: {}";
    private static final String unsuccessfulAuthenticationMessage =
            "Login FAILED for user {}";
    private static final String successfulAuthenticationMessage =
            "User {} successfully logged-in.";
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final RedisTokenManager redisTokenManager;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final JwtService jwtService;
    private final LoggingBean LOG = new LoggingBean();
    UsernameAndPasswordAuthenticationRequest authenticationRequest;
    private String attemptedUsername = "";

    public JwtUsernameAndPasswordAuthenticationFilter(final AuthenticationManager authenticationManager,
                                                      final JwtConfig jwtConfig, final RedisTokenManager redisTokenManager,
                                                      final RegisterService registerService, final LoginService loginService,
                                                      final JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.redisTokenManager = redisTokenManager;
        this.registerService = registerService;
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    /**
     * this method attempts Authentication with
     * - username (e-mail or phone number), and
     * - password (the 6-digit verification code).
     *
     * @param request  as HttpServletRequest containing the user's authentication input
     * @param response as HttpServletResponse
     * @return Authentication: Authentication object containing username and password
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
                    UsernameAndPasswordAuthenticationRequest.class);

            attemptedUsername = authenticationRequest.getPhoneNumber();
            LOG.activity.info(authenticationAttemptMessage,
                    attemptedUsername,
                    authenticationRequest.getPassword());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getPhoneNumber(),
                    authenticationRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            LOG.activity.error(unsuccessfulAuthenticationMessage,
                    authenticationRequest.getPhoneNumber());
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * this method configures successful login by user
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse for that token information can be put in
     * @param chain      FilterChain
     * @param authResult Authentication contains the information contained in the token
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        try {
            String tokenId = UUID.randomUUID().toString();
            String token = jwtService.generateTokenForLogin(authResult.getName(), authResult.getAuthorities(), tokenId);
            redisTokenManager.registerJwtToken(tokenId);
            loginService.checkLoginOrigin(request, authResult);
            LOG.activity.info(successfulAuthenticationMessage, authResult.getName());
            boolean isRegisterCompleted = registerService.newUserOperations(authResult.getName());
            removeAttribute(SMS_CODE);
            response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
            response.addHeader("isRegisterCompleted", String.valueOf(isRegisterCompleted));
        } catch (Exception e) {
            LOG.activity.error("Authentication succeeded, but user {} coldn't be verified. " +
                    "Login failed with message: {}", authResult.getName(), e.getMessage());
        }
    }
}

