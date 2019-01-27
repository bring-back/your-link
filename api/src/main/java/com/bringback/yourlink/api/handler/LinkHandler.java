package com.bringback.yourlink.api.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bringback.yourlink.api.kafka.LinkConsumer;
import com.bringback.yourlink.api.kafka.LinkProducer;
import com.bringback.yourlink.api.model.Link;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class LinkHandler {

  @Autowired
  private LinkProducer linkProducer;

  @Autowired
  private LinkConsumer linkConsumer;

  public Mono<ServerResponse> saveLink(ServerRequest request) {
    List<Link> dummyLinkList = new ArrayList<>();

    Link link = new Link();
    link.setRead(false);

    List<String> tags = new ArrayList<String>();
    tags.add("git");
    tags.add("bring-back");
    tags.add("your-link");
    link.setTags(tags);

    link.setURL("https://github.com/bring-back/your-link");

    dummyLinkList.add(link);

    // produce
    linkProducer.sendMessages(dummyLinkList.size(), dummyLinkList);

    // consume
    Disposable disposable = linkConsumer.consumeMessages(dummyLinkList.size());
    disposable.dispose();

    Flux<Link> linkFlux = Flux.fromIterable(dummyLinkList);

    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(linkFlux, Link.class);
  }
}
