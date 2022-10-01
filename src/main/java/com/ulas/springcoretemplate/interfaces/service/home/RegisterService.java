package com.ulas.springcoretemplate.interfaces.service.home;

import com.ulas.springcoretemplate.model.request.CompleteRegisterRequest;
import com.ulas.springcoretemplate.model.request.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface RegisterService {
    boolean newUserOperations(String name);

    <Res> Res register(RegisterRequest registerRequest);

    <Res> Res completeRegister(CompleteRegisterRequest completeRegisterRequest);
}
