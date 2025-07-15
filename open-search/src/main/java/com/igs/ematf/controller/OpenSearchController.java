package com.igs.ematf.controller;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/open-search")
@RequiredArgsConstructor
public class OpenSearchController {

    final private OpenSearchClient searchClient;

    @RequestMapping("/health")
    public BooleanResponse health() {
        try {
            return searchClient.ping();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}