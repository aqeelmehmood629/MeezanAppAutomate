@Limit
Feature: Limit Management

Scenario Outline: User updates limit for transfer types
    When user clicks Limit Management
    And user selects "<LimitType>"
    And user selects amount using CSV
    And user clicks Apply Button
    And limit should be updated successfully
    Then user click home icon after limit updated

Examples:
    | LimitType       |
    | meezantomeezan  |
    