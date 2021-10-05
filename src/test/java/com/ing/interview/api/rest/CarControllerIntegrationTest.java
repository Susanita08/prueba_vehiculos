package com.ing.interview.api.rest;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ing.interview.InterviewApplication;
import com.ing.interview.api.rest.connectors.CarAvailabilityRestConnector;
import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.api.rest.connectors.InsuranceRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.CarAvailabilityService;
import com.ing.interview.api.rest.subordinated.webservice.ColorPickerService;
import com.ing.interview.api.rest.subordinated.webservice.InsuranceService;
import com.ing.interview.objects.CarCommand;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.org.eclipse.jetty.server.Server;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InterviewApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ColorPickerService colorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CarAvailabilityService carAvailabilityService;

    @MockBean
    private InsuranceService insuranceService;

    @Test
    void givenCarWhenCreateCarExtended() throws Exception {
        JSONObject car = new JSONObject();
        car.put("age", 20);
        car.put("model", "FIAT");
        car.put("color", "YELLOW");

        CarCommand carCommand = new CarCommand(20, "YELLOW", "FIAT");

        ColorPickerRestConnector colorPickerRestConnector = new ColorPickerRestConnector();
        InsuranceRestConnector insuranceRestConnector = new InsuranceRestConnector();
        CarAvailabilityRestConnector carAvailabilityRestConnector = new CarAvailabilityRestConnector();

        when(colorService.getColorDefault(carCommand.getAge(), carCommand.getModel())).thenReturn(colorPickerRestConnector);
        when(insuranceService.getAllowedByModel(carCommand.getAge(), carCommand.getModel())).thenReturn(insuranceRestConnector);
        when(carAvailabilityService.processStock(carCommand.getAge(), carCommand.getModel(), carCommand.getColor())).thenReturn(carAvailabilityRestConnector);

        this.mockMvc
                .perform(
                        post("/cars/api/v1/car")
                                .contentType(APPLICATION_JSON)
                                .content(car.toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.model").value("FIAT"))
                .andExpect(jsonPath("$.color").value("YELLOW"))
                .andExpect(jsonPath("$.orderDate").exists());
    }

}