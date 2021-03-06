package com.spring.example.webflux.handler;

import com.spring.example.webflux.config.RouterConfig;
import com.spring.example.webflux.service.DateTimeService;
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
import reactor.core.publisher.Mono;


import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@Tag("unit-tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RouterConfig.class, DateTimeHandler.class})
@WebFluxTest
public class DateTimeHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DateTimeService service;

    @MockBean
    private HelloHandler helloHandler;

    @MockBean
    private FileProxyHandler fileProxyHandler;

    @Test
    public void get_flux_datetime_success_Test() {

        given(service.getCurrentDateTimes(anyList())).willReturn(
                Mono.just(Map.of("est", "2020-11-25T20:44-05:00", "utc", "2020-11-26T01:44Z")));

        webTestClient.get()
                .uri("/flux-datetime")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(userResponse -> {
                            Assertions.assertEquals(userResponse.get("est"), "2020-11-25T20:44-05:00");
                            Assertions.assertEquals(userResponse.get("utc"), "2020-11-26T01:44Z");
                        }
                );
    }
}
