package com.spring.example.webflux;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * WebClient tutorial https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
 *
 */

@Service
class DateTimeService {

    static final String UTC_DATETIME = "utc";
    static final String EST_DATETIME = "est";
    static final String CURRENT_DATETIME = "currentDateTime";
    static final String UTC_URL = "http://worldclockapi.com/api/json/utc/now";
    static final String EST_URL = "http://worldclockapi.com/api/json/est/now";

    DateTimeService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    public Mono<Map> getCurrentDateTimes() {
        return getWorldTime(UTC_URL).filter(e -> e.containsKey(CURRENT_DATETIME))
                             .map(e -> e.get(CURRENT_DATETIME))
                             .map(e -> Map.of(EST_DATETIME, e));
    }

    private Mono<Map> getWorldTime(String url) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class);
    }
}
