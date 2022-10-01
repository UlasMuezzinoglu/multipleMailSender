package com.ulas.springcoretemplate.interfaces.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.common.BaseController;
import com.ulas.springcoretemplate.interfaces.service.home.LoginService;
import com.ulas.springcoretemplate.model.request.LoginCodeViaGsm;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Api(value = "LoginController", tags = "LoginController")
public interface LoginController extends BaseController<LoginService> {

    @PostMapping("/home/login/gsm")
    <Res> ResponseEntity<Res> getLoginCodeViaGsm(@Valid @ModelAttribute LoginCodeViaGsm loginCodeViaGsm);

    @GetMapping("/user/logout")
    <Res> ResponseEntity<Res> logout();
}
