@ForgotPassword
Feature: Forgot Password

  Scenario: Reset password successfully
    Given user clicks on Forgot Password
    And user enters cnic from csv for password reset
    And user enters account number from csv for password reset
    And user clicks Next button for password reset
    And user enters otp for password reset
    And user clicks process button for password reset
    And user enters new password for password reset
    And user re-enters new password for password reset
    And user clicks Reset Password button
    Then user should see password reset success message