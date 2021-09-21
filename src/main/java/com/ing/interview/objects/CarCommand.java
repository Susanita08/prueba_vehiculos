package com.ing.interview.objects;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CarCommand {
    @NonNull
    private final int age;

    private final String color;

    @NonNull
    @NotBlank
    private final String model;

}
