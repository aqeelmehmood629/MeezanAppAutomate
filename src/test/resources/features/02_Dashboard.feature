Feature: Dashboard Validation
  
  @Dashboard
  Scenario: Navigate to Dashboard
   Given user is logged in
   And user clicks on Show Balance
   Then user should see updated balance