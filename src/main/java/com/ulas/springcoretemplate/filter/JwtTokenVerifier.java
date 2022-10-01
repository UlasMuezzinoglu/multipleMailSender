package com.ulas.springcoretemplate.filter;


import com.ulas.springcoretemplate.auth.JwtConfig;
import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.redis.RedisTokenManager;
import com.ulas.springcoretemplate.util.MethodUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final RedisTokenManager redisTokenManager;
    private final UserRepository userRepository;
    private final LoggingBean LOG;

    @Autowired
    public JwtTokenVerifier(final JwtConfig jwtConfig, final JwtService jwtService,
                            final RedisTokenManager redisTokenManager, final UserRepository userRepository, LoggingBean log) {
        this.jwtConfig = jwtConfig;
        this.jwtService = jwtService;
        this.redisTokenManager = redisTokenManager;
        this.userRepository = userRepository;
        LOG = log;
    }

    private static boolean isNoteBefore(Date notBefore, Date issueAt) {
        if (notBefore == null || issueAt == null) return true;
        return notBefore.before(issueAt);
    }

    //@SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
        if (MethodUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            getExceptionResponse(response, "0021");
            return;
        }
        final String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {
            String username = null;
            Claims claims = jwtService.getBody(token);
            username = claims.getSubject();

            //String tokenId = claims.getId();
                /*if (redisTokenManager.invalidTokenId(tokenId)) {
                    getExceptionResponse(response, "0403");
                    return;
                }
                 */

            Authentication authentication = generateAuthentication(token, username);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (RuntimeException e) {
            getExceptionResponse(response, "0403");
            LOG.filter.error("jwt token verifier logged exception : " + e.getMessage());
        }
        filterChain.doFilter(httpServletRequest, response);
    }

    private Authentication generateAuthentication(String token, String username) {
        List<Map<String, String>> authorities = (List<Map<String, String>>) jwtService.getBody(token).get("authorities");

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());

        isValidRole(username, simpleGrantedAuthorities);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
        );
    }

    @SneakyThrows
    void getExceptionResponse(HttpServletResponse response, String code) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(String.valueOf(
                new JSONObject().appendField("code", code)
                        .appendField("status", HttpStatus.FORBIDDEN)));
    }

    private void isValidRole(String username, List<SimpleGrantedAuthority> authorities) {
        jwtService.isValidRole(username, authorities);
    }
}

