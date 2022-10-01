package com.ulas.springcoretemplate.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<CustomConstraint, String>, BaseValidator {
    int max = 0;
    int min = 0;
    boolean nullable = false;
    String regexErrorCode;
    String constraintErrorCode;
    String nullMessage;
    private String[] regexp;

    public void initialize(CustomConstraint parameters) {
        min = parameters.min() != 0 ? parameters.min() : 0;
        max = parameters.max() != 0 ? parameters.max() : Integer.MAX_VALUE;
        nullable = parameters.nullable();
        regexp = parameters.regexp();
        constraintErrorCode = parameters.constraintErrorCode();
        regexErrorCode = parameters.regexErrorCode();
        nullMessage = parameters.nullMessage();
    }

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        if (nullable && value == null) return true;
        if (!nullable && value == null) {
            setMessageForStatus(constraintValidatorContext, nullMessage);
            return false;
        }
        if (value.trim().length() < min || value.trim().length() > max) {
            setMessageForStatus(constraintValidatorContext, constraintErrorCode);
            return false;
        }
        for (String regex : regexp) {
            if (!java.util.regex.Pattern.matches(regex, value)) {
                setMessageForStatus(constraintValidatorContext, regexErrorCode);
                return false;
            }
        }
        return true;
    }
}

