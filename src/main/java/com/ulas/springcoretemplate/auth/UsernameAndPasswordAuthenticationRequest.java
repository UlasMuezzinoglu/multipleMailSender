package com.ulas.springcoretemplate.auth;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsernameAndPasswordAuthenticationRequest {

    private String phoneNumber;
    private String password;
    private boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }

}
