Feature: Meezan Bank Login

  @Login
  Scenario Outline: Login with Valid user
  Given the Meezan Bank app is launched
  When user enters credentials from csv
  And user taps on login button
  Then user should be logged in successfully