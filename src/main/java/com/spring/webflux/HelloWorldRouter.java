package com.spring.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * The router listens for traffic on the /hello?name={input} path and returns the value provided reactive handler class.
 *
 * for ex: curl -v http://localhost:8080/hello?name=allahhelpme
 *
 * https://spring.io/guides/gs/reactive-rest-service
 */
@Configuration
public class HelloWorldRouter {

    @Bean
    public RouterFunction<ServerResponse> route(HelloWorldHandler helloWorldHandler) {

        return RouterFunctions
                .route(
                        RequestPredicates.GET("/hello")
                                         .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), helloWorldHandler::hello);
    }
}
