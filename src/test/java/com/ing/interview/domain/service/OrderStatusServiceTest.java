package com.ing.interview.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.OrderStatusRestService;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.domain.dto.OrderStatus;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.utilsTest.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class OrderStatusServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findCarIfExistAndVerifyStatusWithOutError() throws JsonProcessingException {
        Long idCar=33L;
        Car car = Car.builder().id(idCar).orderDate(LocalDate.now()).model("PEUGEOT").color("BLUE").build();
        OrderStatus orderStatusExpected = OrderStatus.builder().assignedTo("Tomas").lastUpdate(LocalDateTimeUtil.getFormatTime(LocalDateTime.now())).stage("pending").build();

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

}