package com.ing.interview.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
public class CarCommand {
    @NonNull
    private final int age;

    private final String color;

    @NonNull
    @NotBlank
    private final String model;

}
