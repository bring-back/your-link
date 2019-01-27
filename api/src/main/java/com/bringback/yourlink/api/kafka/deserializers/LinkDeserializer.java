package com.bringback.yourlink.api.kafka.deserializers;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.bringback.yourlink.api.model.Link;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LinkDeserializer implements Deserializer {

  @Override
  public void configure(Map configs, boolean isKey) {

  }

  @Override
  public Object deserialize(String topic, byte[] data) {
    ObjectMapper mapper = new ObjectMapper();
    Link user = null;

    try {
      user = mapper.readValue(topic, Link.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return user;
  }

  @Override
  public void close() {

  }
}
