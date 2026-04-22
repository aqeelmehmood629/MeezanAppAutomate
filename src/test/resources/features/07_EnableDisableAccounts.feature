Feature: Account Management
  
  @Accountdeselection
  Scenario: User deselects account
    Given user opens side menu for Account Management
    When user clicks on Settings for Account Management
    And user clicks on Account Management
    And user click on toggle button for Account Disable
    And user clicks Save Changes button for Account Management
    Then account status should be UNLINKED for Account Management