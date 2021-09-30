package com.ing.interview.validator;

import com.ing.interview.validator.anotations.AgeValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CardCommandAgeValidator implements ConstraintValidator<AgeValidator, Integer> {

    private static final int MAX = 80;
    private static final int MIN = 18;

    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        return age > MIN && age < MAX;
    }

}

