package com.ing.interview.api.rest.subordinated.webservice.configuration;

import org.junit.jupiter.api.Test;

import static com.ing.interview.utilsTest.ConstantsUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPickerServiceConfigurationTest {
    private static final String serviceHost = "localhost";
    private static final String servicePort = "8104";

    @Test
    void shouldReadUrlParametersAndReturnUrlGenerated() {
        ColorPickerServiceConfiguration colorPickerServiceConfiguration = new ColorPickerServiceConfiguration();
        colorPickerServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_COLOR_PICKER_SERVICE);
        assertEquals(service.toString(), colorPickerServiceConfiguration.getUrl());
    }

    @Test
    void shouldReadUrlParametersAndReturnUrlTimeOut() {
        ColorPickerServiceConfiguration colorPickerServiceConfiguration = new ColorPickerServiceConfiguration();
        colorPickerServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_COLOR_PICKER_SERVICE).append("/").append(ING_COLOR_PICKER_SERVICE_PATH_TIMEOUT);
        assertEquals(service.toString(), colorPickerServiceConfiguration.getUrlTimeout());
    }
}