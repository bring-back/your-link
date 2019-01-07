package com.bringback.yourlink.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YourLink {
    private String url;
    private boolean read;

    public YourLink(String url) {
        this.url = url;
    }
}
