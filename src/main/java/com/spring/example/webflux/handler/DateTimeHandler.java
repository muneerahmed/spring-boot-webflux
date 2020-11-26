package com.spring.example.webflux.handler;

import com.spring.example.webflux.service.DateTimeService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The handler for path /datetime and produces current date time from World Time API
 *
 * for ex: curl -v http://localhost:8080/flux-datetime?timezones=est,utc
 */

@Slf4j
@Component
public class DateTimeHandler {

    DateTimeHandler(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    private final DateTimeService dateTimeService;

    public Mono<ServerResponse> handle(ServerRequest request) {
        String[] timezones = request.queryParam("timezones")
                               .orElse("utc")
                               .split(",");
        log.debug("Received a request for /datetime with query parameter timezones={}", timezones);
        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(dateTimeService.getCurrentDateTimes(List.of(timezones)), Map.class);
    }
}
