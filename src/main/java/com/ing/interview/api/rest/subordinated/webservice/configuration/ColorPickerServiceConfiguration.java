package com.ing.interview.api.rest.subordinated.webservice.configuration;

import lombok.AllArgsConstructor;
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
public class ColorPickerServiceConfiguration {

    @Value ("${ing.service.color.picker.host}")
    private String serviceHost;

    private String serviceName= ING_COLOR_PICKER_SERVICE;

    public String getUrl(){ return this.getServiceHost() + PATH_SEPARATOR + this.getServiceName();}

    public String getUrlTimeout() {
        return this.getUrl() + PATH_SEPARATOR + ING_COLOR_PICKER_SERVICE_PATH_TIMEOUT;
    }

}
