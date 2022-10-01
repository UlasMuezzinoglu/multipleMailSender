package com.ulas.springcoretemplate.model.request;


import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.ulas.springcoretemplate.constant.RegexConstants.BLACK_LIST;
import static com.ulas.springcoretemplate.constant.RegexConstants.PHONE_NUMBER;

@Getter
@Setter
public class RegisterRequest implements Serializable {
    @NotNull(message = "0178")
    private Boolean opportunityAndCampaigns;

    @AssertTrue(message = "0179")
    private Boolean kvkk;

    @CustomConstraint(regexp = PHONE_NUMBER, constraintErrorCode = "0091", regexErrorCode = "0091", nullMessage = "0092")
    @ApiParam(required = true)
    private String phoneNumber;

    @CustomConstraint(min = -1000, nullMessage = "0072")
    @ApiParam(required = true)
    private Double latitude;

    @CustomConstraint(min = -1000, nullMessage = "0074")
    @ApiParam(required = true)
    private Double longitude;

    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0080", nullMessage = "0081", regexErrorCode = "0079")
    @ApiParam(required = true)
    private String shortPhysicalAddress;

    @CustomConstraint(min = 1, max = 300, regexp = {BLACK_LIST}, constraintErrorCode = "0083", nullMessage = "0084", regexErrorCode = "0082")
    @ApiParam(required = true)
    private String longPhysicalAddress;

    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0064", nullMessage = "0063", regexErrorCode = "0062")
    @ApiParam(required = true)
    private String city;

    @CustomConstraint(min = 1, max = 100, regexp = {BLACK_LIST}, constraintErrorCode = "0070", nullMessage = "0069", regexErrorCode = "0068")
    @ApiParam(required = true)
    private String country;

    @CustomConstraint(min = 1, max = 4, regexp = {BLACK_LIST}, constraintErrorCode = "0227", nullMessage = "0228", regexErrorCode = "0229")
    @ApiParam(required = true)
    private String gsmCountryCode;
}
