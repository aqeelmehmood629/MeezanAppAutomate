Feature: Quick View Balance Toggle

  @EnableQuickViewBalance
  Scenario: Enable quick view balance
    Given user is logged in
    And user clicks on Side Menu for Enable View Bal
    When user clicks on Settings for Enabale View Bal
    And user clicks on Quick View Balance for Enabale View Bal
    And user toggles Quick View Balance for Enabale View Bal
    Then user click home button after enable quick view Balance