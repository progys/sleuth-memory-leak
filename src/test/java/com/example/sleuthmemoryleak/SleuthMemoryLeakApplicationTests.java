package com.example.sleuthmemoryleak;

import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SleuthMemoryLeakApplicationTests {
    @Autowired
    private RSocketRequester.Builder builder;
    @LocalServerPort
    Integer port;

    RSocketRequester requester;

    @BeforeEach
    void setUp() {
        Hooks.onErrorDropped(e -> {
            //IGNORE: silences this https://github.com/rsocket/rsocket-java/issues/1018
        });
        requester = builder
                .transport(WebsocketClientTransport.create(URI.create("http://localhost:" + port + "/rsocket")));
    }

    @AfterEach
    void tearDown() {
        if (!requester.isDisposed()) {
            requester.dispose();
        }
    }

    @Timeout(5)
    @Test
    void leaksMemory() {
        Mono<String> result = requester.route("message")
                .data("{}")
                .retrieveMono(String.class);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNext("data from server")
                .then(System::gc)
                .verifyComplete();
    }

}
