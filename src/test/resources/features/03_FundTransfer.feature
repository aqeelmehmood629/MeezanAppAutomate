Feature: Fund Transfer

@FT
Scenario: Transfer funds to Meezan Bank account
    Given user navigate to send money
    When user selects account to transfer
    And user hides keyboard
    And user clicks purpose of transfer dropdown
    And user enters purpose of transfer
    And user selects purpose of transfer dynamically
    And user enters amount
    And user clicks Next button
    And user clicks Send Now button
    And transaction should be successful
    
    
@FTOwn
Scenario: Transfer funds to Own Account
    Given user navigate to send money for ftown
    When user selects own account for ftown
    And user hides keyboard for ftown
    And user opens purpose dropdown for ftown
    And user selects purpose of transfer for ftown
    And user enters amount for ftown
    And user clicks Next button for ftown
    And user clicks Send Now button for ftown
    Then transaction should be successful for ftown
    
@Raast   
Scenario: Perform Raast payment successfully
    Given user navigates to Raast payment screen for Raast Payment
    And user selects account from csv for Raast Payment
    And user enters amount from csv for Raast Payment
    And user clicks Next button for Raast Payment
    And user clicks Send Now button for Raast Payment
    Then transaction should be successful for Raast Payment
    
    
@IBFT
Scenario: User performs IBFT transfer successfully
    When user clicks Send Money for IBFT Transfer
    And user clicks Send Money to a new Account for IBFT Transfer
    And user searches Bank using CSV for IBFT Transfer
    And user selects Bank for IBFT Transfer
    And user enters Account Number using CSV for IBFT Transfer
    And user clicks Fetch Account Details for IBFT Transfer
    And user clicks Next for IBFT Transfer
    And user enters Amount using CSV for IBFT Transfer
    And user clicks Next after amount for IBFT Transfer
    And user clicks Send Now button for IBFT Transfer
    Then user should see Transaction Successful message