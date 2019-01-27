package com.bringback.yourlink.api.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.bringback.yourlink.api.kafka.serializers.LinkSerializer;
import com.bringback.yourlink.api.model.Link;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

@Configuration
public class LinkProducer {

  private static final Logger log = LoggerFactory.getLogger(LinkProducer.class);

  // @Value(value = "${kafka.server}")
  private final String bootstrapServers = "localhost:9092";

  // @Value(value = "${kafka.topic.link}")
  private final String topic = "link";

  private final KafkaSender<Integer, Link> sender;
  private final SimpleDateFormat dateFormat;

  public LinkProducer() {

    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "link-producer");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LinkSerializer.class);
    SenderOptions<Integer, Link> senderOptions = SenderOptions.create(props);

    sender = KafkaSender.create(senderOptions);
    dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
  }

  /***
   * send link(meta info) messages
   * @param count
   * @param links
   * @throws InterruptedException
   */
  public void sendMessages(int count, List<Link> links) {

    CountDownLatch latch = new CountDownLatch(count);

    Flux<Link> linkFlux = Flux.fromIterable(links);

    sender.send(linkFlux.map(link -> SenderRecord.create(new ProducerRecord<>(topic, link.hashCode(), link), link.hashCode())))
          .doOnError(e -> log.error("Link message send failed", e))
          .subscribe(r -> {

            RecordMetadata metadata = r.recordMetadata();
            log.info("Message {} sent successfully, topic-partition={}-{} offset={} timestamp={}\n",
                     r.correlationMetadata(),
                     metadata.topic(),
                     metadata.partition(),
                     metadata.offset(),
                     dateFormat.format(new Date(metadata.timestamp())));

            latch.countDown();
          });
  }

  public void close() {
    sender.close();
  }
}