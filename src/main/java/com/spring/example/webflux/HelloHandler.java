package com.spring.example.webflux;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The handler for path /hello and produces an output
 *
 * for ex: curl -v http://localhost:8080/flux-hello?name=isa
 *
 * https://spring.io/guides/gs/reactive-rest-service
 *
 * read marble diagram : https://medium.com/@jshvarts/read-marble-diagrams-like-a-pro-3d72934d3ef5
 */

@Slf4j
@Component
public class HelloHandler {

    public static final String NAME = "name";

    /**
     * @param request
     *
     * @return a Mono object that holds a ServerResponse body
     */
    public Mono<ServerResponse> hello(ServerRequest request) {
        log.debug("Received a request for /hello with query parameter name={}", request.queryParam(NAME));
        return Mono.from(ServerResponse
                            .ok()
                            .contentType(MediaType.TEXT_PLAIN)
                            .header("rid", UUID.randomUUID().toString())
                            .body(BodyInserters.
                                        fromValue(String.format("Allah Help Me %s", request.queryParam(NAME)))))
                            .log();
    }
}
