Feature: Logout Functionality

  @Logout @NoNeedLogin @Smoke
  Scenario: User logs out and exits app
    Given user is logged in
    When user clicks logout icon and app exits