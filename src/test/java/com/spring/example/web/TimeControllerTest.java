package com.spring.example.web;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Do we write unit tests? Or integration tests?
 * https://reflectoring.io/unit-testing-spring-boot
 * https://reflectoring.io/spring-boot-web-controller-test
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(TimeController.class)
public class TimeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TimeService service;

    @Test
    public void getCurrentDateTimeTest() throws Exception {

        given(service.getCurrentDateTimes(anyList())).willReturn(
                Map.of("est", "2020-11-25T20:44-05:00", "utc", "2020-11-26T01:44Z"));

        mvc.perform(get("/web-datetime")
                .queryParam("timezones", "est,utc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.est").value("2020-11-25T20:44-05:00"))
                .andExpect(jsonPath("$.utc").value("2020-11-26T01:44Z"));
    }

}
