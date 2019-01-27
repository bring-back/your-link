package com.bringback.yourlink.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bringback.yourlink.api.handler.YourLinkHandler;

@Component
public class YourLinkRouter {

    private final YourLinkHandler yourLinkHandler;

    @Autowired
    public YourLinkRouter(YourLinkHandler yourLinkHandler) {
        this.yourLinkHandler = yourLinkHandler;
    }

    public RouterFunction<ServerResponse> routeYourLink() {
        return route(RequestPredicates
                        .GET("/your/links")
                        .and(accept(MediaType.APPLICATION_JSON)),
                yourLinkHandler::getYourLinks);
    }
}
