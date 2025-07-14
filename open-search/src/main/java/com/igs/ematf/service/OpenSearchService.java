package com.igs.ematf.service;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class OpenSearchService {

    @Autowired
    private OpenSearchClient client;

    public boolean createIndex(String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index(indexName)
                .settings(s -> s
                        .numberOfShards("1")
                        .numberOfReplicas("0")
                )
                .build();

        CreateIndexResponse response = client.indices().create(request);
        return response.acknowledged();
    }


    public void indexDocument(String indexName, String id, Object document) throws IOException {
        IndexRequest<Object> request = new IndexRequest.Builder<>()
                .index(indexName)
                .id(id)
                .document(document)
                .build();

        IndexResponse response = client.index(request);
        System.out.println("Document ID: " + response.id());
    }
}