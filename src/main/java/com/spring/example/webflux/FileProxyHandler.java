package com.spring.example.webflux;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
public class FileProxyHandler {

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        log.debug("Received request for downloading file uri {}", serverRequest.queryParam("uri"));
        return ServerResponse.ok()
                             .contentType(MediaType.TEXT_PLAIN)
                             .header("RID", UUID.randomUUID().toString())
                             .bodyValue("Sample Text File");
    }
}
