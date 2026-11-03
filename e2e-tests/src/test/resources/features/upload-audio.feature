Feature: Upload audio resource

  Scenario: Upload a valid MP3 file
    Given a valid MP3 file
    When I upload the audio file
    Then the upload should succeed
    And the response should contain a resource ID