package com.ulas.springcoretemplate.util;

import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.RedisMapName;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.other.S3Services;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.service.AttemptService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;

import static com.ulas.springcoretemplate.constant.ErrorConstants.NUMBER_OF_SMS_REQUEST_EXCEEDED;
import static com.ulas.springcoretemplate.constant.GeneralConstants.*;

@Component
@NoArgsConstructor
public class MethodUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");
    private static final int TCKN_LENGTH = 11;
    private static final String EMPTY_STRING = "";

    private LoggingBean LOG;
    private S3Services s3Services;
    private HttpServletRequest httpServletRequest;
    private AttemptService attemptService;

    @Autowired
    public MethodUtils(LoggingBean LOG, S3Services s3Services,
                       HttpServletRequest httpServletRequest, AttemptService attemptService) {
        this.LOG = LOG;
        this.s3Services = s3Services;
        this.attemptService = attemptService;
        this.httpServletRequest = httpServletRequest;
    }

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public static Date getCurrentDate() {
        return Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static <U, S extends Serializable> U getAttribute(S s) {
        return (U) getSession().getAttribute(s.toString());
    }

    public static <T> T getActiveUser() {
        return getAttribute(ACTIVE_USER);
    }

    public static Boolean isBlank(String token) {
        return token == null || token.length() == 0 || token.equals("null") || token.equals("undefined");
    }

    public static <V extends Serializable> void setAttribute(String s, V v) {
        getSession().setAttribute(s, v);
    }

    public static boolean isNotBlank(String item) {
        return !isBlank(item);
    }

    public static int generateSixLengthRandomNumber() {
        return (int) (Math.random() * (MAX - MIN + 1)) + MIN;
    }

    public String getClientIp() {
        final String LOCALHOST_IPV4 = "127.0.0.1";
        final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

        String ipAddress = httpServletRequest.getHeader("X-Forwarded-For");
        if (MethodUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress))
            ipAddress = httpServletRequest.getHeader("Proxy-Client-IP");

        if (MethodUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress))
            ipAddress = httpServletRequest.getHeader("WL-Proxy-Client-IP");

        if (MethodUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = httpServletRequest.getRemoteAddr();
            if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress))
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    LOG.util.error("Exception occured while extracting " +
                            "client IP from request. || Message: {}", e.getMessage());
                }
        }

        if (!MethodUtils.isBlank(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0)
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));

        return ipAddress;
    }

    public int generateRandomNumber() {
        if (isNotBlank(activeProfile) && !activeProfile.equals("prod"))
            return DEFAULT_CODE;
        return generateSixLengthRandomNumber();
    }


    public static String generateUuid() {
        String uuid = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        return uuid.substring(0, 9);
    }

    public static String toSlug(String... inputs) {
        String finalInput = "";
        for (String item : inputs) {
            if (isBlank(item))
                continue;
            item = item.toLowerCase();
            item = item.replace('ı', 'i').replace('ğ', 'g').replace('ö', 'o').replace('ü', 'u').replace('ş', 's').replace('ç', 'c');
            finalInput = finalInput + "-" + item;
        }

        String nowhitespace = WHITESPACE.matcher(finalInput).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");
        slug = slug.replaceAll("/", "-")
                .replaceAll("--", "-")
                .replaceAll("&", "-")
                .toLowerCase(Locale.ENGLISH);
        return slug;
    }

    public static void createNewUserUrl(UserEntity userEntity, UserRepository userRepository) {

        String url = null;

        ArrayList<String> toSlugEntities = new ArrayList<String>(Arrays.asList(Arrays.toString(userEntity.getFirstName().split(" ")) +"-"+ Arrays.toString(userEntity.getLastName().split(" "))));


        toSlugEntities.add(MethodUtils.generateUuid());

        url = MethodUtils.toSlug(toSlugEntities.toArray(String[]::new));
        while (userRepository.existsByUrl(url)) {
            toSlugEntities.remove(toSlugEntities.size() - 1);
            toSlugEntities.add(MethodUtils.generateUuid());
            url = MethodUtils.toSlug(toSlugEntities.toArray(String[]::new));
        }
        userEntity.setUrl(url);
    }

    public void checkNumberOfSmsRequests() {
        String ipAddress = getClientIp();
        boolean exceeded = attemptService.isBlocked(ipAddress, RedisMapName.SMS);
        if (exceeded) throw new CustomException(NUMBER_OF_SMS_REQUEST_EXCEEDED);
        attemptService.incrementAttemptNumber(ipAddress, RedisMapName.SMS, 1800);
    }

    public static <T> T removeAttribute(String s) {
        T t = (T) getSession().getAttribute(s);
        getSession().removeAttribute(s);
        return t;
    }

    /**
     * this method provide creates json state for captured exception and its instance
     *
     * @param status as HttpStatus Current http status to put in respond
     * @param ex     as Exception in order to control and set the information of the caught error.
     * @return Error output that will return to frontend as Object/Map
     */
    public static Object prepareErrorJSON(final HttpStatus status, final Exception ex) {
        Map respond = new HashMap();
        System.out.println("ex.getMessage(): " + ex.getMessage());
        respond.put("status", status.value());
        respond.put("error", status.getReasonPhrase());
        respond.put("code", ex.getMessage());
        respond.put("time", ZonedDateTime.now(ZoneId.of("Z")));
        if (!(ex instanceof CustomException)) {
            respond.put("code", "0500");
        }
        return respond;
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null)
            return request.getRemoteAddr();
        return xfHeader.split(",")[0];
    }
    public static boolean requestSourceDevice(HttpServletRequest request) {
        ContentCachingRequestWrapper contentCachingRequestWrapper = wrapRequest(request);
        Enumeration<String> path = contentCachingRequestWrapper.getHeaders("referer");
        return path.hasMoreElements();
    }

    public static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }
}
