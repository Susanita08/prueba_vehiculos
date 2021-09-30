package com.ing.interview.validator.anotations;

import com.ing.interview.validator.CardCommandAgeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.ing.interview.utils.ConstantsUtils.MESSAGE_DEFAULT_VALIDATOR;

@Constraint(validatedBy = CardCommandAgeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeValidator {

    String message() default MESSAGE_DEFAULT_VALIDATOR;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
