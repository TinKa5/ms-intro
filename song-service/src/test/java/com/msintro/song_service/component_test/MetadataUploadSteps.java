package com.msintro.song_service.component_test;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataUploadSteps {

    private static final String VALID_METADATA = "{\n" +
            "    \"id\": 102,\n" +
            "    \"name\": \"We are the champions\",\n" +
            "    \"artist\": \"Queen\",\n" +
            "    \"album\": \"News of the world\",\n" +
            "    \"duration\": \"02:59\",\n" +
            "    \"year\": \"1999\"\n" +
            "}";
    private static final String INVALID_METADATA = "{\n" +
            "    \"id\": 103,\n" +
            "    \"artist\": \"Queen\",\n" +
            "    \"album\": \"News of the world\",\n" +
            "    \"duration\": \"02:59\",\n" +
            "    \"year\": \"1999\"\n" +
            "}";
    private static final String DETAILS = "Song name is required";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private ResponseEntity<String> response;
    private String uploadingData;

    @Given("the song service is running")
    public void serviceIsRunning() {
    }

    @Given("I have a valid metadata")
    public void setValidMetadata() {
        uploadingData = VALID_METADATA;
        assertTrue(VALID_METADATA.length() > 0 );
    }

    @Given("I have a metadata without Name field")
    public void setInvalidMetadata() {
        uploadingData = INVALID_METADATA;
        assertFalse(INVALID_METADATA.contains("name") );
    }

    @When("I upload the metadata")
    public void uploadFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<String> request =
                new HttpEntity<>(uploadingData, headers);

        response = restTemplate.postForEntity(
                "/songs", request, String.class
        );
    }

    @Then("the response status should be {int}")
    public void checkStatus(int expectedStatus) {

        int actualStatus = response.getStatusCode().value();

        assertEquals(
                expectedStatus,
                actualStatus
        );
    }

    @Then("the response should contain a metadata ID")
    public void checkMetadataId() throws IOException {
        assertNotNull(response.getBody());

        JsonNode body = objectMapper.readTree(
                response.getBody()
        );

        assertTrue(
                body.hasNonNull("id"),
                "Response must contain a non-null id"
        );
    }

    @Then("the body should contain details about Name field missing")
    public void checkResponse() throws IOException {
        System.out.println(response.getBody());
        assertNotNull(response.getBody());

        JsonNode body = objectMapper.readTree(
                response.getBody()
        );

        assertEquals(
                DETAILS,
                body.findPath("details").findPath("name").textValue()
        );
    }

}
