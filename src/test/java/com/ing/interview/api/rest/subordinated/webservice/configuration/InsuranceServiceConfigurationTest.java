package com.ing.interview.api.rest.subordinated.webservice.configuration;

import org.junit.jupiter.api.Test;

import static com.ing.interview.utilsTest.ConstantsUtils.ING_INSURANCE_SERVICE;
import static com.ing.interview.utilsTest.ConstantsUtils.ING_INSURANCE_SERVICE_PATH_TIMEOUT;
import static org.junit.jupiter.api.Assertions.*;

class InsuranceServiceConfigurationTest {
    private static final String serviceHost = "localhost";
    private static final String servicePort = "8106";

    @Test
    void shouldReadUrlParametersAndReturnUrlGenerated() {
        InsuranceServiceConfiguration insuranceServiceConfiguration = new InsuranceServiceConfiguration();
        insuranceServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_INSURANCE_SERVICE);
        assertEquals(service.toString(), insuranceServiceConfiguration.getUrl());
    }

    @Test
    void shouldReadUrlParametersAndReturnUrlTimeOut() {
        InsuranceServiceConfiguration insuranceServiceConfiguration = new InsuranceServiceConfiguration();
        insuranceServiceConfiguration.setServiceHost(serviceHost+":"+servicePort);;
        StringBuilder service = new StringBuilder();
        service.append(serviceHost).append(":").append(servicePort).append("/").append(ING_INSURANCE_SERVICE).append("/").append(ING_INSURANCE_SERVICE_PATH_TIMEOUT);
        assertEquals(service.toString(), insuranceServiceConfiguration.getUrlTimeout());
    }
}