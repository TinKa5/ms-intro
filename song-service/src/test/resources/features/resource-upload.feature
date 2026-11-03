Feature: Metadata upload

  Scenario: Successfully upload metadata
    Given the song service is running
    And I have a valid metadata
    When I upload the metadata
    Then the response status should be 200
    And the response should contain a metadata ID

  Scenario: Reject metadata without Name field
    Given the song service is running
    And I have a metadata without Name field
    When I upload the metadata
    Then the response status should be 400
    And the body should contain details about Name field missing