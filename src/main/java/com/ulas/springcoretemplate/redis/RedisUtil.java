package com.ulas.springcoretemplate.redis;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class RedisUtil {

    public static <R, E extends Enum<E>, K extends Serializable> R sessionMiddlewareGetData(final HttpSession session, final E key) {
        return sessionMiddlewareGetData(session, key, 0);
    }

    public static <R, E extends Enum<E>, K extends Serializable> R sessionMiddlewareGetData(final HttpSession session, E e, final int pageNumber) {
        String sessionKey = getModelSessionKey(e, pageNumber);
        return (R) session.getAttribute(sessionKey);
    }

    public static <E extends Enum<E>, K extends Serializable> String getModelSessionKey(final E e) {
        return getModelSessionKey(e, 0);
    }

    public static <E extends Enum<E>, K extends Serializable> String getModelSessionKey(final E e, final int pageNumber) {
        return e.name() + "_" + pageNumber;
    }
}
