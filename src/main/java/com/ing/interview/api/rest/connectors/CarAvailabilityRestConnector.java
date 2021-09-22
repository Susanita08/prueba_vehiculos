package com.ing.interview.api.rest.connectors;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CarAvailabilityRestConnector {

    private static final Set<String> STOCK= new HashSet<>();

    static{
        STOCK.add("PEUGEOT:BLUE");
        STOCK.add("FIAT:YELLOW");
        STOCK.add("MERCEDES:BLACK");
        STOCK.add("ERROR");
    }

    public boolean available(String color, String model) {
        String colorModel = color + ":" + model;
        return STOCK.contains(colorModel);
    }

}
