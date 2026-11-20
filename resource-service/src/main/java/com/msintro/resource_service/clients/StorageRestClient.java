package com.msintro.resource_service.clients;

import com.msintro.resource_service.dto.StorageDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
public class StorageRestClient {

    private RestClient restClient;

    public StorageRestClient(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder,
                             @Value("${storage-service.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    @CircuitBreaker(name = "storageService", fallbackMethod = "getAllFallback")
    public List<StorageDto> getAll(){
        ResponseEntity<StorageDto[]> response =
                restClient.get().uri("/storages").retrieve().toEntity(StorageDto[].class);
        return !response.getStatusCode().isError() && response.getBody() != null
                ? Arrays.asList(response.getBody()) : List.of();
    }

    private List<StorageDto> getAllFallback(Throwable throwable) {
        return List.of(
                new StorageDto(null, "STAGING", "staging-bucket", "/staging-fallback"),
                new StorageDto(null,"PERMANENT", "permanent-bucket", "/permanent-fallback"));
    }
}
