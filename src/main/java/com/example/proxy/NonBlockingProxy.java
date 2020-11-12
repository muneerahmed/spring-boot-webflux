package com.example.proxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Controller
@RequestMapping("/file")
public class NonBlockingProxy {

    public NonBlockingProxy(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient
                    .create()
                    .wiretap(false);
        this.webClient = webClientBuilder
                            .baseUrl("http://ipv4.download.thinkbroadband.com")
                            .clientConnector(new ReactorClientHttpConnector(httpClient))
                            .build();
    }

    private final WebClient webClient;
    private static final String TEXT_FILE_URL = "http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt";
    private static final String ZIP_FILE_URL = "http://ipv4.download.thinkbroadband.com/5MB.zip";
    private static final String LOCATION = TEXT_FILE_URL;

    @GetMapping("/nio")
    public void nioFile(HttpServletRequest request,
                        HttpServletResponse response) {
        log.debug("Starting Worker Thread for /nio !");
        AsyncContext asyncContext = request.startAsync(request, response);
        webClient.get()
                .uri("/5MB.zip")
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchange()
                .doOnSuccess((ClientResponse clientResponse) -> {
                    log.debug("Writing Response Async Worker Thread !");
                    copyHeaders(clientResponse, response);
                    copyBody(asyncContext, clientResponse, response);
                })
                .subscribe();
        log.debug("Finished Worker Thread for /nio !");
    }

    private void copyHeaders(ClientResponse fileResponse, HttpServletResponse response) {
        log.debug("Writing Response Headers !");
        fileResponse.headers()
                    .asHttpHeaders()
                    .forEach((k, v) ->  response.addHeader(k, v.get(0)));
    }

    private void copyBody(AsyncContext asyncContext, ClientResponse fileResponse, HttpServletResponse response) {
        log.debug("Writing Response Body !");

        Flux<DataBuffer> flux = fileResponse.bodyToFlux(DataBuffer.class);
        try {
            DataBufferUtils.write(flux, response.getOutputStream())
                            .limitRate(1000)
                            .log()
                            .subscribe(e -> {
                                DataBufferUtils.release(e);
                            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
