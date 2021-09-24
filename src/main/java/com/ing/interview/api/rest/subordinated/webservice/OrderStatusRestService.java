package com.ing.interview.api.rest.subordinated.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.OrderStatusRestServiceConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.domain.dto.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OrderStatusRestService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final OrderStatusRestServiceConfiguration orderStatusRestServiceConfiguration;

    @Autowired
    public OrderStatusRestService(WebClient webClient, OrderStatusRestServiceConfiguration orderStatusRestServiceConfiguration) {
        this.webClient = webClient;
        this.orderStatusRestServiceConfiguration = orderStatusRestServiceConfiguration;
        objectMapper = new ObjectMapper();
    }

    public OrderStatusRestConnector getOrderStatus(Long idCar) throws JsonProcessingException {
        OrderStatus orderStatus = new OrderStatusRestConnector().checkOrderStatus(idCar);
        return webClient.post().uri(orderStatusRestServiceConfiguration.getUrl()).body(BodyInserters.fromValue(objectMapper.writeValueAsString(orderStatus))).retrieve()
                .bodyToMono(OrderStatusRestConnector.class).block();
    }

    public OrderStatusRestConnector processTimeOut(Long idCar) throws JsonProcessingException {
        OrderStatus orderStatus= new OrderStatusRestConnector().checkOrderStatus(idCar);
        return webClient.post().uri(orderStatusRestServiceConfiguration.getUrlTimeout()).body(BodyInserters.fromValue(objectMapper.writeValueAsString(orderStatus))).retrieve()
                .bodyToMono(OrderStatusRestConnector.class).block();
    }
}
