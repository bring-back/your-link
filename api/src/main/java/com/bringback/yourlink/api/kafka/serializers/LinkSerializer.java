package com.bringback.yourlink.api.serializers;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;


public class LinkSerializer implements Serializer {

  @Override
  public void configure(Map configs, boolean isKey) {

  }

  @Override
  public byte[] serialize(String topic, Object data) {
    byte[] retVal = null;
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      retVal = objectMapper.writeValueAsString(topic).getBytes();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return retVal;
  }

  @Override public void close() {

  }


}
