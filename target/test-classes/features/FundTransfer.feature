Feature: Fund Transfer

@03_FundTransfer
Scenario: Transfer funds to Meezan Bank account
    Given user navigate to send money
    When user selects account to transfer
    And user hides keyboard
    And user clicks purpose of transfer dropdown
    And user enters purpose of transfer
    And user selects purpose of transfer dynamically
    And user enters amount
    And user clicks Next button
    And user clicks Send Now button
    Then transaction should be successful