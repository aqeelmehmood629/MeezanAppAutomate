
Feature: Manage Registered Devices
  
  @RemoveRegisteredDevice @NeedsLogin
  Scenario: User removes a registered device
    Given user is logged in
    When user clicks on Side Menu for remove device
    And user clicks on Setting for remove device
    And user clicks on Manage Registered Devices
    And user clicks on Remove button for remove device
    Then registered device should be removed successfully