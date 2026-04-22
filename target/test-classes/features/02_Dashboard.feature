Feature: Dashboard Validation

  @Dashboard
  Scenario: Navigate to Dashboard
    Given user is logged in and on dashboard
    When user clicks on Show Balance
    Then user should see updated balance