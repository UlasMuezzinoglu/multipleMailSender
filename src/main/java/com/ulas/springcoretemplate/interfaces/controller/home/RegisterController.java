package com.ulas.springcoretemplate.interfaces.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.common.BaseController;
import com.ulas.springcoretemplate.interfaces.service.home.RegisterService;
import com.ulas.springcoretemplate.model.request.CompleteRegisterRequest;
import com.ulas.springcoretemplate.model.request.RegisterRequest;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Api(value = "RegisterController", tags = "RegisterController")
public interface RegisterController extends BaseController<RegisterService> {

    @PostMapping("/home/register")
    <Res> ResponseEntity<Res> register(@Valid @ModelAttribute RegisterRequest registerRequest);

    @PostMapping("/user/register/complete")
    <Res> ResponseEntity<Res> completeRegister(@Valid @ModelAttribute CompleteRegisterRequest completeRegisterRequest);
}
