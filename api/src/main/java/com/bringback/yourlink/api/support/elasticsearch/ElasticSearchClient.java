package com.bringback.yourlink.api.support.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchClient {
    private final ElasticSearchProperties elasticSearchProperties;

    @Autowired
    public ElasticSearchClient(ElasticSearchProperties elasticSearchProperties) {
        this.elasticSearchProperties = elasticSearchProperties;
    }

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient yourLinkEsClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        elasticSearchProperties.getHosts().stream()
                        .map(hostString -> {
                            String[] split = hostString.split(":");
                            if (split.length < 2) {
                                throw new IllegalArgumentException("elasticsearch.hosts need ip and port");
                            }

                            return new HttpHost(split[0], Integer.parseInt(split[1]), "http");
                        })
                        .map(Node::new).toArray(Node[]::new)
                )
        );
    }
}
