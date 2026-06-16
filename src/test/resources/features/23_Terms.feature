Feature: View Terms and Conditions

  @Terms @NeedsLogin @NeedsDashboard
  Scenario: View Terms and Conditions
    Given click on side menu
    When click on Terms and Conditions
    And user should see terms screen
    Then user click logout icon after view terms