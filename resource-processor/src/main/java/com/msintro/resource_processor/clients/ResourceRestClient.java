package com.msintro.resource_processor.clients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ResourceRestClient {

    private RestClient restClient;

    public ResourceRestClient(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder,
            @Value("${resource-service.url}") String url) {

        restClient = builder.clone().baseUrl(url).build();
    }

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2,
                    maxDelay = 5000
            )
    )
    public ResponseEntity<byte[]> getResource(Long id) {
        System.out.println("GGGGG_id = " + id);
        return restClient.get()
                .uri("/resources/{id}", id)
                .header("Accept", "audio/mpeg")
                .retrieve()
                .toEntity(byte[].class);
    }

}
