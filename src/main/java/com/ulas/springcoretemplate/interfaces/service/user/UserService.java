package com.ulas.springcoretemplate.interfaces.service.user;

public interface UserService {
    <Res> Res getActiveUserProfile();
    <Res> Res refreshToken();
}