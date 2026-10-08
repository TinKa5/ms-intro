package com.msintro.resource_service.services;

import com.msintro.resource_service.dto.IdListResponseDto;
import com.msintro.resource_service.dto.IdResponseDto;
import com.msintro.resource_service.dto.MetadataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SongRestClient {
    private final RestClient restClient;

    SongRestClient(RestClient.Builder builder, @Value("${song-service.url}") String url) {
        restClient = builder.baseUrl(url).build();
    }

    public IdResponseDto saveMetadata(MetadataDto metadata) {
        return restClient
                .post()
                .uri("/songs")
                .body(metadata)
                .retrieve()
                .body(IdResponseDto.class);
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
