@Feedback
Feature: Submit Feedback

  Scenario: User submits feedback successfully
    Given user opens feedback screen
    And user gives rating for feedback
    And user enters feedback text from csv
    Then user clicks submit feedback button