package com.spring.example.webflux;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * curl -v http://localhost:8080/file/webflux?uri=https://www.w3.org/TR/PNG/iso_8859-1.txt
 * curl -v http://localhost:8080/file/webflux?uri=http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt
 * curl -v http://localhost:8080/file/webflux?uri=http://ipv4.download.thinkbroadband.com/5MB.zip > output.zip
 * curl -v http://localhost:8080/file/webflux?uri=http://ipv4.download.thinkbroadband.com/512MB.zip > output.zip
 *
 */

@Slf4j
@Component
class FileProxyHandler {

    FileProxyHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    Mono<ServerResponse> handle(ServerRequest serverRequest) {

        log.debug("Received request for downloading file request uri={}", serverRequest.queryParam("uri"));

        Mono<String> producer = webClient.get()
                 .uri(serverRequest.queryParam("uri").get())
                 .retrieve()
                 .bodyToMono(String.class);

        // https://stackoverflow.com/questions/49426304/convert-writes-to-outputstream-into-a-fluxdatabuffer-usable-by-serverresponse

        return ServerResponse.ok()
                             .header("RID", UUID.randomUUID().toString())
                             .body(producer, Byte.class)
                             .log();
    }

}
