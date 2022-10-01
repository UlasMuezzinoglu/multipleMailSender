package com.ulas.springcoretemplate.service;

import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.redis.RedisStorageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import static com.ulas.springcoretemplate.constant.ErrorConstants.FORBIDDEN;
import static com.ulas.springcoretemplate.util.MethodUtils.getRequest;

@Lazy
@Service
@RequiredArgsConstructor
public class AttemptService {

    private static final Integer MAX_ATTEMPT = 10;
    private final LoggingBean LOG;
    private final RedisTemplate redisTemplate;
    private final RedisStorageManager redisStorageManager;


    @Value("${domains.admin}")
    private String adminDomain;


    /**
     * this method provides wrap given request and checks is instance of ContentCachingRequestWrapper then return
     * else creates new instance and returns
     *
     * @param request as HttpServletRequest for instance check
     * @return ContentCachingRequestWrapper
     */
    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    /**
     * this method provides is login successes set invalidate from attemptsCache using key
     *
     * @param key as String for to send invalidate method
     */
    public <E extends Enum<E>> void delete(String key, E mapName) {
        try {
            redisStorageManager.map.delete(mapName.name(), key);
        } catch (Exception e) {

        }
    }

    /**
     * this method provides is login failed put the key to cache
     *
     * @param key as String for to put attempsCache
     */
    public synchronized <E extends Enum<E>> void incrementAttemptNumber(String key, E mapName, long timeOut) {
        if (redisStorageManager.map.hasKey(mapName.name(), key)) {
            Integer attempts = Integer.valueOf(redisStorageManager.map.get(mapName.name(), key).toString());
            redisStorageManager.map.put(mapName.name(), key, attempts.toString());
        } else {
            redisStorageManager.map.put(mapName.name(), key, String.valueOf(1));
        }
        redisTemplate.expire(mapName.name(), timeOut, TimeUnit.SECONDS);
    }

    /**
     * this method provides is login failed put the key to cache
     *
     * @param key as String for to put attempsCache
     */
    public <E extends Enum<E>> boolean isBlocked(String key, E mapName) {
        try {
            if (redisStorageManager.map.hasKey(mapName.name(), key))
                if (Integer.valueOf(redisStorageManager.map.get(mapName.name(), key).toString()) >= MAX_ATTEMPT) {
                    LOG.service.error("User has exceeded the login attempt limit.");
                    return true;
                }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * this method provide checks is user login from admin login page ? uses by HttpServletRequest and UserEntity
     *
     * @param userEntity as UserEntity for role check
     * @return boolean
     */
    public boolean isFromPanelLoginPage(UserEntity userEntity) {
        ContentCachingRequestWrapper contentCachingRequestWrapper = wrapRequest(getRequest());
        Enumeration<String> path = contentCachingRequestWrapper.getHeaders("referer");
        String url = "";
        if (path.hasMoreElements())
            url = path.nextElement();

        if (url.contains(adminDomain)
                && !(userEntity.getRole().equals(Role.ADMIN)))
            throw new CustomException(FORBIDDEN);
        return false;
    }
}
