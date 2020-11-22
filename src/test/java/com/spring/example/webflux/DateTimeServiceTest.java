package com.spring.example.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/*
    https://dzone.com/articles/unit-tests-for-springs-webclient
 */
@Tag("unit-tests")
public class DateTimeServiceTest {

    public static final String OUTPUT = "2020-11-15T00:43:47.123850+00:00";

    public DateTimeService getService(Map<String, String> map, HttpStatus status) throws JsonProcessingException {
        ClientResponse clientResponse = ClientResponse.create(status)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMapper().writeValueAsString(map))
                .build();
        WebClient webClient = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(clientResponse))
                .build();
        return new DateTimeService(webClient);
    }

    @Test
    @DisplayName("Current EST & UTC Datetime Success Test")
    public void getEstUtcDatetimeTest() throws JsonProcessingException  {
        DateTimeService service = getService(Map.of(DateTimeService.CURRENT_DATETIME, OUTPUT), HttpStatus.OK);
        Flux<Map> output = service.getCurrentDateTimes(List.of(DateTimeService.EST, DateTimeService.UTC));
        StepVerifier.create(output)
                .consumeNextWith(e -> Assertions.assertEquals(e.get(DateTimeService.EST), OUTPUT))
                .consumeNextWith(e -> Assertions.assertEquals(e.get(DateTimeService.UTC), OUTPUT))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Current EST & UTC Datetime Failure Test")
    public void notFoundTest() throws JsonProcessingException  {
        DateTimeService service = getService(Map.of(), HttpStatus.NOT_FOUND);
        Flux<Map> output = service.getCurrentDateTimes(List.of(DateTimeService.EST, DateTimeService.UTC));
        StepVerifier.create(output)
                .consumeNextWith(e -> Assertions.assertEquals(e.get(DateTimeService.EST), "N/A"))
                .consumeNextWith(e -> Assertions.assertEquals(e.get(DateTimeService.UTC), "N/A"))
                .expectComplete()
                .verify();
    }

}
