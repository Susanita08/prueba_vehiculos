package com.ing.interview.api.rest.connectors;

import java.util.HashMap;
import java.util.Map;

public class InsuranceRestConnector {
    private static final Map<String, Integer> KYC= new HashMap<>();

    static{
        KYC.put("PEUGEOT", 18);
        KYC.put("FIAT", 20);
        KYC.put("MERCEDES", 50);
    }

    public boolean isEligible(int age, String model) {
        return KYC.get(model) <= age;
    }

}
