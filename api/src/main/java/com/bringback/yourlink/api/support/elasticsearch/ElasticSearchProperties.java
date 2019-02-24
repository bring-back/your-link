package com.bringback.yourlink.api.support.elasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {
    private List<String> hosts;
}
