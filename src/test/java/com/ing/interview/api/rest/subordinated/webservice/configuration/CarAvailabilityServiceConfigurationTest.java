package com.ing.interview.api.rest.subordinated.webservice.configuration;

import org.junit.jupiter.api.Test;

import static com.ing.interview.utils.ConstantsUtils.ING_STOCK_SERVICE;
import static com.ing.interview.utils.ConstantsUtils.ING_STOCK_SERVICE_PATH_TIMEOUT;
import static org.junit.jupiter.api.Assertions.*;

class CarAvailabilityServiceConfigurationTest {
    private static final String serviceHost = "localhost";
    private static final String servicePort = "8102";

    @Test
    void shouldReadUrlParametersAndReturnUrlGenerated() {
        CarAvailabilityServiceConfiguration carAvailabilityServiceConfiguration = new CarAvailabilityServiceConfiguration();
        carAvailabilityServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_STOCK_SERVICE);
        assertEquals(service.toString(), carAvailabilityServiceConfiguration.getUrl());
    }

    @Test
    void shouldReadUrlParametersAndReturnUrlTimeOut() {
        CarAvailabilityServiceConfiguration carAvailabilityServiceConfiguration = new CarAvailabilityServiceConfiguration();
        carAvailabilityServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_STOCK_SERVICE).append("/").append(ING_STOCK_SERVICE_PATH_TIMEOUT);
        assertEquals(service.toString(), carAvailabilityServiceConfiguration.getUrlTimeout());
    }
}