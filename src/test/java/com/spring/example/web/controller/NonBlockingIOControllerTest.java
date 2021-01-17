package com.spring.example.web.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit-tests")
@ExtendWith(SpringExtension.class) // annotation to tell JUnit 5 to enable Spring support
@WebMvcTest(controllers = NonBlockingIOController.class) // annotation fire up an application context that contains only the beans needed for testing a web controller
public class NonBlockingIOControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private WebClient.Builder builder;

    // @Test
    public void test_bio() throws Exception {
        mvc.perform(get("/nio-file")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .queryParam("uri", "https://www.w3.org/TR/PNG/iso_8859-1.txt")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }
}
