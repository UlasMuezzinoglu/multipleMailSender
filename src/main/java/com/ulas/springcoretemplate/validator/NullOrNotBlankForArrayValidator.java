package com.ulas.springcoretemplate.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class NullOrNotBlankForArrayValidator implements ConstraintValidator<CustomConstraint, Collection<String>> {
    int max = 0;
    int min = 0;
    boolean nullable = false;

    public void initialize(CustomConstraint parameters) {
        max = parameters.max();
        min = parameters.min();
        nullable = parameters.nullable();
    }

    public boolean isValid(Collection<String> values, ConstraintValidatorContext constraintValidatorContext) {
        for (String value : values) {
            if (nullable && value == null) continue;
            if (!nullable && value == null) return false;
            if (value.trim().length() < min && value.trim().length() > max)
                return false;
        }
        return true;
    }
}
