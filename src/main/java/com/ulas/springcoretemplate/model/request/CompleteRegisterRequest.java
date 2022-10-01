package com.ulas.springcoretemplate.model.request;


import com.ulas.springcoretemplate.enums.Gender;
import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ulas.springcoretemplate.constant.RegexConstants.BLACK_LIST;

@Getter
@Setter
public class CompleteRegisterRequest {
    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0130", regexErrorCode = "0129", nullMessage = "0128")
    @ApiParam(required = true)
    private String firstName;

    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0130", regexErrorCode = "0129", nullMessage = "0128")
    @ApiParam(required = true)
    private String lastName;

    @Email(message = "0094")
    @NotBlank(message = "0093")
    private String email;

    @NotNull(message = "0118")
    private Gender gender;

    @NotNull(message = "0131")
    private Date dateOfBirth;

    @CustomConstraint(nullable = true, min = 6, max = 6, regexp = {BLACK_LIST}, constraintErrorCode = "0132", regexErrorCode = "0133")
    private String refCode;
}
