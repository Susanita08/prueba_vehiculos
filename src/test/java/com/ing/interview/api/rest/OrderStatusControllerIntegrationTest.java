package com.ing.interview.api.rest;

import com.ing.interview.InterviewApplication;
import com.ing.interview.api.rest.connectors.OrderStatusRestConnector;
import com.ing.interview.api.rest.subordinated.webservice.OrderStatusRestService;
import com.ing.interview.domain.dto.OrderStatus;
import com.ing.interview.utils.LocalDateTimeUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wiremock.org.eclipse.jetty.server.Server;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InterviewApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderStatusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderStatusRestService orderRestService;

    /** El server http que servirá para hacer un mock del servicio real */
    private Server httpServer;
    private static final int PUERTO_HTTP = 3211;


    @BeforeEach
    public void setUp() throws Exception {
        //iniciamos el servidor mock
        httpServer = new Server(PUERTO_HTTP);
        httpServer.start();

    }

    @AfterEach
    public void tearDown() throws Exception {
        httpServer.stop();
    }

    public static Stream<Arguments> orderStatusEvenAndOdd() {
        return Stream.of(Arguments.of(546L, OrderStatus.builder().assignedTo("Sergi").stage("processing").lastUpdate(LocalDateTimeUtil.getFormatTime(LocalDateTime.now())).build()),
                Arguments.of(225L, OrderStatus.builder().assignedTo("Tomas").lastUpdate(LocalDateTimeUtil.getFormatTime(LocalDateTime.now())).stage("pending").build()));
    }

    @ParameterizedTest
    @MethodSource("orderStatusEvenAndOdd")
    void givenCarWhenFindOrderStatus(Long idCar) throws Exception {
        JSONObject car = new JSONObject();
        car.put("age", 50);
        car.put("model", "MERCEDES");
        car.put("color", "BLACK");

        OrderStatusRestConnector orderStatusRestConnector = new OrderStatusRestConnector();

        when(orderRestService.getOrderStatus(idCar)).thenReturn(orderStatusRestConnector);


        this.mockMvc
                .perform(
                        get("/order/api/v1/findCar/"+idCar)
                                .contentType(APPLICATION_JSON)
                                .content(car.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.model").value("MERCEDES"))
                .andExpect(jsonPath("$.color").value("BLACK"))
                .andExpect(jsonPath("$.orderDate").exists());
    }

}