package com.spring.example.web;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import static com.spring.example.web.TimeService.CURRENT_DATETIME;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Tag("unit-tests")
@ExtendWith(MockitoExtension.class) // The @MockitoExtension tells Mockito to evaluate those @Mock annotations
public class TimeServiceTest {

    public static final String EST = "est";
    public static final String UTC = "utc";
    public static final String DATETIME = "2020-11-24 18:48:13";

    @InjectMocks
    private TimeService timeService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity responseEntity;

    @Test
    void getCurrentDateTimesTest() {

        when(restTemplate.getForEntity(anyString(), eq(Map.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(Map.of(CURRENT_DATETIME, DATETIME));

        Map<String, Object> result = timeService.getCurrentDateTimes(List.of(EST, UTC));

        // When to group multiple assertions
        // Best Practice: Combine all Assertions for a given unit of test
        // for eq: here we want to make sure that the result is not null and is of size 2 and contains specific elements
        Assertions.assertAll( () -> Assertions.assertNotNull(result),
                              () -> Assertions.assertTrue(!result.isEmpty()),
                              () -> Assertions.assertEquals(2, result.size()),
                              () -> Assertions.assertEquals(DATETIME, result.get(EST)),
                              () -> Assertions.assertEquals(DATETIME, result.get(UTC)) );
    }

}
