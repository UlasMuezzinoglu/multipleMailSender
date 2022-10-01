package com.ulas.springcoretemplate.model.request;

import com.ulas.springcoretemplate.validator.CustomConstraint;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import static com.ulas.springcoretemplate.constant.RegexConstants.NOTIFICATION_ID;

@Getter
@Setter
public class NotificationIdSaveRequest {

    @CustomConstraint(regexErrorCode = "0177", regexp = {NOTIFICATION_ID}, constraintErrorCode = "0177", nullMessage = "0176")
    @ApiParam(required = true)
    private String notificationId;

}