package com.bringback.yourlink.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class YourLink {
    private String url;
    private List<String> tags;
    private boolean read;

    public YourLink(String url) {
        this.url = url;
    }
}
