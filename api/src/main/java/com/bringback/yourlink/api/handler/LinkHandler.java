package com.bringback.yourlink.api.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bringback.yourlink.api.kafka.SampleProducer;
import com.bringback.yourlink.api.model.Link;
import com.bringback.yourlink.api.model.YourLink;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class LinkHandler {

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

    Flux<String> linkFlux = Flux.just("Success");

    SampleProducer producer = new SampleProducer("localhost:9092");

    String TOPIC = "test-your-link";
    CountDownLatch latch = new CountDownLatch(1);

    try {
      producer.sendMessages(TOPIC, 1, latch);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return ServerResponse
      .ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(linkFlux, String.class);
  }
}
