package com.spring.example.web;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/web-datetime")
    public Map getCurrentDateTime(@RequestParam("timezones") List<String> timezones) {
        log.debug("Received a request for /datetime with query parameter timezones={}", timezones);
        return service.getCurrentDateTimes(timezones);
    }
}
