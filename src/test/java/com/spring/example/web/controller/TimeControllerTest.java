package com.spring.example.web.controller;

import com.spring.example.web.service.TimeService;
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
 *
 * Rest Controller Responsibilities
 * --------------------------------
 * #	Responsibility	            Description
 * 1.	Listen to HTTP Requests	    The controller should respond to certain URLs, HTTP methods and content types.
 * 2.	Deserialize Input	        The controller should parse the incoming HTTP request and create Java objects from variables in the URL, HTTP request parameters and the request body so that we can work with them in the code.
 * 3.	Validate Input	            The controller is the first line of defense against bad input, so it’s a place where we can validate the input.
 * 4.	Call the Business Logic	    Having parsed the input, the controller must transform the input into the model expected by the business logic and pass it on to the business logic.
 * 5.	Serialize the Output	    The controller takes the output of the business logic and serializes it into an HTTP response.
 * 6.	Translate Exceptions	    If an exception occurs somewhere on the way, the controller should translate it into a meaningful error message and HTTP status for the user.
 *
 * @ExtendWith annotation to tell JUnit 5 to enable Spring support
 * @WebMvcTest annotation fire up an application context that contains only the beans needed for testing a web controller
 *
 * With @WebMvcTest, Spring Boot provides everything we need to build web controller tests,
 * but for the tests to be meaningful, we need to remember to cover all of the responsibilities.
 * Otherwise, we may be in for ugly surprises at runtime.
 */

@ExtendWith(SpringExtension.class) // annotation to tell JUnit 5 to enable Spring support
@WebMvcTest(controllers = TimeController.class) // annotation fire up an application context that contains only the beans needed for testing a web controller
public class TimeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean // @MockBean automatically replaces the bean of the same type in the application context with a Mockito mock
    private TimeService service;

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

        given(service.getCurrentDateTimes(anyList())).willReturn(
                Map.of("est", "2020-11-25T20:44-05:00", "utc", "2020-11-26T01:44Z"));

        mvc.perform(get("/web-datetime")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .queryParam("timezones", "est,utc")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.est").value("2020-11-25T20:44-05:00"))
                .andExpect(jsonPath("$.utc").value("2020-11-26T01:44Z"));
    }

}
