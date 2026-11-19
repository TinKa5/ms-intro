package com.msintro.resource_processor.services;

import com.msintro.resource_processor.dto.Status;
import com.msintro.resource_processor.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class KafkaStatusProducer {

    private final KafkaTemplate<String, StatusResponseDto> kafkaTemplate;

    @Value("${kafka.topic.status}")
    private String topic;

    public void sendStatus(Status status, Long id) {
        try {
            kafkaTemplate.send(topic, new StatusResponseDto(status, id)).get();
        } catch (Exception e){
            throw new RuntimeException("Kafka exception during sending status");
        }
    }
}
