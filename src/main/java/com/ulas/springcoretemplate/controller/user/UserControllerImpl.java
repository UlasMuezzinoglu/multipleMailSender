package com.ulas.springcoretemplate.controller.user;

import com.ulas.springcoretemplate.interfaces.controller.user.UserController;
import com.ulas.springcoretemplate.interfaces.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;


    @Override
    public UserService getService() {
        return userService;
    }

    @Override
    public <Res> ResponseEntity<Res> getActiveUserProfile() {
        return ResponseEntity.ok(getService().getActiveUserProfile());
    }

    @Override
    public <Res> ResponseEntity<Res> refreshToken() {
        return ResponseEntity.ok(getService().refreshToken());
    }
}
