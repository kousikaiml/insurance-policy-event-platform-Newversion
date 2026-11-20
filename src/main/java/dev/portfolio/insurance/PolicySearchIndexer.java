package dev.portfolio.insurance;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.io.StringReader;

@Component
public class PolicySearchIndexer {
    private final ElasticsearchClient client;
    private final ObjectMapper mapper;
    public PolicySearchIndexer(ElasticsearchClient client, ObjectMapper mapper) {
        this.client = client; this.mapper = mapper;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "policy-search-indexer")
    public void onPolicyEvent(String message) throws Exception {
        JsonNode event = mapper.readTree(message);
        String policyNumber = event.path("policyNumber").asText();
        if (policyNumber.isBlank()) return;
        client.index(IndexRequest.of(i -> i.index("insurance-policies")
            .id(policyNumber).withJson(new StringReader(message))));
    }
}
