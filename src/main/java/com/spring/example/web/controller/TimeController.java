package com.spring.example.web.controller;

import com.spring.example.web.service.TimeService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * curl -v http://localhost:8080/web-datetime?timezones=est,utc
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TimeController {

    private final TimeService service;

    @GetMapping(value = "/web-datetime", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getCurrentDateTime(@RequestParam(value = "timezones", required = false) List<String> timezones) {
        log.debug("Received a request for /datetime with query parameter timezones={}", timezones);
        if(CollectionUtils.isEmpty(timezones)) {
            throw new IllegalArgumentException("timeZones query parameter is mandatory");
        }
        return service.getCurrentDateTimes(timezones);
    }
}
