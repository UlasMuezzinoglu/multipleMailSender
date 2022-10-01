package com.ulas.springcoretemplate.interfaces.service.other;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import net.minidev.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.function.Function;

public interface JwtService {
    JSONObject decodeJwtToken(String token);

    String getUsernameFromToken();

    String getUsernameFromToken(String token);

    String generateTokenForLogin(String username, int expirationDays, List<SimpleGrantedAuthority> authorities);

    String generateTokenForLogin(String username, Collection<? extends GrantedAuthority> authorities, String tokenId);

    default String generateTokenForLogin(String username, Collection<? extends GrantedAuthority> authorities) {
        String tokenId = UUID.randomUUID().toString();
        return generateTokenForLogin(username, authorities, tokenId);
    }

    String generateToken(String username, Collection<? extends GrantedAuthority> authorities, String tokenId, Date expiration);

    String getTokenId();

    Claims getBody(String token);

    Jws<Claims> extractClaims(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    boolean isTokenExpired(Date time);

    Date getExpiration(String token);

    List<Map<String, String>> getAuthorities(String token);

    boolean isValidRole(String username, List<SimpleGrantedAuthority> authorities);
}
