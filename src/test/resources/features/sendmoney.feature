@Transfer funds to Meezan Bank account(OBDX_SC_05)
Feature: Fund Transfer functionality

  Background:
    Given the Meezan Bank app is launched
    And user is logged in with username "Aiman23" and password "Admin@786"
    
  (OBDX_SC_05)
  Scenario: Transfer funds to Meezan Bank account
    Given user navigate to send money
    When user selects account to transfer
    And user hides keyboard
    And user clicks purpose of transfer dropdown
    And user enters purpose of transfer "Hotel"
    And user selects purpose of transfer dynamically "Hotel"
    And user enters amount "100"
    And user clicks Next button
    And user clicks Send Now button
    Then transaction should be successful
