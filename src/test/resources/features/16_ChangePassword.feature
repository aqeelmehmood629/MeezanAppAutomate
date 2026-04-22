Feature: Change Password with CSV

  @ChangePassword
  Scenario: Change password using CSV data
    Given user is on dashboard screen for change password
    When user clicks on side menu for change password
    And user clicks on Settings for change password
    And user clicks on Change Password
    And user reads test data from CSV for change password
    And user enters current password for change password
    And user enters new password for change password
    And user re-enters new password for change password
    And user clicks on submit button for change password
    Then password fields should be filled successfully