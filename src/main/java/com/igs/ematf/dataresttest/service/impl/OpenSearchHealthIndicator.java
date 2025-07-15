package com.igs.ematf.dataresttest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OpenSearchHealthIndicator implements HealthIndicator {
    private final OpenSearchClient client;

    public OpenSearchHealthIndicator(@Qualifier("openSearchClient2_8_1") OpenSearchClient client) {
        this.client = client;
    }

    @Override
    public Health health() {
        try {
            BooleanResponse ping = client.ping();
            if (ping.value()) {
                return Health.up().build();
            }
            return Health.down().build();
        } catch (IOException e) {
            log.error("Error in health check:{}", e.getMessage());
            return Health.down(e).build();
        }
    }
}