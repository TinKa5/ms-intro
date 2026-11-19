package com.msintro.resource_service.services;

import com.msintro.resource_service.dto.Status;
import com.msintro.resource_service.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusKafkaListener {

    private final ResourceService resourceService;


    @KafkaListener(topics = "${kafka.topic.status}")
    public void handleStatus(@Payload StatusResponseDto statusResponseDto){
        if (statusResponseDto.getStatus().equals(Status.Success)){
            resourceService.updateStatus(statusResponseDto.getId());
        }
    }

}
