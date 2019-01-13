package com.bringback.yourlink.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bringback.yourlink.api.handler.LinkHandler;

@Configuration
public class LinkRouter {

  @Bean
  public RouterFunction<ServerResponse> routeLink(LinkHandler linkHandler) {

    return route(RequestPredicates
                   .POST("/api/v1/your/link/save")
                   .and(accept(MediaType.APPLICATION_JSON)),
                 linkHandler::saveLink);
  }

}
