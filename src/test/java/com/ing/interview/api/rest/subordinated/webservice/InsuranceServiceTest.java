package com.ing.interview.api.rest.subordinated.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.api.rest.connectors.InsuranceRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.configuration.InsuranceServiceConfiguration;
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
class InsuranceServiceTest {

    @Mock
    InsuranceServiceConfiguration insuranceServiceConfiguration;
    private static ObjectMapper objectMapper;
    private static String host;
    private WebClient webClientMock;

    @BeforeAll
    static void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        host = "localhost:8106/insuranceService";
    }

    public static Stream<Arguments> insuranceArguments() {
        return Stream.of(Arguments.of("FIAT", 20),
                Arguments.of("MERCEDES", 50),
                Arguments.of("PEUGEOT", 18));
    }

    @ParameterizedTest
    @MethodSource("insuranceArguments")
    void whenProcessInsuranceAgeAllowedWithOutErrors(String model, Integer allowedAge) throws JsonProcessingException {
        InsuranceRestConnector insuranceRestConnectorExpected = new InsuranceRestConnector();
        when(insuranceServiceConfiguration.getUrl()).thenReturn(host);

        CarCommand carCommandBody = new CarCommand(allowedAge,null, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final InsuranceService tested = new InsuranceService(webClientMock, insuranceServiceConfiguration);

        InsuranceRestConnector insuranceRestConnectorOutput = tested.getAllowedByModel(allowedAge, model);

        assertEquals(insuranceRestConnectorExpected.isEligible(allowedAge, model), insuranceRestConnectorOutput.isEligible(allowedAge, model));

    }

    @ParameterizedTest
    @MethodSource("insuranceArguments")
    void whenProcessInsuranceAgeAllowedWithErrorsTimeOut(String model, Integer allowedAge) throws JsonProcessingException {
        InsuranceRestConnector insuranceRestConnectorExpected = new InsuranceRestConnector();

        CarCommand carCommandBody = new CarCommand(allowedAge,null, model);
        String READER_JSON=objectMapper.writeValueAsString(carCommandBody);

        webClientMock= WebClient.builder().baseUrl(host)
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.BAD_REQUEST)
                        .header("content-type", "application/json")
                        .body(READER_JSON)
                        .build()))
                .build();

        final InsuranceService tested = new InsuranceService(webClientMock, insuranceServiceConfiguration);

        assertThrows(RuntimeException.class, ()->tested.processTimeOut(allowedAge, model));
    }
}