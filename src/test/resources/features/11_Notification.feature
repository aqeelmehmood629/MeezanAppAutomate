Feature: Notification Flow

  @Notification
  Scenario: View Notifications
    Given user is logged in
    And user is on dashboard screen
    When user clicks on sidebar menu
    And user clicks on notification icon
    And user should be able to view notifications
    Then user clicks home icon after view notifications