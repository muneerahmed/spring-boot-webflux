package com.spring.example.webflux.handler;


import com.spring.example.webflux.config.RouterConfig;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
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
@ContextConfiguration(classes = {RouterConfig.class, HelloHandler.class})
@WebFluxTest
public class HelloHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FileProxyHandler fileProxyHandler;

    @MockBean
    private DateTimeHandler handler;

    @Test
    public void test_hello() {
        String name = "Isa";
        webTestClient.get()
                .uri(builder -> builder.path("/flux-hello").queryParam("name", name).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(userResponse -> {
                            Assertions.assertEquals(userResponse.get("result"), "Allah Help Me Optional[Isa]");
                        }
                );
    }
}
