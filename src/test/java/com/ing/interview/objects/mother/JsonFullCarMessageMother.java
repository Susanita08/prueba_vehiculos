package com.ing.interview.objects.mother;

import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.Message;
import com.ing.interview.objects.JsonFullCarMessage.Response;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonFullCarMessageMother {

    public static JsonFullCarMessage getMessageCarAvailableResponse(CarCommand carCommand) {
        Message message = Message.builder().carCommand(carCommand).build();
        Response response = Response.builder().code(ApplicationMessage.CREATED.getCode()).message(ApplicationMessage.CREATED.getMessage()).strCode(ApplicationMessage.CREATED.getStrCode()).sourceService("CarAvailabilityService").build();
        return JsonFullCarMessage.builder().message(message).response(response).build();
    }

}
