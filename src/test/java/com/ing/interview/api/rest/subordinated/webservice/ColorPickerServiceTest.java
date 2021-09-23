package com.ing.interview.api.rest.subordinated.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.CarAvailabilityServiceConfiguration;
import com.ing.interview.api.rest.subordinated.webservice.configuration.ColorPickerServiceConfiguration;
import com.ing.interview.objects.CarCommand;
import javafx.scene.control.ColorPicker;
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
class ColorPickerServiceTest {
    @Mock
    ColorPickerServiceConfiguration colorPickerServiceConfiguration;
    private static ObjectMapper objectMapper;
    private static String host;
    private WebClient webClientMock;

    @BeforeAll
    static void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        host = "localhost:8104/colorPickerService";
    }

    public static Stream<Arguments> colorPickerArguments() {
        return Stream.of(Arguments.of("FIAT"),
                Arguments.of("MERCEDES"),
                Arguments.of("PEUGEOT"));
    }

    @ParameterizedTest
    @MethodSource("colorPickerArguments")
    void whenProcessColorPickerDefault(String model) throws JsonProcessingException {
        ColorPickerRestConnector colorPickerRestConnectorExpected = new ColorPickerRestConnector();

        when(colorPickerServiceConfiguration.getUrl()).thenReturn(host);

        CarCommand carCommandBody = new CarCommand(0,null, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.CREATED)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final ColorPickerService tested = new ColorPickerService(webClientMock, colorPickerServiceConfiguration);

        ColorPickerRestConnector colorPickerRestConnectorOutput = tested.getColorDefault(model);

        assertEquals(colorPickerRestConnectorExpected.pickColor(model), colorPickerRestConnectorOutput.pickColor(model));

    }

    @ParameterizedTest
    @MethodSource("colorPickerArguments")
    void whenProcessColorPickerDefaultWithErrors(String model) throws JsonProcessingException {
        ColorPickerRestConnector colorPickerRestConnectorExpected = new ColorPickerRestConnector();
        host += "/Timeout";

        CarCommand carCommandBody = new CarCommand(0,null, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.BAD_REQUEST)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final ColorPickerService tested = new ColorPickerService(webClientMock, colorPickerServiceConfiguration);

        assertThrows(RuntimeException.class, ()->tested.processTimeOut(model));
    }
}