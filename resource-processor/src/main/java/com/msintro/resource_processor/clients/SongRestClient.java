package com.msintro.resource_processor.clients;

import com.msintro.resource_processor.dto.IdResponseDto;
import com.msintro.resource_processor.dto.MetadataDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SongRestClient {

    private final RestClient restClient;

    public SongRestClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder builder,
            @Value("${song-service.url}") String url) {

        this.restClient = builder.clone()
                .baseUrl(url)
                .build();
    }

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2,
                    maxDelay = 5000
            )
    )
    public ResponseEntity<IdResponseDto> saveMetadata(MetadataDto metadata) {
        return restClient
                .post()
                .uri("/songs")
                .body(metadata)
                .retrieve()
                .toEntity(IdResponseDto.class);
    }

}
