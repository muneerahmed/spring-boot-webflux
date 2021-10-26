package com.spring.example.webflux.config;

import com.spring.example.webflux.handler.DateTimeHandler;
import com.spring.example.webflux.handler.FileProxyHandler;
import com.spring.example.webflux.handler.HelloHandler;
import com.spring.example.webflux.handler.SSEHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
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
public class RouterConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RouterFunction<ServerResponse> helloRouter(HelloHandler helloHandler) {
        return RouterFunctions
                .route(
                        RequestPredicates.GET("/flux-hello")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), helloHandler::hello);
    }

    @Bean
    public RouterFunction<ServerResponse> fileRouter(FileProxyHandler handler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/flux-file"), handler::handle);
    }

    @Bean
    public RouterFunction<ServerResponse> datetimeRouter(DateTimeHandler handler) {
        return RouterFunctions.route(
                                RequestPredicates.GET("/flux-datetime"), handler::handle);
    }

    @Bean
    public RouterFunction<ServerResponse> sseRouter(SSEHandler handler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/sse"), handler::handle);
    }
}
