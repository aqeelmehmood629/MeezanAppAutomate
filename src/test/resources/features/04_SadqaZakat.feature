Feature: Sadqa & Zakat Donations

  @Sadqa
  Scenario: Perform Sadqa donation
    Given user is logged in and on dashboard for "Sadqa"
    When user clicks on Zakat Sadqa button
    And user click on Sadqa tab
    And user searches foundation from CSV
    And user selects foundation from results
    And user enters amount from CSV
    And user clicks on Next button
    And user clicks on Pay Now button for "Sadqa"
    Then donation should be successful

  @Zakat
  Scenario: Perform Zakat donation
    Given user is logged in and on dashboard for "Zakat"
    When user clicks on Zakat Sadqa button
    And user searches foundation from CSV
    And user selects foundation from results
    And user enters amount from CSV
    And user clicks on Next button
    And user clicks on Pay Now button for "Zakat"
    Then donation should be successful