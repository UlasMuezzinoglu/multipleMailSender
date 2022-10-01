package com.ulas.springcoretemplate.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrDoubleValidator implements ConstraintValidator<CustomConstraint, Double>, BaseValidator {
    int max = 0;
    int min = 0;
    boolean nullable = false;
    String constraintErrorCode;
    String nullMessage;
    String regexErrorCode;

    public void initialize(CustomConstraint parameters) {
        max = parameters.max();
        min = parameters.min();
        nullable = parameters.nullable();
        constraintErrorCode = parameters.constraintErrorCode();
        nullMessage = parameters.nullMessage();
    }

    public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
        if (nullable && value == null) return true;
        if (!nullable && value == null) {
            setMessageForStatus(constraintValidatorContext, nullMessage);
            return false;
        }
        if (!(value >= min && value <= max)) {
            setMessageForStatus(constraintValidatorContext, constraintErrorCode);
            return false;
        }
        return true;
    }
}
