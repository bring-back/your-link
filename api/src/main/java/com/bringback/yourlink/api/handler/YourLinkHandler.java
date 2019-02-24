package com.bringback.yourlink.api.handler;

import com.bringback.yourlink.api.model.YourLink;
import com.bringback.yourlink.api.repository.YourLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class YourLinkHandler {
    private final YourLinkRepository yourLinkRepository;

    @Autowired
    public YourLinkHandler(YourLinkRepository yourLinkRepository) {
        this.yourLinkRepository = yourLinkRepository;
    }

    public Mono<ServerResponse> saveYourLink(ServerRequest request) {
        // TODO need to change after logged-in user email
        List<String> userIdHeader = request.headers().header("x-yourlink-userid");
        String userId = userIdHeader.get(0);

        Mono<YourLink> yourLinkMono = request.bodyToMono(YourLink.class);
        yourLinkRepository.saveYourLink(userId, yourLinkMono);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.empty(), Void.class);
    }

    public Mono<ServerResponse> getYourLinks(ServerRequest request) {
        List<YourLink> dummyList = new ArrayList<>();
        dummyList.add(new YourLink("http://naver.com"));

        Flux<YourLink> yourLinkFlux = Flux.fromIterable(dummyList);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(yourLinkFlux, YourLink.class);
    }
}
