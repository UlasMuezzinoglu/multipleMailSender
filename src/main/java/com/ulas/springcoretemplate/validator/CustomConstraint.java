package com.ulas.springcoretemplate.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NullOrNotBlankValidator.class, NullOrIntegerValidator.class,
        NullOrLongValidator.class, NullOrNotBlankForArrayValidator.class, NullOrDoubleValidator.class})
public @interface CustomConstraint {
    String constraintErrorCode() default "0000";

    String regexErrorCode() default "0001";

    String nullMessage() default "0000";

    String message() default "0000";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max() default 10000000;

    int min() default 0;

    String[] regexp() default {};

    boolean nullable() default false;
}
