Feature: Company Profile delete
  Scenario: send DELETE request to the api
    Given the application is running
    When the consumer receives a delete payload
    Then a DELETE request is sent to the company profile api

  Scenario: send DELETE with invalid JSON
    Given the application is running
    When the consumer receives an invalid delete payload
    Then the message should be moved to topic company-profile-delta-invalid

  Scenario: send DELETE with 400 from data api
  Given the application is running
  When the consumer receives a delete message but the api returns a 400
  Then the message should be moved to topic company-profile-delta-invalid

  Scenario Outline: send DELETE with retryable response from api
    Given the application is running
    When the consumer receives a delete message but the api returns a <code>
    Then the message should retry 3 times and then error

  Examples:
  | code |
  | 404  |
  | 503  |