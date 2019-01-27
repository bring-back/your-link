package com.bringback.yourlink.api.kafka;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.bringback.yourlink.api.kafka.deserializers.LinkDeserializer;
import com.bringback.yourlink.api.model.Link;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.IntegerDeserializer;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Configuration
public class LinkConsumer {

  private static final Logger log = LoggerFactory.getLogger(LinkConsumer.class.getName());

  private static final String bootstrapServers = "localhost:9092";
  private static final String topic = "link";

  private final ReceiverOptions<Integer, Link> receiverOptions;
  protected final SimpleDateFormat dateFormat;

  public LinkConsumer() {

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "link-consumer");
    // props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LinkDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    receiverOptions = ReceiverOptions.create(props);
    dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
  }

  public Disposable consumeMessages(int count) {

    CountDownLatch latch = new CountDownLatch(count);

    ReceiverOptions<Integer, Link> options = receiverOptions.subscription(Collections.singleton(topic))
                                                            .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                                                            .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

    Flux<ReceiverRecord<Integer, Link>> kafkaFlux = KafkaReceiver.create(options).receive();

    return kafkaFlux.subscribe(record -> {
      ReceiverOffset offset = record.receiverOffset();
      log.info("Received message: topic-partition={} offset={} timestamp=%{} key={} value={}\n",
                        offset.topicPartition(),
                        offset.offset(),
                        dateFormat.format(new Date(record.timestamp())),
                        record.key(),
                        record.value());
      offset.acknowledge();
      latch.countDown();
    });
  }

}
