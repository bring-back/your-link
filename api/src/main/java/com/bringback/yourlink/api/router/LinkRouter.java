package com.bringback.yourlink.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bringback.yourlink.api.handler.LinkHandler;

@Component
public class LinkRouter {

  private final LinkHandler linkHandler;

  @Autowired
  public LinkRouter(LinkHandler linkHandler) {
    this.linkHandler = linkHandler;
  }

  public RouterFunction<ServerResponse> routeLink() {

    return route(RequestPredicates
                   .POST("/link/save")
                   .and(accept(MediaType.APPLICATION_JSON)),
                 linkHandler::saveLink);
  }

}
