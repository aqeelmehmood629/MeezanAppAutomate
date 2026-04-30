Feature: Qibla Locator

  @Qibla
  Scenario: View Qibla Locator
    Given user is logged in
    And user is on dashboard screen for qibla
    When user clicks on side menu for qibla
    And user clicks on Qibla option
    And user should see Qibla screen
    Then user click home icon after qibla view