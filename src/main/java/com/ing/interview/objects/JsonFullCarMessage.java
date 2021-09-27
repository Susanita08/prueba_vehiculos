package com.ing.interview.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.domain.dto.OrderStatus;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonFullCarMessage implements Serializable {
    private static final long serialVersionUID = 1554669223822615716L;

    @Valid
    @NotNull
    private Response response;

    @Valid
    @NotNull
    private Message message;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response implements Serializable {
        private static final long serialVersionUID = 3538641147032958369L;

        @Min(value=0)
        @Digits(integer = 2, fraction =0)
        private Integer code;

        private String message;

        private String strCode;

        private String sourceService;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Message implements Serializable {
        private static final long serialVersionUID = 5220869892943424924L;
        private Car car;
        private OrderStatus orderStatus;
    }
}
