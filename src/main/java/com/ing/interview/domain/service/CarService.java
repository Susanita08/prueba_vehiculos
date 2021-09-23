package com.ing.interview.domain.service;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.api.rest.subordinated.webservice.ColorPickerService;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.domain.repository.CarRepository;

import java.time.LocalDate;

import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.Message;
import com.ing.interview.objects.JsonFullCarMessage.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarAvailabilityService carAvailabilityService;
    private final ColorPickerService colorPickerService;

    @Autowired
    public CarService(CarRepository carRepository, CarAvailabilityService carAvailabilityService, ColorPickerService colorPickerService) {
        this.carRepository = carRepository;
        this.carAvailabilityService = carAvailabilityService;
        this.colorPickerService = colorPickerService;
    }

    public Car create(CarCommand carCommand) {
        final Car car = Car.builder()
                .color(carCommand.getColor())
                .model(carCommand.getModel())
                .orderDate(LocalDate.now())
                .build();

        carRepository.save(car);

        return car;
    }

    public JsonFullCarMessage createCarExtended(CarCommand carCommand) {
        if(isNull(carCommand) || isNull(getColorDefault(carCommand))){
            return getBadResponse(false);
        }
        String color=getColorDefault(carCommand);
        CarAvailabilityRestConnector carAvailability = carAvailabilityService.processStock(carCommand.getModel(), color);
        if (carAvailability.available(carCommand.getModel(), color)) {
            CarCommand carCommandModificate= CarCommand.builder().model(carCommand.getModel()).color(color).build();
            Message message = JsonFullCarMessage.Message.builder().car(create(carCommandModificate)).build();
            Response response = JsonFullCarMessage.Response.builder().code(ApplicationMessage.CREATED.getCode()).message(ApplicationMessage.CREATED.getMessage()).strCode(ApplicationMessage.CREATED.getStrCode()).sourceService("CarAvailabilityService").build();
            return JsonFullCarMessage.builder().message(message).response(response).build();
        }
        return getBadResponse(true);
    }

    public JsonFullCarMessage getBadResponse(boolean isNotAvailable) {
        Response response =null;
        if(isNotAvailable){
            response = JsonFullCarMessage.Response.builder().code(ApplicationMessage.UNAVAILABLE.getCode()).message(ApplicationMessage.UNAVAILABLE.getMessage()).strCode(ApplicationMessage.UNAVAILABLE.getStrCode()).build();
            return JsonFullCarMessage.builder().response(response).build();
        }
        response = JsonFullCarMessage.Response.builder().code(ApplicationMessage.UNEXPECTED.getCode()).message(ApplicationMessage.UNEXPECTED.getMessage()).strCode(ApplicationMessage.UNEXPECTED.getStrCode()).build();
        return JsonFullCarMessage.builder().response(response).build();
    }

    private String getColorDefault(CarCommand carCommand) {
        return ofNullable(carCommand.getColor()).map(String::toString).orElse(
                ofNullable(colorPickerService.getColorDefault(carCommand.getModel()))
                    .map(colorPickerRest -> {
                        if(colorPickerRest.pickColor(carCommand.getModel()).isPresent()){
                            return colorPickerRest.pickColor(carCommand.getModel()).get();
                        }
                        return null;
                    }).orElse(null));
    }


}
