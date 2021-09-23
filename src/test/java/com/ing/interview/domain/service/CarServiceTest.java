package com.ing.interview.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.api.rest.subordinated.webservice.ColorPickerService;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.objects.JsonFullCarMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

class CarServiceTest {

    public static Stream<Arguments> carCommandsArgumentsWithOutErrors() {
        return Stream.of(Arguments.of(CarCommand.builder().age(16).color("BLUE").model("PEUGEOT").build(), "BLUE", "PEUGEOT"),
                Arguments.of(CarCommand.builder().age(0).model("FIAT").build(), "YELLOW", "FIAT"));
    }

    @Test
    void givenCommandWhenCreateThenExpectedValues() {
        CarCommand carCommand = CarCommand.builder()
                .age(16)
                .color("PINK")
                .model("BMW")
                .build();

        CarRepository carRepository = mock(CarRepository.class);
        CarService sut = new CarService(carRepository, null, null);

        final Car result = sut.create(carCommand);

        assertEquals("PINK", result.getColor());
        assertEquals("BMW", result.getModel());
    }

    @ParameterizedTest
    @MethodSource("carCommandsArgumentsWithOutErrors")
    void givenCarIfExistAndVerifyColorDefaultThenCreate_withFullColorAndWitOutColor(CarCommand carCommand, String colorExpected, String modelExpected) {

        CarAvailabilityRestConnector carAvailabilityRestConnector = new CarAvailabilityRestConnector();
        ColorPickerRestConnector colorPickerRestConnector = mock(ColorPickerRestConnector.class);

        ColorPickerService colorPickerService = mock(ColorPickerService.class);
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);

        boolean available = carAvailabilityRestConnector.available(carCommand.getColor(), carCommand.getModel());

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService);

        when(carAvailabilityService.processStock(eq(carCommand.getModel()), any())).thenReturn(carAvailabilityRestConnector);
        when(colorPickerService.getColorDefault(eq(carCommand.getModel()))).thenReturn(colorPickerRestConnector);
        when(colorPickerRestConnector.pickColor(eq(carCommand.getModel()))).thenReturn(Optional.of(colorExpected));

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(colorPickerService, times(2)).getColorDefault(any());
        verify(carAvailabilityService).processStock(eq(carCommand.getModel()), any());

        assertEquals(colorExpected, result.getMessage().getCar().getColor());
        assertEquals(modelExpected, result.getMessage().getCar().getModel());

    }

    @Test
    void givenCarIfNotExistThenSetJsonFullCarMessageWithError() {
        CarCommand carCommand = CarCommand.builder().age(16).color("PINK").model("FORD").build();
        ColorPickerRestConnector colorPickerRestConnector = new ColorPickerRestConnector();
        String colorExpected = "color";

        CarAvailabilityRestConnector carAvailabilityRestConnector = mock(CarAvailabilityRestConnector.class);
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);
        ColorPickerService colorPickerService = mock(ColorPickerService.class);

        when(carAvailabilityService.processStock(any(), any())).thenReturn(carAvailabilityRestConnector);
        when(carAvailabilityRestConnector.available(any(), any())).thenReturn(false);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService);

        when(colorPickerService.getColorDefault(eq(carCommand.getModel()))).thenReturn(colorPickerRestConnector);

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(carAvailabilityService).processStock(eq(carCommand.getModel()), any());
        verify(carAvailabilityRestConnector).available(eq(carCommand.getModel()), eq(carCommand.getColor()));
        verify(colorPickerService, times(2)).getColorDefault(any());

        assertEquals(ApplicationMessage.UNAVAILABLE.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNAVAILABLE.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNAVAILABLE.getStrCode(), result.getResponse().getStrCode());

    }

    @Test
    void givenCarWithErrorByCarCommandNull() {
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);
        ColorPickerService colorPickerService = mock(ColorPickerService.class);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService);

        final JsonFullCarMessage result = carService.createCarExtended(null);

        assertEquals(ApplicationMessage.UNEXPECTED.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNEXPECTED.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNEXPECTED.getStrCode(), result.getResponse().getStrCode());
    }


    @Test
    void givenCarWithErrorByColorPickerRestConectorNull() {
        //WithOut color in carCommand and colorPickerService not response
        CarCommand carCommand = CarCommand.builder().age(16).model("CHEVROLET").build();
        String colorExpected = "color";

        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);
        ColorPickerService colorPickerService = mock(ColorPickerService.class);

        when(colorPickerService.getColorDefault(any())).thenReturn(null);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService);

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(colorPickerService, times(1)).getColorDefault(any());

        assertEquals(ApplicationMessage.UNEXPECTED.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNEXPECTED.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNEXPECTED.getStrCode(), result.getResponse().getStrCode());
    }

}

