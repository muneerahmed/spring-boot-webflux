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

    static final String DATETIME = "datetime";
    static final String URI = "http://worldtimeapi.org/api/ip";

    DateTimeService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    public Mono<String> getCurrentDateTime() {
        return webClient.get()
                                    .uri(URI)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(Map.class)
                                    .map(e -> e.get(DATETIME).toString());
    }
}
