package com.spring.example.webflux;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
class DateTimeService {

    static final String DATETIME = "datetime";
    static final String URI = "http://worldtimeapi.org/api/ip";

    DateTimeService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    public Mono<Map> getCurrentDateTime() {
        return webClient.get()
                                    .uri(URI)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(Map.class);
    }
}
