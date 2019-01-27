package com.bringback.yourlink.api.kafka.deserializers;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.bringback.yourlink.api.model.Link;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LinkDeserializer implements Deserializer {

  @Override
  public void configure(Map configs, boolean isKey) {

  }

  @Override
  public Object deserialize(String topic, byte[] data) {
    ObjectMapper mapper = new ObjectMapper();
    Link link = null;

    try {
      link = mapper.readValue(topic, Link.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return link;
  }

  @Override
  public void close() {

  }
}
