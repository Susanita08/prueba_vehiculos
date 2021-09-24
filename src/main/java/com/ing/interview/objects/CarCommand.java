package com.ing.interview.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
public class CarCommand {
    @NonNull
    @Min(value = 18)
    @Max(value=80)
    @Digits(integer=2, fraction=0)
    private final Integer age;

    private final String color;

    @NonNull
    @NotBlank
    private final String model;

}
