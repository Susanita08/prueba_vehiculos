package com.ing.interview.api.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.method;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ing.interview.InterviewApplication;
import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.api.rest.subordinated.webservice.configuration.CarAvailabilityServiceConfiguration;
import com.ing.interview.domain.repository.CarRepository;
import com.ing.interview.domain.service.CarService;
import com.ing.interview.enums.ApplicationMessage;
import com.ing.interview.objects.CarCommand;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.mother.JsonFullCarMessageMother;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.org.eclipse.jetty.server.Server;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static reactor.core.publisher.Mono.when;

import java.util.Objects;

@AutoConfigureMockMvc
@AutoConfigureWebTestClient(timeout = "100000")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InterviewApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CarAvailabilityService carAvailabilityService;

    @Mock
    private CarAvailabilityServiceConfiguration carAvailabilityServiceConfiguration;

    @Autowired
    private CarRepository carRepository;

    /** El server http que servirá para hacer un mock del servicio real */
    private Server httpServer;
    private static final int PUERTO_HTTP = 3210;
    private static final String URL_SERVICIO = "http://localhost:" + PUERTO_HTTP;


    @Before
    public void setUp() throws Exception {
        carAvailabilityService= mock(CarAvailabilityService.class);
        //iniciamos el servidor mock
        httpServer = new Server(PUERTO_HTTP);
        httpServer.start();

    }

    @After
    public void tearDown() throws Exception {
        httpServer.stop();
    }

    @Test
    void givenCarWhenCreateThenReturnIsCreated() throws Exception {

        JSONObject car = new JSONObject();
        car.put("age", 16);
        car.put("model", "FIAT");

        this.mockMvc
                .perform(
                        post("/cars/api/v1/car")
                                .contentType(APPLICATION_JSON)
                                .content(car.toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.model").value("FIAT"))
                .andExpect(jsonPath("$.orderDate").exists());
    }

    @Test
    void givenCarOnlyIfExists_ThenCreateThenReturnIsCreated() throws Exception {

        CarCommand carCommand = new CarCommand(0, "BLACK", "MERCEDES");
        JsonFullCarMessage jsonFullCarMessageExpected = JsonFullCarMessageMother.getMessageCarAvailableResponse(carCommand);

        CarAvailabilityRestConnector carAvailabilityRestConnector= new CarAvailabilityRestConnector();

        Mockito.when(carAvailabilityService.processStock(any(), any())).thenReturn(carAvailabilityRestConnector);

        CarService carService = new CarService(carRepository, carAvailabilityService);
        CarController carController = new CarController(carService);

        ResponseEntity<JsonFullCarMessage>  jsonOutput = carController.createCarVerifyStock(carCommand);

        verify(carAvailabilityService).processStock(eq(carCommand.getModel()), eq(carCommand.getColor()));

        assertEquals(ApplicationMessage.CREATED.getCode(), Objects.requireNonNull(jsonOutput.getBody()).getResponse().getCode());
        assertEquals(ApplicationMessage.CREATED.getMessage(), jsonOutput.getBody().getResponse().getMessage());
        assertEquals(ApplicationMessage.CREATED.getStrCode(), jsonOutput.getBody().getResponse().getStrCode());

        assertEquals(objectMapper.writeValueAsString(jsonFullCarMessageExpected), objectMapper.writeValueAsString(jsonOutput.getBody()));
    }

}