package com.spring.example.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

class TimeService {

    TimeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    final RestTemplate restTemplate;
    static final String CURRENT_DATETIME = "currentDateTime";
    static final String URL = "http://worldclockapi.com/api/json/%s/now";


    Map<String, Object> getCurrentDateTimes(List<String> timezones) {
        Map<String, Object> result = new HashMap<>();
        for (String timezone : timezones) {
            result.put(timezone, get(String.format(URL, timezone)));
        }
        return result;
    }

    Object get(String uri) {
        return restTemplate.getForEntity(uri, Map.class).getBody().get(CURRENT_DATETIME);
    }

}