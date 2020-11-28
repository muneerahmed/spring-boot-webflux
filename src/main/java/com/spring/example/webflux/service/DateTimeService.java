package com.spring.example.webflux.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * Terminology
 *
 * 	• Design Patterns : Observer able or Producers, Observer or Subscribers and Iterator
 *
 * 	• Publisher
 * 	• Subscriber
 * 	• Subscription
 * 	• Processor
 *
 * 	• Mono
 * 	• Flux
 * 	• Operators
 * 	• Pure Functions
 * 	• Pipeline
 *
 * 	• Mono or Flux is just a function which iterates over the data and push the value to Observer and returns Subscription so that consumer can say “i don't need more data”.
 * 	• Basically convert the data producer to an Observable
 * 	• Data producer can be anything for eg: http call, database call etc.
 * 	• In Short we need to find data sources API which returns observable rather than we doing ourselves as its not a trivial task.
 * 	• Once we get observable consumer need to subscribe to it in order for the data producer to start producing values. Hence we need to subscribe to the observable.
 * 	• Web Client converts HTTP Call to an observable either Mono or Flux
 *
 * 	In nutshell
 * 	• An Observable is just a function , consumer call it by subscribing to it and then observable or producer will start producing values.
 *
 * 	References:
 * 	• WebClient tutorial https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
 * 	• How to Create Mono/Flux https://medium.com/@cheron.antoine/reactor-java-1-how-to-create-mono-and-flux-471c505fa158
 */

@Service
public class DateTimeService {

    static final String EST  = "est";
    static final String UTC  = "utc";
    static final String CURRENT_DATETIME = "currentDateTime";
    static final String URL = "http://worldclockapi.com/api/json/%s/now";

    DateTimeService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    public Mono<Map> getCurrentDateTimes(List<String> timezones) {

        return Mono.zip(
                timezones.stream()
                        .map(this::getWorldTime)
                        .collect(Collectors.toList() )
                        , this::convert);
    }

    private Mono<Map<String, Object>> getWorldTime(String timezone) {
        return webClient.get()
                .uri(String.format(URL, timezone))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .map(e -> Map.of(timezone, e.get(CURRENT_DATETIME)))
                .onErrorReturn(Map.of(timezone, "N/A"))
                .log();
    }

    /**
     * Convert a List of Map to Map
     *
     * @param input
     *
     * @return
     */
    private Map convert(Object[] input) {
        Map<Object, Object> result = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            Map temp = (Map) input[i];
            temp.keySet().forEach(k -> result.put(k, temp.get(k)));
        }
        return result;
    }


}
