package com.ing.interview.api.rest.subordinated.webservice.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.ing.interview.utilsTest.ConstantsUtils.*;

@Component
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusRestServiceConfiguration {

    @Value ("${ing.service.order.conector.host}")
    private String serviceHost;

    private String serviceName= ING_ORDER_CONECTOR_SERVICE;

    public String getUrl(){ return this.getServiceHost() + PATH_SEPARATOR + this.getServiceName();}

    public String getUrlTimeout() {
        return this.getUrl() + PATH_SEPARATOR + ING_ORDER_CONECTOR_SERVICE_PATH_TIMEOUT;
    }

}
