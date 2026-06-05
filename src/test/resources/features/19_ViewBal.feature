
Feature: Quick View Balance
  @ViewBal @NoNeedLogin @Smoke
  Scenario: User View Balance
    When user clicks on View Balance for viewbal
    Then user should see balance for viewbal