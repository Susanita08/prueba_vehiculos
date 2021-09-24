package com.ing.interview.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.OrderStatusRestService;
import com.ing.interview.domain.dto.OrderStatus;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.*;
import com.ing.interview.utils.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class OrderStatusService {

    private final CarRepository carRepository;
    private final OrderStatusRestService orderStatusRestService;

    public JsonFullCarMessage findCarByOrderStatus(Long idCar) throws JsonProcessingException {
        OrderStatusRestConnector orderStatusRestConnector = orderStatusRestService.getOrderStatus(idCar);
        OrderStatus orderStatus = orderStatusRestConnector.checkOrderStatus(idCar);
        return ofNullable(carRepository.findById(idCar)).map(car->{
                    if(nonNull(orderStatus)){
                        OrderStatus orderStatusModificate = OrderStatus.builder().assignedTo(orderStatus.getAssignedTo()).stage(orderStatus.getStage()).lastUpdate(LocalDateTimeUtil.getFormatTime(orderStatus.getLastUpdate())).build();
                        return JsonFullCarMessage.builder().message(Message.builder()
                                .car(car).orderStatus(orderStatusModificate).build()).response(Response.builder().code(ApplicationMessage.CREATED.getCode())
                                .message(ApplicationMessage.CREATED.getMessage()).strCode(ApplicationMessage.CREATED.getStrCode())
                                .sourceService("OrderStatusService").build()).build();
                    }
                    return this.getBadResponse();
                })
                .orElse(this.getBadResponse());
    }

    public JsonFullCarMessage getBadResponse() {
        return JsonFullCarMessage.builder().response(JsonFullCarMessage.Response.builder().
                code(ApplicationMessage.UNEXPECTED.getCode()).message(ApplicationMessage.UNEXPECTED.getMessage())
                .strCode(ApplicationMessage.UNEXPECTED.getStrCode()).sourceService("OrderStatusService").build()).build();
    }
}
