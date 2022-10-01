package com.ulas.springcoretemplate.interfaces.controller.user;

import com.ulas.springcoretemplate.interfaces.controller.common.BaseController;
import com.ulas.springcoretemplate.interfaces.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "UserController", value = "UserController")
public interface UserController extends BaseController<UserService> {

    @GetMapping("/user/profile")
    @ApiOperation("This service is used to get my all data")
    <Res> ResponseEntity<Res> getActiveUserProfile();

    @GetMapping("/user/refreshToken")
    @ApiOperation("This service is used to refresh user's token")
    <Res> ResponseEntity<Res> refreshToken();
}
