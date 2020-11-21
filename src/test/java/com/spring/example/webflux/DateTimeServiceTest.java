package com.spring.example.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/*
    https://dzone.com/articles/unit-tests-for-springs-webclient
 */
@Tag("unit-tests")
public class DateTimeServiceTest {

    public DateTimeService getService(Map<String, String> map) throws JsonProcessingException {
        ClientResponse clientResponse = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMapper().writeValueAsString(map))
                .build();
        WebClient webClient = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(clientResponse))
                .build();
        return new DateTimeService(webClient);
    }

    @Test
    @DisplayName("Current EST & UTC Datetime")
    public void getCurrentDateTime() throws JsonProcessingException  {
        DateTimeService service = getService(Map.of(DateTimeService.CURRENT_DATETIME, "2020-11-15T00:43:47.123850+00:00"));
        Mono<Map> output = service.getCurrentDateTimes();
        StepVerifier.create(output)
                .consumeNextWith(e -> Assertions.assertEquals(e.get(DateTimeService.EST_DATETIME), "2020-11-15T00:43:47.123850+00:00"))
                .expectNext()
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Empty EST & UTC Datetime")
    public void getCurrentDateTime1() throws JsonProcessingException  {
        DateTimeService service = getService(Map.of());
        Mono<Map> output = service.getCurrentDateTimes();
        StepVerifier.create(output)
                .expectComplete()
                .verify();
    }

}
