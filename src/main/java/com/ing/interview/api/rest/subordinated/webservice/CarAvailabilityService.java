package com.ing.interview.api.rest.subordinated.webservice;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.CarAvailabilityServiceConfiguration;
import com.ing.interview.objects.CarCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CarAvailabilityService {

    private final WebClient webClient;
    private final CarAvailabilityServiceConfiguration stockMessageServiceConfiguration;

    @Autowired
    public CarAvailabilityService(WebClient webClient, CarAvailabilityServiceConfiguration stockMessageServiceConfiguration) {
        this.webClient = webClient;
        this.stockMessageServiceConfiguration = stockMessageServiceConfiguration;
    }

    public CarAvailabilityRestConnector processStock(String model, String color){
        CarCommand carCommand = CarCommand.builder().model(model.toUpperCase()).color(color.toUpperCase()).build();
        return webClient.post().uri(stockMessageServiceConfiguration.getUrl()).body(BodyInserters.fromValue(carCommand)).retrieve()
                .bodyToMono(CarAvailabilityRestConnector.class).block();
    }

    public CarAvailabilityRestConnector processTimeOut(String model, String color){
        CarCommand carCommand = CarCommand.builder().model(model.toUpperCase()).color(color.toUpperCase()).build();
        return webClient.post().uri(stockMessageServiceConfiguration.getUrlTimeout()).body(BodyInserters.fromValue(carCommand)).retrieve()
                .bodyToMono(CarAvailabilityRestConnector.class).block();
    }
}
