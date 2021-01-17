package com.spring.example.webflux.handler;
import com.spring.example.webflux.config.RouterConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@Tag("unit-tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RouterConfig.class, FileProxyHandler.class})
@WebFluxTest
public class FileProxyHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private HelloHandler helloHandler;

    @MockBean
    private DateTimeHandler handler;

    @Test
    public void test_hello() {
        webTestClient.get()
                .uri(builder -> builder.path("/flux-file").queryParam("uri", "https://www.w3.org/TR/PNG/iso_8859-1.txt").build())
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }
}
