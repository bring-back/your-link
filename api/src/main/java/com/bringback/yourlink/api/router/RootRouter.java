package com.bringback.yourlink.api.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RootRouter {

  private final YourLinkRouter yourLinkRouter;

  private final YourTagRouter yourTagRouter;

  private final LinkRouter linkRouter;

  @Autowired
  public RootRouter(YourLinkRouter yourLinkRouter, YourTagRouter yourTagRouter, LinkRouter linkRouter) {
    this.yourLinkRouter = yourLinkRouter;
    this.yourTagRouter = yourTagRouter;
    this.linkRouter = linkRouter;
  }

  @Bean
  public RouterFunction<ServerResponse> routeAPIs() {
    return nest(RequestPredicates.path("/api/v1"),
                yourLinkRouter.routeYourLink()
                              .and(yourTagRouter.routeYourTag())
                              .and(linkRouter.routeLink()));
  }
}
