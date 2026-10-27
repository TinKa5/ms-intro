package com.msintro.resource_service.contract;


import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msintro.resource_service.dto.IdResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Provider("resource-service")
@PactFolder("pacts")
class ResourceKafkaProviderPactTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @PactVerifyProvider("a resource created event")
    MessageAndMetadata resourceCreatedEvent() throws Exception {
        IdResponseDto dto = new IdResponseDto(102L);

        byte[] payload = objectMapper.writeValueAsBytes(dto);

        return new MessageAndMetadata(
                payload,
                Map.of(
                        "contentType", "application/json",
                        "kafka_topic", "resource-topic"
                )
        );
    }
}