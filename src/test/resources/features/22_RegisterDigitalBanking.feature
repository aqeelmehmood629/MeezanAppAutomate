Feature: User Registration Flow

  @RegisterDigitalBanking
  Scenario: Register user in Digital Banking
    Given User clicks on Register
    When User clicks on Register for digital banking
    And User enters NIC number from CSV for digital banking
    And User enters Account number from CSV for digital banking
    And User clicks on Next button for digital banking
    And User enters OTP from CSV for digital banking
    And User sets Username from CSV for digital banking
    And User clicks on Next button again for digital banking
    Then User registration should be completed for digital banking