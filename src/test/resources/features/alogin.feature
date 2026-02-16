Feature: Meezan Bank Login

  @OBDX_SC_01
  Scenario: Login with valid credentials
    Given the Meezan Bank app is launched
    When user enters username "Aiman23" and password "Admin@786"
    And user taps on login button
    Then user should be logged in successfully