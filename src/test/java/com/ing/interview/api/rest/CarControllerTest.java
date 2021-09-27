package com.ing.interview.api.rest;

import com.ing.interview.domain.dto.Car;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CarService carService;

    @Before
    public void setUpEach(){
        carService=mock(CarService.class);
    }

    @Test
    void givenCarOnlyIfExistsAndFindColorAndHaveAllowedA_ThenCreateThenReturnIsCreated() throws Exception {
        CarCommand carCommand = new CarCommand(50, "BLACK", "MERCEDES");
        JsonFullCarMessage jsonFullCarMessageExpected = JsonFullCarMessageMother.getMessageCarAvailableResponse(carCommand, 0L, null,true);

        when(carService.createCarExtended(any())).thenReturn(jsonFullCarMessageExpected);

        CarController carController = new CarController(carService);

        ResponseEntity<Car> jsonOutput = carController.createCarExtended(carCommand);

        verify(carService).createCarExtended(any());

        assertEquals(HttpStatus.CREATED, jsonOutput.getStatusCode());

        assertEquals(jsonFullCarMessageExpected.getMessage().getCar(), jsonOutput.getBody());
    }
}
