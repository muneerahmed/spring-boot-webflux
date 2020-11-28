package com.spring.example.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test multiple layers and the whole path through the application.
 *
 * @SpringBootTest is a very convenient method to set up an application context for tests that is very close the one we’ll have in production.
 * @SpringBootTest brings the most value if we want to test the whole way through the application.
 */
@ExtendWith(SpringExtension.class) // annotation to tell JUnit 5 to enable Spring support
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WebIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenEmptyQueryParam_thenReturns400AndErrorResult() throws Exception {
        mvc.perform(get("/web-datetime")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("timeZones query parameter is mandatory"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("timeZones query parameter is mandatory"));
    }

    @Test
    public void getCurrentDateTimeTest() throws Exception {
        mvc.perform(get("/web-datetime")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .queryParam("timezones", "est,utc")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.est").exists())
                .andExpect(jsonPath("$.utc").exists());
    }
}
