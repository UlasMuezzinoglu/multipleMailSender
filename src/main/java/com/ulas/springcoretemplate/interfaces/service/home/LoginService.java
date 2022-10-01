package com.ulas.springcoretemplate.interfaces.service.home;

import com.ulas.springcoretemplate.model.request.LoginCodeViaGsm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface LoginService {
    void checkLoginOrigin(HttpServletRequest request, Authentication authResult);

    <Res> Res getLoginCodeViaGsm(LoginCodeViaGsm loginCodeViaGsm);

    <Res> Res logout();
}
