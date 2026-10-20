package com.msintro.resource_processor.servises;

import com.msintro.resource_processor.clients.ResourceRestClient;
import com.msintro.resource_processor.clients.SongRestClient;
import com.msintro.resource_processor.dto.IdResponseDto;
import com.msintro.resource_processor.dto.MetadataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceKafkaListener {

    private final ResourceService resourceService;
    private final ResourceRestClient resourceRestClient;
    private final SongRestClient songRestClient;

    @KafkaListener(topics = "resource-topic")
    private void handleResource(@Payload IdResponseDto id) {
        ResponseEntity<byte[]> recourseResponse = resourceRestClient.getResource(id.getId());
        MetadataDto metadataDto = resourceService.process(recourseResponse.getBody());
        metadataDto.setId(id.getId());
        songRestClient.saveMetadata(metadataDto);
    }
}
