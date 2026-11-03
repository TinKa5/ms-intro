package com.msintro.e2e.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AudioUploadSteps {


    private byte[] audioData;
    private Response response;

    @Given("a valid MP3 file")
    public void uploadAudio() throws IOException {
        audioData = Files
                .readAllBytes(
                        Path.of("src/test/resources/files/valid-sample-with-required-tags.mp3")
                );
        assertTrue(audioData.length > 0);
    }

    @When("I upload the audio file")
    public void createRequest() {
        response = given()
                .baseUri("http://localhost:8081")
                .contentType("audio/mpeg")
                .body(audioData)
                .when()
                .post("/resources");
    }

    @Then("the upload should succeed")
    public void uploadShouldSucceed() {
        assertTrue(response.statusCode() == 200 || response.statusCode() == 201,
                "Unexpected response: " + response.asString());
    }

    @Then("the response should contain a resource ID")
    public void uploadShouldContainId() {
        Integer id = response.jsonPath().getObject("id", Integer.class);

        assertTrue(id != null && id > 0,
                "Response should contain a valid resource ID");
    }
}
