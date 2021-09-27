package com.ing.interview.objects.mother;

import com.ing.interview.domain.dto.Car;
import com.ing.interview.domain.dto.OrderStatus;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.Message;
import com.ing.interview.objects.JsonFullCarMessage.Response;
import com.ing.interview.utils.LocalDateTimeUtil;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class JsonFullCarMessageMother {

    public static JsonFullCarMessage getMessageCarAvailableResponse(CarCommand carCommand, Long idCar, OrderStatus orderStatus,boolean created) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Message message = created ? Message.builder().car(Car.builder().id(idCar).model(carCommand.getModel()).color(carCommand.getColor()).orderDate(LocalDate.parse("2021-09-21")).build()).build()
                : Message.builder().car(Car.builder().id(idCar).model(carCommand.getModel()).color(carCommand.getColor()).orderDate(LocalDate.parse("2021-09-21")).build())
                .orderStatus(OrderStatus.builder().assignedTo(orderStatus.getAssignedTo()).lastUpdate(orderStatus.getLastUpdate()).stage(orderStatus.getStage()).build()).build();
        Response response = created ? Response.builder().code(ApplicationMessage.CREATED.getCode()).message(ApplicationMessage.CREATED.getMessage())
                .strCode(ApplicationMessage.CREATED.getStrCode()).build()
                : Response.builder().code(ApplicationMessage.SUCCESS.getCode()).message(ApplicationMessage.SUCCESS.getMessage()).strCode(ApplicationMessage.SUCCESS.getStrCode()).build();
        return JsonFullCarMessage.builder().message(message).response(response).build();

        //"{"response":{"code":0,"message":"Successfully","strCode":"00","sourceService":null},
        // "message":{"car":{"id":546,"color":"BLACK","model":"MERCEDES","orderDate":"2021-09-21","links":[]},
        // "orderStatus":{"assignedTo":"Sergi","lastUpdate":"2021-09-26 20:48","stage":"processing"}}}"

    }



}
