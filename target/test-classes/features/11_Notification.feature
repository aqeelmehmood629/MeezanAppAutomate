Feature: Notification Flow

  @Notification
  Scenario: View Notifications
    Given user is on dashboard screen
    When user clicks on sidebar menu
    And user clicks on notification icon
    Then user should be able to view notifications