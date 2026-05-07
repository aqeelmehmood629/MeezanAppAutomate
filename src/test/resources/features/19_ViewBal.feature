@ViewBal
Feature: Quick View Balance

  Scenario: User View Balance
    Given user is logged in
    When user clicks on View Balance for viewbal
    Then user should see balance for viewbal