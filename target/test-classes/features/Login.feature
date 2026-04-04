Feature: Meezan Bank Login

  @01_Login
  Scenario Outline: Login with multiple users
  Given the Meezan Bank app is launched
  When user enters credentials from csv
  And user taps on login button
  Then user should be logged in successfully