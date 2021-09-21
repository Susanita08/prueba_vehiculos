package com.ing.interview.api.rest.connectors;

import com.ing.interview.objects.OrderStatus;
import java.time.LocalDateTime;

public class OrderStatusRestConnector {

    public OrderStatus checkOrderStatus(Long id) {
        return id % 2 == 0
            ? OrderStatus.builder().assignedTo("Sergi").stage("processing").lastUpdate(LocalDateTime.now()).build()
            : OrderStatus.builder().assignedTo("Tomas").stage("pending").lastUpdate(LocalDateTime.now()).build();
    }

}
