package com.spring.example.web.controller;

import com.intuit.karate.junit5.Karate;

/**
 * mvn test -Dtest=time
 */

public class TimeRunner {

    @Karate.Test
    Karate testTime() {
        return Karate.run("time").relativeTo(getClass());
    }

}