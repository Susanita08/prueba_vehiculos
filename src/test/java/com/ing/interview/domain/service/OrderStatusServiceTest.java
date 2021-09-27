package com.ing.interview.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.OrderStatusRestService;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.domain.dto.OrderStatus;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.utils.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class OrderStatusServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Stream<Arguments> orderStatusEvenAndOdd() {
        return Stream.of(Arguments.of(20L, OrderStatus.builder().assignedTo("Sergi").stage("processing").lastUpdate(LocalDateTimeUtil.getFormatTime(LocalDateTime.now())).build()),
                Arguments.of(33L, OrderStatus.builder().assignedTo("Tomas").lastUpdate(LocalDateTimeUtil.getFormatTime(LocalDateTime.now())).stage("pending").build()));
    }

    @ParameterizedTest
    @MethodSource("orderStatusEvenAndOdd")
    void findCarIfExistAndVerifyStatusWithOutError(Long idCar, OrderStatus orderStatusExpected) throws JsonProcessingException {
        Car car = Car.builder().id(idCar).orderDate(LocalDate.now()).model("PEUGEOT").color("BLUE").build();

        CarRepository carRepository = mock(CarRepository.class);
        OrderStatusRestService orderStatusRestService=mock(OrderStatusRestService.class);
        OrderStatusRestConnector orderStatusRestConnector = new OrderStatusRestConnector();

        when(carRepository.findById(idCar)).thenReturn(car);
        when(orderStatusRestService.getOrderStatus(idCar)).thenReturn(orderStatusRestConnector);


        OrderStatusService orderStatusService = new OrderStatusService(carRepository, orderStatusRestService);

        final JsonFullCarMessage result = orderStatusService.findCarByOrderStatus(idCar);

        verify(carRepository).findById(any());
        verify(orderStatusRestService).getOrderStatus(any());

        assertEquals(objectMapper.writeValueAsString(orderStatusExpected), objectMapper.writeValueAsString(result.getMessage().getOrderStatus()));

    }

    @Test
    void findCarWithError() throws JsonProcessingException {
        Long idCar=560L;
        OrderStatus orderStatusExpected=null;
        CarRepository carRepository = mock(CarRepository.class);
        OrderStatusRestService orderStatusRestService=mock(OrderStatusRestService.class);
        OrderStatusRestConnector orderStatusRestConnector = mock(OrderStatusRestConnector.class);

        when(orderStatusRestService.getOrderStatus(idCar)).thenReturn(orderStatusRestConnector);
        when(orderStatusRestConnector.checkOrderStatus(idCar)).thenReturn(null);


        OrderStatusService orderStatusService = new OrderStatusService(carRepository, orderStatusRestService);
        final JsonFullCarMessage result = orderStatusService.findCarByOrderStatus(idCar);

        assertEquals(ApplicationMessage.UNEXPECTED.getCode(), result.getResponse().getCode());
        assertEquals(ApplicationMessage.UNEXPECTED.getMessage(), result.getResponse().getMessage());
        assertEquals(ApplicationMessage.UNEXPECTED.getStrCode(), result.getResponse().getStrCode());
    }

}