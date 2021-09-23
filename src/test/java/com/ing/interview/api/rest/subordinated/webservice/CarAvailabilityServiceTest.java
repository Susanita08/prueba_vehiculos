package com.ing.interview.api.rest.subordinated.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.CarAvailabilityServiceConfiguration;
import com.ing.interview.objects.CarCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarAvailabilityServiceTest {

    @Mock
    CarAvailabilityServiceConfiguration carAvailabilityServiceConfiguration;
    private static ObjectMapper objectMapper;
    private static String host;
    private WebClient webClientMock;

    @BeforeAll
    static void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        host = "localhost:8102/stockService";
    }

    public static Stream<Arguments> carAvailabilityArgumentsWithStock() {
        return Stream.of(Arguments.of("FIAT", "YELLOW"),
                Arguments.of("MERCEDES", "GREY"),
                Arguments.of("PEUGEOT", "BLUE"));
    }

    public static Stream<Arguments> carAvailabilityArgumentsWithOutStock() {
        return Stream.of(Arguments.of("FORD", "ROJO RUBY"),
                Arguments.of("VOLKSWAGEN", "NEGRO"));
    }

    @ParameterizedTest
    @MethodSource("carAvailabilityArgumentsWithStock")
    void whenProcessCarIfStockExistThenReturnAvailability(String model, String color) throws JsonProcessingException {
        Integer age=18;
        CarAvailabilityRestConnector carAvailabilityRestConnectorExpected = new CarAvailabilityRestConnector();

        StringBuilder builderCar= new StringBuilder();
        builderCar.append(model.toUpperCase()).append(":").append(color.toUpperCase());

        when(carAvailabilityServiceConfiguration.getUrl()).thenReturn(host);

        CarCommand carCommandBody = new CarCommand(age,color, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.CREATED)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final CarAvailabilityService tested = new CarAvailabilityService(webClientMock, carAvailabilityServiceConfiguration);

        CarAvailabilityRestConnector carAvailabilityRestConnectorOutput = tested.processStock(age, model, color);

        assertEquals(carAvailabilityRestConnectorExpected.available(color,model), carAvailabilityRestConnectorOutput.available(color, model));

    }

    @ParameterizedTest
    @MethodSource("carAvailabilityArgumentsWithOutStock")
    void whenProcessCarIfStockNotExistThenReturnFalse(String model, String color) throws JsonProcessingException {
        Integer age = 18;
        StringBuilder builderCar= new StringBuilder();
        builderCar.append(model.toUpperCase()).append(":").append(color.toUpperCase());

        when(carAvailabilityServiceConfiguration.getUrl()).thenReturn(host);

        CarCommand carCommandBody = new CarCommand(age,color, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final CarAvailabilityService tested = new CarAvailabilityService(webClientMock, carAvailabilityServiceConfiguration);

        CarAvailabilityRestConnector carAvailabilityRestConnectorOutput = tested.processStock(age, model, color);

        assertFalse(carAvailabilityRestConnectorOutput.available(color, model));

    }

    @ParameterizedTest
    @MethodSource("carAvailabilityArgumentsWithOutStock")
    void whenProcessCarWithErrors(String model, String color) throws JsonProcessingException {
        CarAvailabilityRestConnector carAvailabilityRestConnectorExpected = new CarAvailabilityRestConnector();
        host = "localhost:8102/stockService/Timeout";

        StringBuilder builderCar= new StringBuilder();
        builderCar.append(model.toUpperCase()).append(":").append(color.toUpperCase());

        CarCommand carCommandBody = new CarCommand(18,color, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.BAD_REQUEST)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final CarAvailabilityService tested = new CarAvailabilityService(webClientMock, carAvailabilityServiceConfiguration);

        assertThrows(RuntimeException.class, ()->tested.processTimeOut(model, color));

    }

}
