package com.bringback.yourlink.api.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {
  String URL;
  List<String> tags;
  boolean read;
}
