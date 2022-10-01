package com.ulas.springcoretemplate.auth;


import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.RedisMapName;
import com.ulas.springcoretemplate.exception.CustomException;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.model.entity.UserEntity;
import com.ulas.springcoretemplate.model.util.CodeKeeper;
import com.ulas.springcoretemplate.service.AttemptService;
import com.ulas.springcoretemplate.util.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ulas.springcoretemplate.constant.ErrorConstants.*;
import static com.ulas.springcoretemplate.constant.GeneralConstants.SMS_CODE;
import static com.ulas.springcoretemplate.util.MethodUtils.getActiveUser;
import static com.ulas.springcoretemplate.util.MethodUtils.getSession;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttemptService attemptService;
    @Autowired
    private LoggingBean LOG;
    @Autowired
    private MethodUtils methodUtils;

    /**
     * the action of the user trying to take action goes here
     *
     * @param username as String. for search user from database
     * @return given username to user as UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public final UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        if (attemptService.isBlocked(username, RedisMapName.LOGIN_ATTEMPT)) {
            try {
                getSession().invalidate();
            } catch (Exception e) {
            }
            LOG.service.error("User {} tried to login but they exceeded " +
                            "the login limit.",
                    username);
            throw new CustomException(MULTIPLE_LOGIN_FAILURE);
        }

        UserEntity attemptedUser = userRepository.findByUsername(username);

        if (attemptService.isFromPanelLoginPage(attemptedUser))
            return null;

        UserEntity activeUser = getActiveUser();

        if (activeUser == null) {
            LOG.service.error("User {} tried to login but the active " +
                            "user in the session is NULL",
                    username);
            throw new CustomException(USER_NOT_EXIST);
        }

        if (!activeUser.getUsername().equals(username)) {
            LOG.service.error("User {} tried to login but the active " +
                            "user in the session is someone else: {}.",
                    username,
                    activeUser.getPhoneNumber());
            throw new CustomException(USER_NOT_EXIST);
        }

        CodeKeeper codeKeeper = (CodeKeeper) getSession().getAttribute(SMS_CODE);
        if (codeKeeper == null) throw new CustomException(INVALID_ID);

        String encodedLoginCode = codeKeeper.getEncodedCode();

        List<SimpleGrantedAuthority> authorities = activeUser.getRole().getGrantedAuthorities();

        attemptService.incrementAttemptNumber(methodUtils.getClientIp(), RedisMapName.LOGIN_ATTEMPT, 1800);

        return new UserDetailsImp(
                authorities,
                activeUser.getUsername(),
                encodedLoginCode,
                true, true,
                true, true);
    }
}
