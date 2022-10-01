package com.ulas.springcoretemplate.service.other;


import com.ulas.springcoretemplate.auth.JwtConfig;
import com.ulas.springcoretemplate.constant.ErrorConstants;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.util.DateUtil;
import com.ulas.springcoretemplate.util.MethodUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static com.ulas.springcoretemplate.constant.GeneralConstants.ACTIVE_USER;
import static com.ulas.springcoretemplate.util.MethodUtils.getRequest;
import static com.ulas.springcoretemplate.util.MethodUtils.setAttribute;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

    @SneakyThrows
    public JSONObject decodeJwtToken(String token) {

        Base64.Decoder decoder = Base64.getDecoder();
        String[] chunks = token.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));

        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        return parser.parse(payload, JSONObject.class);
    }

    @SneakyThrows
    public String getUsernameFromToken() {
        String token = getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        return getUsernameFromToken(token);
    }

    @SneakyThrows
    public String getUsernameFromToken(String token) {
        if (MethodUtils.isBlank(token))
            return null;
        JSONObject jsonObject = decodeJwtToken(token);
        return jsonObject.get("sub").toString();
    }

    public String generateTokenForLogin(String username, int expirationDays, List<SimpleGrantedAuthority> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expirationDays)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateTokenForLogin(String username, Collection<? extends GrantedAuthority> authorities, String tokenId) {
        Date expiration = DateUtil.addDaysToDate(jwtConfig.getTokenExpirationForLogin());
        return generateToken(username, authorities, tokenId, expiration);
    }

    @Override
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities, String tokenId, Date expiration) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setId(tokenId)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String getTokenId() {
        String authHeader = getRequest().getHeader(jwtConfig.getAuthorizationHeader());
        final String token = authHeader.replace(jwtConfig.getTokenPrefix(), "");
        return getBody(token).getId();
    }

    @Override
    public Claims getBody(String token) {
        return extractClaims(token).getBody();
    }

    public Jws<Claims> extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractClaims(token).getBody());
    }

    @Override
    public boolean isTokenExpired(Date time) {
        return time == null || time.after(DateUtil.getCurrentDate());
    }

    @Override
    public Date getExpiration(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public List<Map<String, String>> getAuthorities(String token) {
        if (token == null) throw new CustomException(ErrorConstants.INVALID_OPERATION);
        return (List<Map<String, String>>) getBody(token).get("authorities");

    }

    @Override
    public boolean isValidRole(String phoneNumber, List<SimpleGrantedAuthority> authorities) {
        UserEntity activeUser = userRepository.findByPhoneNumber(phoneNumber);
        if (activeUser == null) {
            return false;
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = activeUser.getRole().getGrantedAuthorities();
        boolean isValid = simpleGrantedAuthorities.equals(authorities);
        if (!isValid)
            return false;
        setAttribute(ACTIVE_USER, activeUser);
        return true;
    }
}
