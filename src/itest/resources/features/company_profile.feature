Feature: Company Profile delta
  Scenario: Can transform and send a company profile
    Given the application is running
    When the consumer receives a message
    Then a PUT request is sent to the company profile api with the encoded data

  Scenario: Process invalid avro message
    Given the application is running
    When an invalid avro message is sent
    Then the message should be moved to topic company-profile-delta-invalid

  Scenario: Process message with invalid data
    Given the application is running
    When a message with invalid data is sent
    Then the message should be moved to topic company-profile-delta-invalid

  Scenario: Process message when the api returns 400
    Given the application is running
    When the consumer receives a message but the api returns a 400
    Then the message should be moved to topic company-profile-delta-invalid

  Scenario: Process message when the api returns 503
    Given the application is running
    When the consumer receives a message but the api returns a 503
    Then the message should retry 3 times and then error