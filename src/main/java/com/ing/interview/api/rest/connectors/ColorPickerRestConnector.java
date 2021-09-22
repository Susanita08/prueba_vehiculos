package com.ing.interview.api.rest.connectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ColorPickerRestConnector {

    private static final Map<String, String> MAP = new HashMap<>();

    static{
        MAP.put("PEUGEOT", "BLUE");
        MAP.put("FIAT", "YELLOW");
        MAP.put("MERCEDES", "GREY");
    };

    public Optional<String> pickColor(String model) {
        return Optional.ofNullable(MAP.get(model));
    }

}
