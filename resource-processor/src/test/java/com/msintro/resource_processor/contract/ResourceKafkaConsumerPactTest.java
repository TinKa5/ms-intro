package com.msintro.resource_processor.contract;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(
        providerName = "resource-service",
        providerType = au.com.dius.pact.consumer.junit5.ProviderType.ASYNCH
)
public class ResourceKafkaConsumerPactTest {

    @Pact(consumer = "resource-processor")
    public V4Pact resourceCreatedMessage(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.integerType("id", 102);

        return builder
                .expectsToReceive("a resource created event")
                .withMetadata(Map.of(
                        "contentType", "application/json",
                        "kafka_topic", "resource-topic"
                ))
                .withContent(body)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(
            pactMethod = "resourceCreatedMessage",
            providerType = au.com.dius.pact.consumer.junit5.ProviderType.ASYNCH
    )
    void shouldCreateResourceMessageContract(V4Pact pact) {
        assertThat(pact).isNotNull();
        assertThat(pact.getInteractions()).hasSize(1);
    }
}
