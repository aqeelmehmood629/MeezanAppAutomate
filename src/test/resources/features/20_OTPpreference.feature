
Feature: Change OTP Preference

  @ChangeOTPPreference
  Scenario: User changes OTP preference to SMS

    Given user is logged in
    When user clicks on Side Menu for otp
    And user clicks on Settings for otp
    And user clicks on Change OTP Preference
    And user selects option for otp
    And user clicks on Save Changes for otp
    Then OTP preference should be updated successfully