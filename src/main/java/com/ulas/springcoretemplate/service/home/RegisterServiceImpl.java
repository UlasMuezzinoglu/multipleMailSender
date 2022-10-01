package com.ulas.springcoretemplate.service.home;


import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.Status;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.mapper.UserMapper;
import com.ulas.springcoretemplate.interfaces.repository.user.UrlEntityRepository;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.home.RegisterService;
import com.ulas.springcoretemplate.interfaces.service.other.SmsService;
import com.ulas.springcoretemplate.interfaces.service.user.NotificationService;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.request.CompleteRegisterRequest;
import com.ulas.springcoretemplate.model.request.RegisterRequest;
import com.ulas.springcoretemplate.model.util.CodeKeeper;
import com.ulas.springcoretemplate.util.DateUtil;
import com.ulas.springcoretemplate.util.MethodUtils;
import com.ulas.springcoretemplate.util.SeoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.ulas.springcoretemplate.constant.ErrorConstants.*;
import static com.ulas.springcoretemplate.constant.GeneralConstants.*;
import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.SUCCESS;
import static com.ulas.springcoretemplate.util.MethodUtils.*;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private final UserMapper userMapper;
    private final LoggingBean LOG;
    private final MethodUtils methodUtils;
    private final UrlEntityRepository urlEntityRepository;

    public static void checkCredentials(UserEntity user, String username) {
        if (user == null) throw new CustomException(USER_NOT_EXIST);

        if (Objects.equals(user.getEmail(), user.getPhoneNumber())) throw new CustomException(INVALID_OPERATION);
    }

    @Override
    public boolean newUserOperations(String name) {
        UserEntity userEntity = getActiveUser();

        checkCredentials(userEntity, name);

        if (userEntity.isPhoneNumberVerified()) {
            return userEntity.isRegisterCompleted();
        }
        userEntity = verifyPhoneNumber(userEntity);


        setAttribute(ACTIVE_USER, userEntity);
        return userEntity.isRegisterCompleted();
    }

    private UserEntity verifyPhoneNumber(UserEntity userEntity) {
        userEntity.setPhoneNumberVerified(true);
        userEntity.setStatus(Status.ACTIVE);
        String refCode = RandomStringUtils.randomAlphabetic(LENGTH_REF_CODE).toUpperCase(Locale.ENGLISH);

        while (userRepository.existsByMyRefCode(refCode)) {
            refCode = RandomStringUtils.randomAlphabetic(LENGTH_REF_CODE).toUpperCase(Locale.ENGLISH);
        }

        userEntity.setMyRefCode(refCode);
        userEntity.setProfileImageUrl(DEFAULT_PROFILE_IMAGE);
        userEntity = userRepository.save(userEntity);
        notificationService.createNotificationSettingsEntity(userEntity);
        return userEntity;
    }

    @Override
    public <Res> Res register(RegisterRequest registerRequest) {
        CodeKeeper codeKeeper = getAttribute(SMS_CODE);
        String phoneNumber = registerRequest.getGsmCountryCode() + registerRequest.getPhoneNumber();

        //Check last sent date
        if (codeKeeper == null) {
            codeKeeper = new CodeKeeper();
        } else if (codeKeeper.getLastSentDate().after(DateUtil.getCurrentDate())) {
            LOG.activity.info("{} user want to register but user's register request rate so high, ip address: {}",
                    phoneNumber, methodUtils.getClientIp());
            throw new CustomException(LOGIN_CODE_REQUESTS_EXCEEDED);
        }

        //Check credentials
        boolean isPhoneNumberExists = userRepository.existsByGsmCountryCodeAndPhoneNumber
                (registerRequest.getGsmCountryCode(), registerRequest.getPhoneNumber());

        if (isPhoneNumberExists) {
            LOG.activity.info("{} user want to register but user's phoneNumber is in use, ip address: {}",
                    registerRequest.getPhoneNumber(), methodUtils.getClientIp());
            throw new CustomException(PHONE_NUMBER_IN_USE);
        }

        //Generate code
        String code = String.valueOf(methodUtils.generateRandomNumber());
        String password = passwordEncoder.encode(code);

        //Save credentials to session
        codeKeeper.setEncodedCode(password);
        codeKeeper.setLastSentDate(DateUtil.addSecondsToTime(DateUtil.getCurrentDate(), 90));

        UserEntity newUser = userMapper.toEntity(registerRequest);

        newUser.setPhoneNumberVerified(false);
        newUser.setStatus(Status.PASSIVE);
        newUser.setUsername(phoneNumber);

        LOG.activity.info("A user wanted to register our platform by phoneNumber: {}, " +
                "name: {} ", newUser.getPhoneNumber(), phoneNumber);

        setAttribute(SMS_CODE, codeKeeper);
        setAttribute(ACTIVE_USER, newUser);

        //Send sms to user
        smsService.sendSmsToGsm(List.of(phoneNumber), code);
        return (Res) SUCCESS;
    }

    @Override
    public <Res> Res completeRegister(CompleteRegisterRequest completeRegisterRequest) {
        UserEntity activeUser = getActiveUser();
        if (activeUser.isRegisterCompleted()) throw new CustomException(USER_REGISTER_ALREADY_COMPLETED);
        boolean isEmailExists = userRepository.existsByEmail(completeRegisterRequest.getEmail());

        if (isEmailExists) {
            LOG.activity.info("{} user want to register but user's email is in use, gms: {}, ip address: {}",
                    completeRegisterRequest.getEmail(), activeUser.getPhoneNumber(), methodUtils.getClientIp());
            throw new CustomException(EMAIL_IN_USE);
        }

        activeUser = userMapper.toEntity(completeRegisterRequest, activeUser);
        if (userRepository.existsByMyRefCode(completeRegisterRequest.getRefCode())) {
            activeUser.setInvitedRefCode(completeRegisterRequest.getRefCode());
        }
        activeUser.setRegisterCompleted(true);
        MethodUtils.createNewUserUrl(activeUser, userRepository);
        SeoUtil.createAndSaveSeoUrlForUser(urlEntityRepository, activeUser.getUrl());

        userRepository.save(activeUser);
        setAttribute(ACTIVE_USER, activeUser);
        notificationService.assignNotificationSettings(activeUser);
        return (Res) SUCCESS;
    }
}
