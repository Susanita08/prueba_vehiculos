package com.ing.interview.config;

import io.netty.channel.ChannelOption;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@Data
public class SelfConfiguration {

    @Value("${ing.service.car.availability.internal.timeout}")
    private Integer internalTimeout;

    @Bean
    @Primary
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getInternalTimeout());
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }
}
