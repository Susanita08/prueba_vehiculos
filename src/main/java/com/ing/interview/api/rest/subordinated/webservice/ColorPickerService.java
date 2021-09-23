package com.ing.interview.api.rest.subordinated.webservice;

import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.ColorPickerServiceConfiguration;
import com.ing.interview.objects.CarCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ColorPickerService {

    private final WebClient webClient;
    private final ColorPickerServiceConfiguration colorPickerServiceConfiguration;

    @Autowired
    public ColorPickerService(WebClient webClient, ColorPickerServiceConfiguration colorPickerServiceConfiguration) {
        this.webClient = webClient;
        this.colorPickerServiceConfiguration = colorPickerServiceConfiguration;
    }

    public ColorPickerRestConnector getColorDefault(String model){
        CarCommand carCommand = CarCommand.builder().model(model.toUpperCase()).build();
        return webClient.post().uri(colorPickerServiceConfiguration.getUrl()).body(BodyInserters.fromValue(carCommand)).retrieve()
                .bodyToMono(ColorPickerRestConnector.class).block();
    }

    public ColorPickerRestConnector processTimeOut(String model){
        CarCommand carCommand = CarCommand.builder().model(model.toUpperCase()).build();
        return webClient.post().uri(colorPickerServiceConfiguration.getUrlTimeout()).body(BodyInserters.fromValue(carCommand)).retrieve()
                .bodyToMono(ColorPickerRestConnector.class).block();
    }
}
