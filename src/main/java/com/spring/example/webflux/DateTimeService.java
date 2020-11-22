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
    static final String EST_URL = "http://worldclockapi.com/api/json/est/now";
    static final String UTC_URL = "http://worldclockapi.com/api/json/utc/now";
    static final String CURRENT_DATETIME = "currentDateTime";
    static final Map<String, String> URLS = Map.of(EST, EST_URL, UTC, UTC_URL);

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

    private Mono<Map> get(String timeZone) {
        return   getWorldTime(URLS.get(timeZone))
                .map(e -> e.get(CURRENT_DATETIME))
                .map(e -> Map.of(timeZone, e));
    }

    private Mono<Map> getWorldTime(String url) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorReturn(Map.of(CURRENT_DATETIME, "N/A"))
                .log();
    }
}
