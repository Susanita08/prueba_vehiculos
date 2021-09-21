package com.ing.interview.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.objects.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.objects.JsonFullCarMessage;
import org.junit.jupiter.api.Test;

class CarServiceTest {

    @Test
    void givenCommandWhenCreateThenExpectedValues(){
        CarCommand carCommand = CarCommand.builder()
            .age(16)
            .color("PINK")
            .model("BMW")
            .build();

        CarRepository carRepository = mock(CarRepository.class);
        CarService sut = new CarService(carRepository, null);

        final Car result = sut.create(carCommand);

        assertEquals("PINK", result.getColor());
        assertEquals("BMW", result.getModel());
    }

    @Test
    void givenCarIfExistThenCreate(){
        CarCommand carCommand = CarCommand.builder()
                .age(16)
                .color("BLUE")
                .model("PEUGEOT")
                .build();

        CarAvailabilityRestConnector carAvailabilityRestConnector= mock(CarAvailabilityRestConnector.class);
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService= mock(CarAvailabilityService.class);

        when(carAvailabilityService.processStock(any(), any())).thenReturn(carAvailabilityRestConnector);
        when(carAvailabilityRestConnector.available(any(), any())).thenReturn(true);

        CarService carService = new CarService(carRepository, carAvailabilityService);

        final JsonFullCarMessage result = carService.createCarVerifyAvailability(carCommand);

        verify(carAvailabilityService).processStock(eq(carCommand.getModel()), eq(carCommand.getColor()));
        verify(carAvailabilityRestConnector).available(eq(carCommand.getModel()), eq(carCommand.getColor()));

        assertEquals("BLUE", result.getMessage().getCarCommand().getColor());
        assertEquals("PEUGEOT", result.getMessage().getCarCommand().getModel());
    }

    @Test
    void givenCarIfNotExistThenSetJsonFullCarMessageWithError(){
        CarCommand carCommand = CarCommand.builder()
                .age(16)
                .color("PINK")
                .model("FORD")
                .build();

        CarAvailabilityRestConnector carAvailabilityRestConnector= mock(CarAvailabilityRestConnector.class);
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService= mock(CarAvailabilityService.class);

        when(carAvailabilityService.processStock(any(), any())).thenReturn(carAvailabilityRestConnector);
        when(carAvailabilityRestConnector.available(any(), any())).thenReturn(false);

        CarService carService = new CarService(carRepository, carAvailabilityService);

        final JsonFullCarMessage result = carService.createCarVerifyAvailability(carCommand);

        verify(carAvailabilityService).processStock(eq(carCommand.getModel()), eq(carCommand.getColor()));
        verify(carAvailabilityRestConnector).available(eq(carCommand.getModel()), eq(carCommand.getColor()));

        assertEquals(ApplicationMessage.UNAVAILABLE.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNAVAILABLE.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNAVAILABLE.getStrCode(), result.getResponse().getStrCode());
    }

}