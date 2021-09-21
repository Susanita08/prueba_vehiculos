package com.ing.interview.api.rest.connectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
@AllArgsConstructor
public enum ColorPickerRestConnector {
    //MAP
    COLOR_ERRO(0, "ERROR", ""),
    PEUGEOT(1, "PEUGEOT", "BLUE"),
    FIAT(2, "FIAT", "YELLOW"),
    MERCEDES(3, "MERCEDES", "GREY");

    private static final long serialVersionUID = 1L;
    private final Integer id;
    private final String model;
    private final String color;

    public ColorPickerRestConnector pickColor(String model) {
        return ColorPickerRestConnector.fromModel(model);
    }

    public static ColorPickerRestConnector fromId(Integer id){
        return Arrays.stream(values()).filter(carAvailability -> Objects.equals(id, carAvailability.id)).findAny().orElse(null);
    }

    public static ColorPickerRestConnector fromModel(String model){
        return Arrays.stream(values()).filter(pickColor -> Objects.equals(model, pickColor.model)).findAny().orElse(null);
    }

}
