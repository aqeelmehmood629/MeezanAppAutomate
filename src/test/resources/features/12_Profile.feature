@MyProfile
Feature: Open Profile

  Scenario: User navigates to Profile
    Given user is logged in
    And user clicks on side bar menu
    And user clicks on My Profile menu
    And My Profile screen should be displayed
    Then user clicks home icon after profile view