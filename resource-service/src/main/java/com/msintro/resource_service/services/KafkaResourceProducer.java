package com.msintro.resource_service.services;

import com.msintro.resource_service.dto.IdResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaResourceProducer {

    private final KafkaTemplate<String, IdResponseDto> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2,
                    maxDelay = 5000
            )
    )
    public CompletableFuture sendMessage(Long id) {
        return kafkaTemplate.send(topic, new IdResponseDto(id));
    }

}
