Feature: Timezones Datetime Test

  Background:
    * url $url

  Scenario: Success - Get current datetime for est and utc timezones
    Given path '/web-datetime'
    And param timezones = 'est,utc'
    When method get
    Then status 200
    Then match $ contains { est: '#string', utc: '#string' } // $ represents the response

  Scenario: Bad Request - Get current datetime for est and utc timezones
    Given path '/web-datetime'
    When method get
    Then status 400
    Then match $ == {"fieldErrors":[{"field":"timeZones query parameter is mandatory","message":"timeZones query parameter is mandatory"}]}