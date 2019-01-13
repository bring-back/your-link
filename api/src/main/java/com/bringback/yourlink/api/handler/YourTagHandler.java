package com.bringback.yourlink.api.handler;

import com.bringback.yourlink.api.model.YourLink;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class YourTagHandler {
    public Mono<ServerResponse> getYourTags(ServerRequest request) {
        List<YourLink> dummyList = new ArrayList<>();
        dummyList.add(new YourLink("http://your.tags.com"));

        Flux<YourLink> yourLinkFlux = Flux.fromIterable(dummyList);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(yourLinkFlux, YourLink.class);
    }
}
