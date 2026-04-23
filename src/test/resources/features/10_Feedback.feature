@Feedback
Feature: Submit Feedback

  Scenario: User submits feedback successfully
    Given user opens feedback screen
    And user gives rating for feedback
    And user enters feedback text from csv
    And user clicks submit feedback button
    Then user clicks home icon after feedback