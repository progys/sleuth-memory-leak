package com.example.sleuthmemoryleak;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class LeakExampleController {

    @MessageMapping("message")
    public Mono<String> someEndpoint(String request) {
        return Mono.just("data from server");
    }
}
