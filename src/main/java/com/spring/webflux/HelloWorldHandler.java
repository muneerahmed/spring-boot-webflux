package com.spring.webflux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HelloWorldHandler {

    /**
     * @param request
     *
     * @return a Mono object that holds a ServerResponse body
     */
    public Mono<ServerResponse> hello(ServerRequest request) {
        var name = request.queryParam("name");
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(String.format("Welcome to the World of Spring Flux Mr/Mrs %s", name)));
    }
}
