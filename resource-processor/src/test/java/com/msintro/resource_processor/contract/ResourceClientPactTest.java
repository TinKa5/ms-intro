package com.msintro.resource_processor.contract;


import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "test_provider", hostInterface = "localhost")
public class ResourceClientPactTest {
    private static final byte[] AUDIO = {
            0x01, 0x02, 0x03, 0x04
    };


    @Pact(consumer = "test_consumer")
    public V4Pact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "audio/mpeg");

        return builder
                .given("resource 102 exists")
                .uponReceiving("a request for audio resource 102")
                .path("/resources/102")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .withBinaryData(AUDIO, "audio/mpeg")
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor
    public void givenGet_whenSendRequest_shouldReturn200WithProperHeader(MockServer mockProvider) {

        // when

        ResponseEntity<byte[]> response = new RestTemplate()
                .getForEntity(mockProvider.getUrl() + "/resources/102", byte[].class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().get("Content-Type").contains("audio/mpeg")).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(AUDIO);
    }
}
