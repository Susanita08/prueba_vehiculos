package com.ing.interview.objects;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderStatus {

    private final String assignedTo;

    private final LocalDateTime lastUpdate;

    private final String stage;

}
