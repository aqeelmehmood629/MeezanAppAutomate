Feature: Meezan Bank Login

  @LOG_TC01 @NeedsLogin
  Scenario: Login with Valid credentials
    Given the Meezan Bank app is launched
    When user enters credentials from csv
    And user taps on login button
    Then user should be logged in successfully


  @LOG_TC02 @NoNeedLogin
  Scenario Outline: Login with Empty Fields - <description>
    Given user is on Login screen
    When user enters invalid credentials from CSV row <rowIndex>
    Then verify mandatory field validation message is displayed

    Examples:
      | rowIndex | description                 |
      | 0        | Empty Username              |
      | 1        | Empty Password              |
      | 4        | Empty Username and Password |


  @LOG_TC03 @NoNeedLogin
  Scenario Outline: Login with Wrong Credentials - <description>
    Given user is on Login screen
    When user enters invalid credentials from CSV row <rowIndex>
    And user clicks show password icon to verify password visibility
    Then verify invalid credential error message is displayed

    Examples:
      | rowIndex | description                           |
      | 2        | Wrong Password Case                   |
      | 3        | Wrong username or Wrong Password Case |

  @LOG_TC04 @NoNeedLogin 
  Scenario: Verify account is locked
    Given user is on Login screen
    When user enters invalid credentials from CSV row 2
    And verify invalid credential error message is displayed
    Then verify account lock message is displayed


  
  @LOG_TC05 @NoNeedLogin
  Scenario: Verify username is remembered after app restart
    Given user is on Login screen
    When user enters credentials from csv
    And user selects Remember My Username option
    And user taps on login button
    Then user should be logged in successfully
    When user restarts the app
    Then verify username is prefilled



  @LOG_TC06 @NoNeedLogin
  Scenario: Verify Login screen UI elements are displayed
    Given user is on Login screen
    And verify View Balance button is displayed
    Then verify Register tab is displayed