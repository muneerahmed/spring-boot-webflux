package com.spring.example.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/*
    https://dzone.com/articles/unit-tests-for-springs-webclient
 */
public class DateTimeServiceTest {

    @Test
    public void getCurrentDateTime() throws JsonProcessingException {
        Map<String, String> map = Map.of(DateTimeService.DATETIME, "2020-11-15T00:43:47.123850+00:00");
        ClientResponse clientResponse = ClientResponse.create(HttpStatus.OK)
                                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .body(new ObjectMapper().writeValueAsString(map))
                                        .build();
        WebClient webClient = WebClient.builder()
                                        .exchangeFunction(clientRequest -> Mono.just(clientResponse))
                                        .build();
        DateTimeService service = new DateTimeService(webClient);

        Mono<String> output = service.getCurrentDateTime();

        StepVerifier.create(output)
                .expectNext("2020-11-15T00:43:47.123850+00:00")
                .expectComplete()
                .verify();
    }
}
