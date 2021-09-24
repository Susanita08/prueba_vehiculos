package com.ing.interview.api.rest.subordinated.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.OrderStatusRestServiceConfiguration;
import com.ing.interview.domain.dto.OrderStatus;
import com.ing.interview.utils.LocalDateTimeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusRestServiceTest {

    @Mock
    OrderStatusRestServiceConfiguration orderStatusRestServiceConfiguration;
    private static String host;
    private static ObjectMapper objectMapper;
    private WebClient webClientMock;

    @BeforeAll
    static void setUp() throws Exception {
        host = "localhost:8108/insuranceService";
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenProcessOrderStatusRestConectorWithOutErrors() throws JsonProcessingException {
        Long idCar=33L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdateString = lastUpdate.format(formatter);

        OrderStatus orderStatusExpected = OrderStatus.builder().assignedTo("Tomas").lastUpdate(LocalDateTime.parse(lastUpdateString, formatter)).stage("pending").build();

        String READER_JSON=objectMapper.writeValueAsString(orderStatusExpected);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final OrderStatusRestService tested = new OrderStatusRestService(webClientMock, orderStatusRestServiceConfiguration);

        OrderStatusRestConnector orderStatusRestConnectorOutput = tested.getOrderStatus(idCar);

        assertEquals(objectMapper.writeValueAsString(orderStatusExpected), objectMapper.writeValueAsString(orderStatusRestConnectorOutput.checkOrderStatus(idCar)));

    }

    @Test
    void whenProcessOrderStatusRestConectorWithErrorByTimeOut() throws JsonProcessingException {
        Long idCar=33L;
        OrderStatus orderStatusExpected = OrderStatus.builder().assignedTo("Tomas").lastUpdate(LocalDateTimeUtil.getFormatTime(LocalDateTime.now())).stage("pending").build();

        String READER_JSON=objectMapper.writeValueAsString(orderStatusExpected);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.BAD_REQUEST)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final OrderStatusRestService tested = new OrderStatusRestService(webClientMock, orderStatusRestServiceConfiguration);

        assertThrows(RuntimeException.class, ()->tested.processTimeOut(idCar));
    }

}