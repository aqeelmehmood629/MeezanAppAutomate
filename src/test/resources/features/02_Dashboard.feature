Feature: Dashboard Validation
  
  @DSB_TC01 @NeedsDashboard @NeedsLogin
  Scenario: Navigate to Dashboard
   Given user is logged in
   And user clicks on Show Balance
   Then user should see updated balance
   
   
  @DSB_TC02 @NeedsLogin @Dashboard
  Scenario: Verify account details are displayed correctly
    Given user is on account details screen
    When Title should be displayed
    And Current Account should be displayed
    And IBAN should be displayed
    And Branch should be displayed
    And Balance should be displayed
    And Share option should be displayed
    Then Transaction view option should be displayed
    
  @DSB_TC03 @NeedsLogin @Dashboard
  Scenario Outline: Copy account details from account screen
    Given user click on share details screen
    When user clicks on "<copy_action>"
    Then "<expected_result>" should be copied to clipboard


  Examples:
  | copy_action              | expected_result              |
  | Copy Account Number      | account number               |
  | Copy IBAN Number         | IBAN                         |
  | Copy Account Details     | full account details         |

    
 @DSB_TC04 @Dashboard @NeedsLogin
 Scenario Outline: Share account details from account screen
  Given user clicks on share icon
  When user selects share action "<share_action>"
  Then "<expected_result>" should be shared successfully

 Examples:
 | share_action            | expected_result       |
 | Share Account Number    | account number        |
 | Share IBAN Number       | IBAN                  |
 | Share Account Details   | full account details  |


  @DSB_TC05 @Dashboard @NeedsLogin
  Scenario: Multiple rapid taps on refresh button
    Given user rapidly taps on refresh button multiple times
    When application should remain stable
    Then data is correctly updated without crashes
   
  @DSB_TC06 @Dashboard @NeedsLogin
  Scenario: View account transaction history
   When user taps on View Transaction History
   Then transaction history screen should be displayed
   

  @DSB_TC07 @Dashboard @NeedsLogin @Regression
  Scenario: Mark account as favourite using account number from CSV
    Given user reads target account number from CSV
    When user searches for the account card by swiping if needed
    And user clicks the favourite star for the target account
    Then favourite confirmation popup should appear
    And user confirms the favourite action
    Then account should be marked as favourite
