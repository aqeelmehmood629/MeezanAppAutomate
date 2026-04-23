@MyAccounts
Feature: Verify Account

  Scenario: View Customer Accounts
    Given user stores account title from dashboard
    When user clicks on side menu
    And user clicks on My Accounts
    And user verifies account title with dashboard
    Then user click home icon after view Account