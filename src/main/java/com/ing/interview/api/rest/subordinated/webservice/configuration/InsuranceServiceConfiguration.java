package com.ing.interview.api.rest.subordinated.webservice.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.ing.interview.utils.ConstantsUtils.*;

@Component
@Getter
@Setter
@NoArgsConstructor
public class InsuranceServiceConfiguration {

    @Value ("${ing.service.insurance.host}")
    private String serviceHost;

    private String serviceName= ING_INSURANCE_SERVICE;

    public String getUrl(){ return this.getServiceHost() + PATH_SEPARATOR + this.getServiceName();}

    public String getUrlTimeout() {
        return this.getUrl() + PATH_SEPARATOR + ING_INSURANCE_SERVICE_PATH_TIMEOUT;
    }

}
