package com.ing.interview.domain.service;

import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.objects.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.domain.repository.CarRepository;
import java.time.LocalDate;

import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.Message;
import com.ing.interview.objects.JsonFullCarMessage.Response;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarAvailabilityService carAvailabilityService;

    public CarService(CarRepository carRepository, CarAvailabilityService carAvailabilityService) {
        this.carRepository = carRepository;
        this.carAvailabilityService = carAvailabilityService;
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

    public JsonFullCarMessage createCarVerifyAvailability(CarCommand carCommand) {
        CarAvailabilityRestConnector carAvailability=carAvailabilityService.processStock(carCommand.getModel(), carCommand.getColor());
        Message message = null;
        Response response;
        if(carAvailability.available(carCommand.getModel(), carCommand.getColor())){
            create(carCommand);
            message = JsonFullCarMessage.Message.builder().carCommand(carCommand).build();
            response = JsonFullCarMessage.Response.builder().code(ApplicationMessage.SUCCESS.getCode()).message(ApplicationMessage.SUCCESS.getMessage()).build();
            return JsonFullCarMessage.builder().message(message).response(response).build();
        }
        response =JsonFullCarMessage.Response.builder().code(ApplicationMessage.UNAVAILABLE.getCode()).message(ApplicationMessage.UNAVAILABLE.getMessage()).strCode(ApplicationMessage.UNAVAILABLE.getStrCode()).build();
        return JsonFullCarMessage.builder().response(response).build();
    }

}
