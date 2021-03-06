package com.ing.interview.api.rest;

import com.ing.interview.domain.dto.Car;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.domain.service.CarService;
import com.ing.interview.objects.JsonFullCarMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.ing.interview.utils.ConstantsUtils.*;
import static java.util.Optional.ofNullable;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(PATH_SEPARATOR + CARS + PATH_SEPARATOR + API + PATH_SEPARATOR + API_VERSION)
@Api(value = "/api-cars")
public class CarController {
    private static final Log log = LogFactory.getLog(CarController.class);

    private final static Map<String, HttpStatus> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put(ApplicationMessage.UNAVAILABLE.getStrCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        STATUS_MAP.put(ApplicationMessage.UNEXPECTED.getStrCode(), HttpStatus.BAD_REQUEST);
        STATUS_MAP.put(ApplicationMessage.CREATED.getStrCode(), HttpStatus.CREATED);
    }

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @ApiOperation(value = "Create new car in the system, called to stock service and configured default color")
    @ApiResponses( {
            @ApiResponse(code = 201, message = "Create new resource of a car called to stock service and configured default color."),
            @ApiResponse(code = 400, message = "Some microservice did not respond"),
            @ApiResponse(code = 500, message = "CardAvailabilityService not availability for the model specific")
    })

    @PostMapping(path = PATH_SEPARATOR + CREATE_CAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Car> createCarExtended(@Valid @RequestBody CarCommand carCommand) {
        log.info("Create car extended");
        JsonFullCarMessage jsonFullCarMessage=carService.createCarExtended(carCommand);
        jsonFullCarMessage.getMessage().getCar().add(linkTo(methodOn(CarController.class).createCarExtended(carCommand)).withSelfRel());
        return ResponseEntity.status(getHttpStatusFromResponseCode(jsonFullCarMessage.getResponse().getStrCode())).body(jsonFullCarMessage.getMessage().getCar());
       }

    private HttpStatus getHttpStatusFromResponseCode(String responseCode){
        log.info("Get HttpStatus of response in CarController: "+responseCode);
        return ofNullable(STATUS_MAP.get(responseCode)).orElse(HttpStatus.OK);
    }



}
