package com.ulas.springcoretemplate.model.request;


import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import static com.ulas.springcoretemplate.constant.RegexConstants.PHONE_NUMBER;

@Getter
@Setter
public class LoginCodeViaGsm {
    @CustomConstraint(regexp = PHONE_NUMBER, constraintErrorCode = "0091", regexErrorCode = "0091", nullMessage = "0092")
    @ApiParam(required = true)
    private String phoneNumber;
}
