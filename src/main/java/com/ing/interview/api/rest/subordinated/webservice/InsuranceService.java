package com.ing.interview.api.rest.subordinated.webservice;

import com.ing.interview.api.rest.connectors.InsuranceRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.InsuranceServiceConfiguration;
import com.ing.interview.objects.CarCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class InsuranceService {

    private final WebClient webClient;
    private final InsuranceServiceConfiguration insuranceServiceConfiguration;

    @Autowired
    public InsuranceService(WebClient webClient, InsuranceServiceConfiguration insuranceServiceConfiguration) {
        this.webClient = webClient;
        this.insuranceServiceConfiguration = insuranceServiceConfiguration;
    }

    public InsuranceRestConnector getAllowedByModel(Integer age, String model){
        CarCommand carCommand = CarCommand.builder().age(age).model(model.toUpperCase()).build();
        return webClient.post().uri(insuranceServiceConfiguration.getUrl()).body(BodyInserters.fromValue(carCommand)).retrieve()
                .bodyToMono(InsuranceRestConnector.class).block();
    }

    public InsuranceRestConnector processTimeOut(Integer age, String model){
        CarCommand carCommand = CarCommand.builder().age(age).model(model.toUpperCase()).build();
        return webClient.post().uri(insuranceServiceConfiguration.getUrlTimeout()).body(BodyInserters.fromValue(carCommand)).retrieve()
                .bodyToMono(InsuranceRestConnector.class).block();
    }
}
