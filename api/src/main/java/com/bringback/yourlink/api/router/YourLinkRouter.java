package com.bringback.yourlink.api.router;

import com.bringback.yourlink.api.handler.YourLinkHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class YourLinkRouter {
    @Bean
    public RouterFunction<ServerResponse> routeYourLink(YourLinkHandler yourLinkHandler) {

        return route(RequestPredicates
                        .GET("/your/links")
                        .and(accept(MediaType.APPLICATION_JSON)),
                yourLinkHandler::getYourLinks);
    }
}
