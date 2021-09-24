package com.ing.interview.api.rest.connectors;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class ColorPickerRestConnector {
    private static final Log log = LogFactory.getLog(ColorPickerRestConnector.class);
    private static final Map<String, String> MAP = new HashMap<>();

    static{
        MAP.put("PEUGEOT", "BLUE");
        MAP.put("FIAT", "YELLOW");
        MAP.put("MERCEDES", "GREY");
    };

    public Optional<String> pickColor(String model) {
        log.info("Color default of microservice ColorPickerService: "+MAP.get(model) + "by model: "+ model);
        return Optional.ofNullable(MAP.get(model));
    }

}
