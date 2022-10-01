package com.ulas.springcoretemplate.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.home.RegisterController;
import com.ulas.springcoretemplate.interfaces.service.home.RegisterService;
import com.ulas.springcoretemplate.model.request.CompleteRegisterRequest;
import com.ulas.springcoretemplate.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterControllerImpl implements RegisterController {
    private final RegisterService registerService;

    @Override
    public RegisterService getService() {
        return registerService;
    }

    @Override
    public <Res> ResponseEntity<Res> register(RegisterRequest registerRequest) {
        return ResponseEntity.ok(getService().register(registerRequest));
    }

    @Override
    public <Res> ResponseEntity<Res> completeRegister(CompleteRegisterRequest completeRegisterRequest) {
        return ResponseEntity.ok(getService().completeRegister(completeRegisterRequest));
    }
}
