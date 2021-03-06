package com.ing.interview.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.domain.service.OrderStatusService;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.JsonFullCarMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/*import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;*/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ing.interview.utils.ConstantsUtils.*;
import static java.util.Optional.ofNullable;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(PATH_SEPARATOR + ORDERS + PATH_SEPARATOR + API + PATH_SEPARATOR + API_VERSION)
@Api(value = "/api-cars")
public class OrderStatusController {

    private static final Log log = LogFactory.getLog(OrderStatusController.class);
    private static final Map<String, HttpStatus> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put(ApplicationMessage.UNEXPECTED.getStrCode(), HttpStatus.BAD_REQUEST);
        STATUS_MAP.put(ApplicationMessage.SUCCESS.getStrCode(), HttpStatus.OK);
    }

    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService=orderStatusService;
    }

    @ApiOperation(value = "Find Car by orderStatus in the system")
    @ApiResponses( {@ApiResponse(code = 200, message = "Find Car by orderStatus in the system")})
    @GetMapping(path = PATH_SEPARATOR + FIND_CAR + PATH_SEPARATOR + ID_CAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Car> findCarByOrderStatus(@PathVariable Long idCar) throws JsonProcessingException {
        log.info("Find Car by orderStatus in the system");
        JsonFullCarMessage jsonFullCarMessage=orderStatusService.findCarByOrderStatus(idCar);
        addSelfLink(jsonFullCarMessage.getMessage().getCar());
        return ResponseEntity.status(getHttpStatusFromResponseCode(jsonFullCarMessage.getResponse().getStrCode())).body(jsonFullCarMessage.getMessage().getCar());
    }

    private HttpStatus getHttpStatusFromResponseCode(String responseCode){
        log.info("Get HttpStatus of response in OrderStatusController: "+responseCode);
        return ofNullable(STATUS_MAP.get(responseCode)).orElse(HttpStatus.OK);
    }

    private void addSelfLink(Car car) throws JsonProcessingException {
        car.add(linkTo(methodOn(OrderStatusController.class).findCarByOrderStatus(car.getId())).withSelfRel());
    }

}
