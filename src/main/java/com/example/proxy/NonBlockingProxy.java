package com.example.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 *
 * curl -v http://localhost:8080/web/file/nio?uri=https://www.w3.org/TR/PNG/iso_8859-1.txt
 * curl -v http://localhost:8080/web/file/nio?uri=http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt
 * curl -v http://localhost:8080/web/file/nio?uri=http://ipv4.download.thinkbroadband.com/5MB.zip > output.zip
 * curl -v http://localhost:8080/web/file/nio?uri=http://ipv4.download.thinkbroadband.com/512MB.zip > output.zip
 *
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class NonBlockingProxy {

    public NonBlockingProxy(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient
                    .create()
                    .wiretap(false);
        this.webClient = webClientBuilder
                            .clientConnector(new ReactorClientHttpConnector(httpClient))
                            .build();
    }

    private final WebClient webClient;

    static class AsyncCompletedListener implements AsyncListener {

        private final AtomicBoolean completed = new AtomicBoolean();

        public boolean isCompleted() {
            return completed.get();
        }

        @Override
        public void onComplete(AsyncEvent event) {
            log.error("Listener Request is Completed");
            completed.set(true);
        }

        @Override
        public void onTimeout(AsyncEvent event) {
            log.error("timed out while proxying {}", event.getThrowable());
            completed.set(true);
        }

        @Override
        public void onError(AsyncEvent event) {
            log.error("error occurred while proxying {}", event.getThrowable());
            completed.set(true);
        }

        @Override
        public void onStartAsync(AsyncEvent event) {

        }
    }

    @GetMapping("/nio")
    public void nioFile(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam("uri") String uri) throws IOException {
        log.debug("Received request for download file");
        OutputStream outputStream = response.getOutputStream();
        AsyncContext asyncContext = request.startAsync(request, response);
        AsyncCompletedListener asyncCompletedListener = new AsyncCompletedListener();
        asyncContext.addListener(asyncCompletedListener);
        webClient.get()
                .uri(uri)
                .exchange()
                .flatMap(clientResponse -> {
                    log.debug("Handling Response");
                    response.setStatus(clientResponse.rawStatusCode());
                    copyHeaders(clientResponse, response);
                    Mono<Void> mono = copyBody(clientResponse, outputStream);
                    log.debug("Handling Response Completed");
                    return mono;
                })
                .doFinally(signalType ->  {
                        log.debug("Completed Request");
                        if(!asyncCompletedListener.isCompleted()) {
                            asyncContext.complete();
                        }
                    })
                .subscribe();
    }

    private void copyHeaders(ClientResponse fileResponse, HttpServletResponse response) {
        log.debug("Writing Response Headers");
        fileResponse.headers()
                    .asHttpHeaders()
                    .forEach((k, v) ->  v.forEach(e -> response.addHeader(k, e)));
    }

    private Mono<Void> copyBody(ClientResponse fileResponse, OutputStream outputStream) {
        log.debug("Writing Response Body");
        Flux<DataBuffer> buffers = fileResponse.bodyToFlux(DataBuffer.class).limitRate(10);
        return DataBufferUtils.write(buffers, outputStream)
                .map(DataBufferUtils::release)
                .then();
    }
}
