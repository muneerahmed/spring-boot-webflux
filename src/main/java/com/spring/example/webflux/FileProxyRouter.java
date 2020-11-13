package com.spring.example.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FileProxyRouter {

    @Bean
    public RouterFunction<ServerResponse> fileRouter(FileProxyHandler handler) {
        return RouterFunctions.route(RequestPredicates.GET("/file/webflux"), handler::handle);
    }
}
