@DashboardApp
Feature: Dashboard Feature

  # Ensure user is logged in first (conditional login handles already logged-in case)
  Background: 
    Given the Meezan Bank app is launched
    And user is logged in with username "Aiman23" and password "Admin@786"

  Scenario: Verify Dashboard is visible
    Then dashboard should be visible