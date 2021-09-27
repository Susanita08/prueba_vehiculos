package com.ing.interview.api.rest;

import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.domain.service.OrderStatusService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderStatusService orderStatusService;

    @Before
    public void setUpEach(){
        orderStatusService=mock(OrderStatusService.class);
    }

    @Test
    void givenCarOnlyIfExistsOrderStatusThenReturnIsSuccessfully() throws Exception {
        CarCommand carCommand = new CarCommand(50, "BLACK", "MERCEDES");
        OrderStatusRestConnector orderStatusRestConnector = new OrderStatusRestConnector();
        JsonFullCarMessage jsonFullCarMessageExpected = JsonFullCarMessageMother.getMessageCarAvailableResponse(carCommand, 33L,orderStatusRestConnector.checkOrderStatus(33L), false);

        when(orderStatusService.findCarByOrderStatus(any())).thenReturn(jsonFullCarMessageExpected);

        OrderStatusController orderStatusController = new OrderStatusController(orderStatusService);

        ResponseEntity<Car> jsonOutput = orderStatusController.findCarByOrderStatus(33L);

        verify(orderStatusService).findCarByOrderStatus(any());

        assertEquals(HttpStatus.OK, Objects.requireNonNull(jsonOutput.getStatusCode()));

        assertEquals(jsonFullCarMessageExpected.getMessage().getCar(),
                jsonOutput.getBody());
    }
}