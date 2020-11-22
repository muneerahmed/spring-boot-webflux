package com.spring.example.webflux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebClient tutorial https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
 *
 */

@Service
class DateTimeService {

    static final String EST  = "est";
    static final String UTC  = "utc";
    static final String CURRENT_DATETIME = "currentDateTime";
    static final String URL = "http://worldclockapi.com/api/json/%s/now";

    DateTimeService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    public Flux<Map> getCurrentDateTimes(List<String> timezones) {
        return Flux.merge(
                    timezones.stream()
                          .map(this::get)
                          .collect(Collectors.toList())
                );
    }

    private Mono<Map> get(String timezone) {
        return   getWorldTime(timezone)
                .map(e -> e.get(CURRENT_DATETIME))
                .map(e -> Map.of(timezone, e));
    }

    private Mono<Map> getWorldTime(String timezone) {
        return webClient.get()
                .uri(String.format(URL, timezone))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorReturn(Map.of(CURRENT_DATETIME, "N/A"))
                .log();
    }
}
