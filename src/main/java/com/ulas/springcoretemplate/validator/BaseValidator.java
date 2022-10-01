package com.ulas.springcoretemplate.validator;

import javax.validation.ConstraintValidatorContext;

public interface BaseValidator {
    default void setMessageForStatus(ConstraintValidatorContext constraintValidatorContext, String statusMessage) {
        ConstraintValidatorContext.ConstraintViolationBuilder context =
                constraintValidatorContext.buildConstraintViolationWithTemplate(statusMessage);
        context.addConstraintViolation().buildConstraintViolationWithTemplate(statusMessage);
    }
}
