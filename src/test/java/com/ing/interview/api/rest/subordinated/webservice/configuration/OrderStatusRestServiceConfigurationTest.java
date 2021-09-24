package com.ing.interview.api.rest.subordinated.webservice.configuration;

import org.junit.jupiter.api.Test;

import static com.ing.interview.utilsTest.ConstantsUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusRestServiceConfigurationTest {
    private static final String serviceHost = "localhost";
    private static final String servicePort = "8108";

    @Test
    void shouldReadUrlParametersAndReturnUrlGenerated() {
        OrderStatusRestServiceConfiguration orderStatusRestServiceConfiguration = new OrderStatusRestServiceConfiguration();
        orderStatusRestServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_ORDER_CONECTOR_SERVICE);
        assertEquals(service.toString(), orderStatusRestServiceConfiguration.getUrl());
    }

    @Test
    void shouldReadUrlParametersAndReturnUrlTimeOut() {
        OrderStatusRestServiceConfiguration orderStatusRestServiceConfiguration = new OrderStatusRestServiceConfiguration();
        orderStatusRestServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_ORDER_CONECTOR_SERVICE).append("/").append(ING_ORDER_CONECTOR_SERVICE_PATH_TIMEOUT);
        assertEquals(service.toString(), orderStatusRestServiceConfiguration.getUrlTimeout());
    }
}