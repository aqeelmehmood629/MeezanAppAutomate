
Feature: Verify Account
 
  @MyAccounts
  Scenario: View Customer Accounts
    Given user is logged in for view Accounts
    When user stores account title from dashboard
    And user clicks on side menu
    And user clicks on My Accounts
    And user verifies account title with dashboard
    Then user click home icon after view Account