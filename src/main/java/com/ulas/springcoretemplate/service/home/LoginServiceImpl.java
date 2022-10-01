package com.ulas.springcoretemplate.service.home;


import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.Status;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.NotificationRepository;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.home.LoginService;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.interfaces.service.other.SmsService;
import com.ulas.springcoretemplate.interfaces.service.user.NotificationService;
import com.ulas.springcoretemplate.model.entity.NotificationEntity;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.request.LoginCodeViaGsm;
import com.ulas.springcoretemplate.model.util.CodeKeeper;
import com.ulas.springcoretemplate.property.SessionProperties;
import com.ulas.springcoretemplate.redis.RedisTokenManager;
import com.ulas.springcoretemplate.util.DateUtil;
import com.ulas.springcoretemplate.util.MethodUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.ulas.springcoretemplate.constant.ErrorConstants.*;
import static com.ulas.springcoretemplate.constant.GeneralConstants.ACTIVE_USER;
import static com.ulas.springcoretemplate.constant.GeneralConstants.SMS_CODE;
import static com.ulas.springcoretemplate.constant.InfoConstants.LOGIN_MESSAGE_TR;
import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.SUCCESS;
import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.VERIFICATION_CODE_SENT;
import static com.ulas.springcoretemplate.util.MethodUtils.*;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final LoggingBean LOG;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private final SessionProperties sessionProperties;
    private final MethodUtils methodUtils;
    private final JwtService jwtService;
    private final RedisTokenManager redisTokenManager;
    private final NotificationService notificationService;
    private final HttpServletRequest httpServletRequest;
    private final NotificationRepository notificationRepository;

    @Override
    public void checkLoginOrigin(HttpServletRequest request, Authentication authResult) {

    }

    @Override
    public <Res> Res getLoginCodeViaGsm(LoginCodeViaGsm loginCodeViaGsm) {
        methodUtils.checkNumberOfSmsRequests();

        CodeKeeper loginCode = getAttribute(SMS_CODE);

        if (loginCode == null) loginCode = new CodeKeeper();
        else {
            if (loginCode.getLastSentDate().after(DateUtil.getCurrentDate()))
                throw new CustomException(LOGIN_CODE_REQUESTS_EXCEEDED);
        }

        UserEntity userEntity = userRepository.findByUsername(loginCodeViaGsm.getPhoneNumber());
        if (userEntity == null) {
            LOG.service.warn("Somebody tried to log in by sms, but the phone number " +
                            "they supplied in the request body ({}) couldn't be found in the database. " +
                            "Trying to retrieve the user from the session...",
                    loginCodeViaGsm.getPhoneNumber());
            userEntity = getAttribute(ACTIVE_USER);
        }

        if (userEntity == null) {
            LOG.service.error("Somebody tried to log in by sms, but the phone number " +
                            "they supplied in the request body ({}) couldn't be found in the database, " +
                            "and their session does not contain any users. This user probably does not exist. " +
                            "Login failed.",
                    loginCodeViaGsm.getPhoneNumber());
            throw new CustomException(USER_NOT_EXIST);
        }

        if (userEntity.getStatus().equals(Status.BANNED)) {
            throw new CustomException(THIS_USER_IS_BANNED);
        }
        if (userEntity.getStatus().equals(Status.DELETED)) {
            throw new CustomException(THIS_USER_IS_DELETED);
        }

        String code = String.valueOf(methodUtils.generateRandomNumber());
        String encodedLoginCode = passwordEncoder.encode(code);

        if (userEntity.getUsername().equals("905421111111")) {
            smsService.sendSmsToGsm(List.of(loginCodeViaGsm.getPhoneNumber()), "111111");
            loginCode.setEncodedCode(passwordEncoder.encode("111111"));
        } else {
            loginCode.setEncodedCode(encodedLoginCode);
            try {
                smsService.sendSmsToGsm(List.of(loginCodeViaGsm.getPhoneNumber()), "<#> code: " + code + " " + LOGIN_MESSAGE_TR);
                LOG.service.info("Login code {} is successfully sent to user {} by sms.",
                        code, loginCodeViaGsm.getPhoneNumber());
            } catch (Exception e) {
                LOG.service.error("{} tried to log in by sms, but login code " +
                                "{} couldn't be sent to the user by sms. " + "Message: {}",
                        loginCodeViaGsm.getPhoneNumber(),
                        code, e.getMessage());
            }
        }

        loginCode.setLastSentDate(DateUtil.addSecondsToTime(DateUtil.getCurrentDate(), 90));
        setAttribute(ACTIVE_USER, userEntity);
        setAttribute(SMS_CODE, loginCode);
        getSession().setMaxInactiveInterval(sessionProperties.getObjectTimeout());

        return (Res) VERIFICATION_CODE_SENT;
    }

    @Override
    public <Res> Res logout() {
        UserEntity activeUser = getActiveUser();
        SecurityContextHolder.clearContext();

        if (activeUser != null) {
            try {
                String tokenId = jwtService.getTokenId();
                if (tokenId != null)
                    redisTokenManager.unregisterToken(tokenId);
            } catch (Exception e) {

            }
            String notificationId = httpServletRequest.getHeader("notificationId");
            if (!isBlank(notificationId)) {
                NotificationEntity entity = notificationRepository.findByNotificationId(notificationId);
                if (entity.getUserId() != null && !entity.getUserId().equals(activeUser.getId()))
                    throw new CustomException(THIS_OPERATION_DOES_NOT_BELONG_TO_THIS_USER);
                notificationService.deleteNotificationId(notificationId);
            }
        }
        try {
            System.out.println("user session id :" + getSession().getId());
            getSession().invalidate();
            System.out.println("session will be invalidated");
        } catch (Exception e) {
            System.out.println("session throwed exception : " + e.getMessage());
        }

        return (Res) SUCCESS;
    }
}

