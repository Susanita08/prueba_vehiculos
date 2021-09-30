package com.ing.interview.objects;

import com.ing.interview.validator.anotations.AgeValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.*;

@Builder
@Getter
@AllArgsConstructor
public class CarCommand {

    @NonNull
    @AgeValidator
    @Digits(integer=2, fraction=0)
    private final Integer age;

    private final String color;

    @NonNull
    @NotBlank
    private final String model;

}
