package com.ing.interview.api.rest;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.domain.service.CarService;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.mother.JsonFullCarMessageMother;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarService carService;

    @Before
    void setUp(){
        carRepository=mock(CarRepository.class);
        carService=mock(CarService.class);
    }

    @Test
    void givenCarOnlyIfExists_ThenCreateThenReturnIsCreated() throws Exception {
        CarCommand carCommand = new CarCommand(0, "BLACK", "MERCEDES");
        JsonFullCarMessage jsonFullCarMessageExpected = JsonFullCarMessageMother.getMessageCarAvailableResponse(carCommand);

        CarAvailabilityRestConnector carAvailabilityRestConnector= new CarAvailabilityRestConnector();

        when(carService.createCarVerifyAvailability(eq(carCommand))).thenReturn(jsonFullCarMessageExpected);

        CarController carController = new CarController(carService);

        ResponseEntity<JsonFullCarMessage> jsonOutput = carController.createCarVerifyStock(carCommand);

       verify(carService).createCarVerifyAvailability(eq(carCommand));

        assertEquals(ApplicationMessage.CREATED.getCode(), jsonOutput.getBody().getResponse().getCode());
        assertEquals(ApplicationMessage.CREATED.getMessage(), jsonOutput.getBody().getResponse().getMessage());
        assertEquals(ApplicationMessage.CREATED.getStrCode(), jsonOutput.getBody().getResponse().getStrCode());

        assertEquals(objectMapper.writeValueAsString(jsonFullCarMessageExpected), objectMapper.writeValueAsString(jsonOutput.getBody()));
    }
}
