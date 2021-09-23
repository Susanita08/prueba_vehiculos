package com.ing.interview.objects.mother;

import com.ing.interview.domain.dto.Car;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.Message;
import com.ing.interview.objects.JsonFullCarMessage.Response;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class JsonFullCarMessageMother {

    public static JsonFullCarMessage getMessageCarAvailableResponse(CarCommand carCommand, Long idCar) {
        Message message = Message.builder().car(Car.builder().id(idCar).model(carCommand.getModel()).color(carCommand.getColor()).orderDate(LocalDate.now()).build()).build();
        Response response = Response.builder().code(ApplicationMessage.CREATED.getCode()).message(ApplicationMessage.CREATED.getMessage()).strCode(ApplicationMessage.CREATED.getStrCode()).sourceService("CarAvailabilityService").build();
        return JsonFullCarMessage.builder().message(message).response(response).build();
    }

}
