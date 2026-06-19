
Feature: Forgot Password

  @ForgotPassword @NoNeedLogin
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
    
    
   Scenario: Reset password using ID types from CSV
    When user selects ID Type from CSV
    And user enters NIC from CSV
    And user enters Account Number from CSV
    And user clicks on Next button
    And user enters OTP from CSV
    And user clicks on Process button
    And user enters New Password from CSV
    And user re-enters Confirm Password from CSV
    And user clicks on Reset Password button
    Then success message should be displayed
    
    
    
    Scenario: Validate CNIC and Account Number Question Mark display
    Given user is on Forgot Password screen
    When user clicks on CNIC question mark
    Then CNIC information should be displayed
    When user clicks on Account Number field
    Then Account Number field should be displayed
    
    