package com.ing.interview.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.interview.domain.service.OrderStatusService;
import com.ing.interview.enums.ApplicationMessage;
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

import java.util.HashMap;
import java.util.Map;

import static com.ing.interview.utilsTest.ConstantsUtils.*;
import static java.util.Optional.ofNullable;

@RestController
@RequestMapping(PATH_SEPARATOR + ORDERS + PATH_SEPARATOR + API + PATH_SEPARATOR + API_VERSION)
@Api(value = "/vehicle test")
public class OrderStatusController {

    private static final Log log = LogFactory.getLog(OrderStatusController.class);
    private final static Map<String, HttpStatus> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put(ApplicationMessage.UNEXPECTED.getStrCode(), HttpStatus.BAD_REQUEST);
        STATUS_MAP.put(ApplicationMessage.CREATED.getStrCode(), HttpStatus.CREATED);
    }

    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService=orderStatusService;
    }

    @ApiOperation(value = "Find Car by orderStatus in the system")
    @ApiResponses( {@ApiResponse(code = 200, message = "Find Car by orderStatus in the system")})
    @GetMapping(path = PATH_SEPARATOR + FIND_CAR + PATH_SEPARATOR + ID_CAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonFullCarMessage> findCarByOrderStatus(@PathVariable Long idCar) throws JsonProcessingException {
        log.info("Find Car by orderStatus in the system");
        JsonFullCarMessage jsonFullCarMessage=orderStatusService.findCarByOrderStatus(idCar);
        return ResponseEntity.status(getHttpStatusFromResponseCode(jsonFullCarMessage.getResponse().getStrCode())).body(jsonFullCarMessage);
    }

    private HttpStatus getHttpStatusFromResponseCode(String responseCode){
        log.info("Get HttpStatus of response in OrderStatusController: "+responseCode);
        return ofNullable(STATUS_MAP.get(responseCode)).orElse(HttpStatus.OK);
    }

}
