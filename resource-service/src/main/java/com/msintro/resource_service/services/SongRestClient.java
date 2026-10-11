package com.msintro.resource_service.services;

import com.msintro.resource_service.dto.IdListResponseDto;
import com.msintro.resource_service.dto.IdResponseDto;
import com.msintro.resource_service.dto.MetadataDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<IdResponseDto> saveMetadata(MetadataDto metadata) {
        return restClient
                .post()
                .uri("/songs")
                .body(metadata)
                .retrieve()
                .toEntity(IdResponseDto.class);
    }

    public IdListResponseDto deleteMetadata(String paramsString) {
        return restClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/songs")
                        .queryParam("id", paramsString)
                        .build())
                .retrieve()
                .body(IdListResponseDto.class);
    }

}
