package com.spring.example.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * The router listens for traffic for different paths and routes it to registered handler class.
 *
 */
@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RouterFunction<ServerResponse> helloRouter(HelloHandler helloHandler) {
        return RouterFunctions
                .route(
                        RequestPredicates.GET("/hello")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), helloHandler::hello);
    }

    @Bean
    public RouterFunction<ServerResponse> datetimeRouter(DateTimeHandler handler) {
        return RouterFunctions.route(
                                RequestPredicates.GET("/datetime"), handler::handle);
    }

    @Bean
    public RouterFunction<ServerResponse> fileRouter(FileProxyHandler handler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/file/webflux"), handler::handle);
    }

}
