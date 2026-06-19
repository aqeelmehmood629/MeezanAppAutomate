@BillPayment @Regression
Feature: Bill Payment Validation

  @BP_TC01
  Scenario: Make bill payment using account number from CSV
    Given user is on Dashbaord screen for bill payment
    When user clicks on Bill Payment
    And user selects Account Number from CSV
    And user clicks on Next button
    And user clicks on Next button
    And user clicks on Pay Now button
    Then payment success message should be displayed