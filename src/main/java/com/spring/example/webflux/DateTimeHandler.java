package com.spring.example.webflux;

import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The handler for path /datetime and produces current date time from World Time API
 *
 * for ex: curl -v http://localhost:8080/datetime?timezones=est,utc
 */

@Component
class DateTimeHandler {

    DateTimeHandler(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    private final DateTimeService dateTimeService;

    Mono<ServerResponse> handle(ServerRequest request) {
        String[] timezones = request.queryParam("timezones")
                               .orElse("utc")
                               .split(",");
        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(dateTimeService.getCurrentDateTimes(List.of(timezones)), Map.class);
    }
}
