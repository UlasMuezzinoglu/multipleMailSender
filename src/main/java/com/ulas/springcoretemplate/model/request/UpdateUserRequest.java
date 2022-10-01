package com.ulas.springcoretemplate.model.request;


import com.ulas.springcoretemplate.enums.Gender;
import com.ulas.springcoretemplate.model.common.ByIdRequest;
import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.ulas.springcoretemplate.constant.RegexConstants.BLACK_LIST;
import static com.ulas.springcoretemplate.constant.RegexConstants.PHONE_NUMBER;

@Getter
@Setter
public class UpdateUserRequest extends ByIdRequest {
    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0130", regexErrorCode = "0129", nullMessage = "0128")
    @ApiParam(required = true)
    private String firstName;

    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0130", regexErrorCode = "0129", nullMessage = "0128")
    @ApiParam(required = true)
    private String lastName;

    @CustomConstraint(regexp = PHONE_NUMBER, constraintErrorCode = "0091", regexErrorCode = "0091", nullMessage = "0092")
    @ApiParam(required = true)
    private String phoneNumber;

    @Email(message = "0094")
    @NotBlank(message = "0093")
    private String email;

    @CustomConstraint(min = 11, max = 11, regexp = {BLACK_LIST}, nullMessage = "0110", constraintErrorCode = "0110", regexErrorCode = "0109")
    private String identityNumber;

    @NotNull(message = "0118")
    @ApiParam(required = true)
    private Gender gender;
}

