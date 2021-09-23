package com.ing.interview.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.api.rest.connectors.InsuranceRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.api.rest.subordinated.webservice.ColorPickerService;
import com.ing.interview.api.rest.subordinated.webservice.InsuranceService;
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
        return Stream.of(Arguments.of(CarCommand.builder().age(18).color("BLUE").model("PEUGEOT").build(), "BLUE", "PEUGEOT"),
                Arguments.of(CarCommand.builder().age(20).model("FIAT").build(), "YELLOW", "FIAT"));
    }

    @Test
    void givenCommandWhenCreateThenExpectedValues() {
        CarCommand carCommand = CarCommand.builder()
                .age(16)
                .color("PINK")
                .model("BMW")
                .build();

        CarRepository carRepository = mock(CarRepository.class);
        CarService sut = new CarService(carRepository, null, null, null);

        final Car result = sut.create(carCommand);

        assertEquals("PINK", result.getColor());
        assertEquals("BMW", result.getModel());
    }

    @ParameterizedTest
    @MethodSource("carCommandsArgumentsWithOutErrors")
    void givenCarIfExistAndVerifyColorDefaultAndVerifyAgeThenCreate_withFullColorAndWitOutColor(CarCommand carCommand, String colorExpected, String modelExpected) {

        CarAvailabilityRestConnector carAvailabilityRestConnector = new CarAvailabilityRestConnector();
        ColorPickerRestConnector colorPickerRestConnector = mock(ColorPickerRestConnector.class);

        ColorPickerService colorPickerService = mock(ColorPickerService.class);
        InsuranceService insuranceService = mock(InsuranceService.class);
        InsuranceRestConnector insuranceRestConnector = new InsuranceRestConnector();

        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);

        boolean available = carAvailabilityRestConnector.available(carCommand.getColor(), carCommand.getModel());

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService, insuranceService);

        when(carAvailabilityService.processStock(eq(carCommand.getAge()) ,eq(carCommand.getModel()), any())).thenReturn(carAvailabilityRestConnector);
        when(colorPickerService.getColorDefault(eq(carCommand.getAge()), eq(carCommand.getModel()))).thenReturn(colorPickerRestConnector);
        when(colorPickerRestConnector.pickColor(eq(carCommand.getModel()))).thenReturn(Optional.of(colorExpected));
        when(insuranceService.getAllowedByModel(eq(carCommand.getAge()),eq(carCommand.getModel()))).thenReturn(insuranceRestConnector);

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(colorPickerService, times(2)).getColorDefault(any(), any());
        verify(carAvailabilityService).processStock(eq(carCommand.getAge()) ,eq(carCommand.getModel()), any());
        verify(insuranceService).getAllowedByModel(eq(carCommand.getAge()) ,eq(carCommand.getModel()));

        assertEquals(colorExpected, result.getMessage().getCar().getColor());
        assertEquals(modelExpected, result.getMessage().getCar().getModel());

    }

    @Test
    void givenCarIfNotExistThenSetJsonFullCarMessageWithErrorForNonExistentStock() {
        CarCommand carCommand = CarCommand.builder().age(50).color("PINK").model("MERCEDES").build();
        ColorPickerRestConnector colorPickerRestConnector = new ColorPickerRestConnector();
        String colorExpected = "color";
        InsuranceRestConnector insuranceRestConnector = new InsuranceRestConnector();

        CarAvailabilityRestConnector carAvailabilityRestConnector = mock(CarAvailabilityRestConnector.class);
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);
        ColorPickerService colorPickerService = mock(ColorPickerService.class);
        InsuranceService insuranceService = mock(InsuranceService.class);

        when(carAvailabilityService.processStock(any(), any(), any())).thenReturn(carAvailabilityRestConnector);
        when(carAvailabilityRestConnector.available(any(), any())).thenReturn(false);
        when(insuranceService.getAllowedByModel(any(), any())).thenReturn(insuranceRestConnector);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService, insuranceService);

        when(colorPickerService.getColorDefault(eq(carCommand.getAge()),eq(carCommand.getModel()))).thenReturn(colorPickerRestConnector);

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(carAvailabilityRestConnector).available(eq(carCommand.getModel()), eq(carCommand.getColor()));
        verify(colorPickerService, times(2)).getColorDefault(any(), any());

        assertEquals(ApplicationMessage.UNAVAILABLE.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNAVAILABLE.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNAVAILABLE.getStrCode(), result.getResponse().getStrCode());

    }

    @Test
    void givenCarWithErrorByCarCommandNull() {
        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);
        ColorPickerService colorPickerService = mock(ColorPickerService.class);
        InsuranceService insuranceService = mock(InsuranceService.class);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService, insuranceService);

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
        InsuranceService insuranceService = mock(InsuranceService.class);

        when(colorPickerService.getColorDefault(any(),any())).thenReturn(null);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService, insuranceService);

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(colorPickerService, times(1)).getColorDefault(any(), any());

        assertEquals(ApplicationMessage.UNEXPECTED.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNEXPECTED.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNEXPECTED.getStrCode(), result.getResponse().getStrCode());
    }

    @Test
    void givenCarWithErrorByInsuranceRestConectorNull() {
        //InsuranceService not response
        CarCommand carCommand = CarCommand.builder().age(18).color("BLUE").model("PEUGEOT").build();
        String colorExpected = "BLUE";

        CarRepository carRepository = mock(CarRepository.class);
        CarAvailabilityService carAvailabilityService = mock(CarAvailabilityService.class);
        ColorPickerService colorPickerService = mock(ColorPickerService.class);
        InsuranceService insuranceService = mock(InsuranceService.class);
        ColorPickerRestConnector colorPickerRestConnector = mock(ColorPickerRestConnector.class);

        when(colorPickerService.getColorDefault(any(),any())).thenReturn(colorPickerRestConnector);
        when(insuranceService.getAllowedByModel(any(), any())).thenReturn(null);

        CarService carService = new CarService(carRepository, carAvailabilityService, colorPickerService, insuranceService);

        final JsonFullCarMessage result = carService.createCarExtended(carCommand);

        verify(colorPickerService, times(1)).getColorDefault(any(), any());
        verify(insuranceService, times(1)).getAllowedByModel(any(), any());

        assertEquals(ApplicationMessage.UNEXPECTED.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNEXPECTED.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNEXPECTED.getStrCode(), result.getResponse().getStrCode());
    }

}

