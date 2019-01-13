package com.bringback.yourlink.api.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
public class RootRouter {

    private final YourLinkRouter yourLinkRouter;

    private final YourTagRouter yourTagRouter;

    @Autowired
    public RootRouter(YourLinkRouter yourLinkRouter, YourTagRouter yourTagRouter) {
        this.yourLinkRouter = yourLinkRouter;
        this.yourTagRouter = yourTagRouter;
    }

    @Bean
    public RouterFunction<ServerResponse> routeAPIs() {
        return nest(RequestPredicates.path("/api/v1"),
                yourLinkRouter.routeYourLink()
                        .and(yourTagRouter.routeYourTag()));
    }
}
