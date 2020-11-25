package com.spring.example.web;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * curl -v http://localhost:8080/web/datetime?timezones=est,utc
 *
 */
@RestController
@RequestMapping("/web/datetime")
public class TimeController {

    TimeController(TimeService service) {
        this.service = service;
    }

    TimeService service;

    @GetMapping
    public Map getCurrentDateTime(@RequestParam("timezones") List<String> timezones) {
        return service.getCurrentDateTimes(timezones);
    }
}
