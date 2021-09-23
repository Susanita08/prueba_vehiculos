package com.ing.interview.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.interview.api.rest.connectors.ColorPickerRestConnector;
import com.ing.interview.domain.dto.Car;
import com.ing.interview.utils.CarMessageUtil;
import lombok.*;
import org.springframework.http.HttpEntity;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonFullCarMessage implements Serializable {
    private static final long serialVersionUID = 1554669223822615716L;

    @Valid
    @NotNull
    private Response response;

    @Valid
    @NotNull
    private Message message;


    public JsonFullCarMessage(){
        this.response=new Response();
        this.message= new Message();
    }

    public HttpEntity<String> toHttpEntity() throws JsonProcessingException {
        return CarMessageUtil.toHttpEntity(this);
    }

    public JsonFullCarMessage mergeWith(JsonFullCarMessage otherMessage) throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CarMessageUtil.mergeWith(this, otherMessage);
    }

    public void unexpectedError(String service){
        CarMessageUtil.setUnexpectedError(this,service);
    }

    @NoArgsConstructor
    @Getter
    @Setter
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

    @NoArgsConstructor
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Message implements Serializable {
        private static final long serialVersionUID = 5220869892943424924L;
        private Car car;
        //private CarCommand carCommand;
        private OrderStatus orderStatus;

        private ColorPickerRestConnector colorPickerRestConnector;
    }
}
