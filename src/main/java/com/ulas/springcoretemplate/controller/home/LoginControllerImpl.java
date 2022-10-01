package com.ulas.springcoretemplate.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.home.LoginController;
import com.ulas.springcoretemplate.interfaces.service.home.LoginService;
import com.ulas.springcoretemplate.model.request.LoginCodeViaGsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginControllerImpl implements LoginController {

    @Autowired
    private LoginService loginService;

    @Override
    public LoginService getService() {
        return loginService;
    }

    @Override
    public <Res> ResponseEntity<Res> getLoginCodeViaGsm(LoginCodeViaGsm loginCodeViaGsm) {
        return ResponseEntity.ok(getService().getLoginCodeViaGsm(loginCodeViaGsm));
    }

    @Override
    public <Res> ResponseEntity<Res> logout() {
        return ResponseEntity.ok(getService().logout());
    }
}
