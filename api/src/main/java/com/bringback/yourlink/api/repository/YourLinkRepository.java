package com.bringback.yourlink.api.repository;

import com.bringback.yourlink.api.support.elasticsearch.ElasticSearchClient;
import com.bringback.yourlink.api.model.YourLink;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Repository
public class YourLinkRepository {

    private final RestHighLevelClient elasticSearchClient;

    @Autowired
    public YourLinkRepository(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    private ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
        @Override
        public void onResponse(IndexResponse indexResponse) {
            log.debug("SUCCESS: " + indexResponse.toString());
        }

        @Override
        public void onFailure(Exception e) {
            log.error("Fail to save yourLink", e);
        }
    };

    public void saveYourLink(String userId, Mono<YourLink> yourLinkMono) {
        yourLinkMono
                .log()
                .map(yourLink -> {
                    try {
                        return new IndexRequest(yourLinkIndex(userId), "doc", yourLink.getUrl())
                                .source(XContentFactory.jsonBuilder()
                                        .startObject()
                                        .field("url").value(yourLink.getUrl())
                                        .field("tags").value(yourLink.getTags())
                                        .field("read").value(yourLink.isRead())
                                        .endObject());
                    } catch (IOException e) {
                        log.error("fail to make IndexRequest", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .subscribe(request -> elasticSearchClient.indexAsync(request, ElasticSearchClient.COMMON_OPTIONS, listener));
    }

    private String yourLinkIndex(String userId) {
        return "yourlink_" + userId;
    }
}
