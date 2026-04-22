Feature: FAQs Feature
  
  @FAQs
  Scenario: View FAQs
    Given user is on dashboard screen for faqs
    When user clicks on side menu for faqs
    And user clicks on FAQs option for faqs
    Then user should see FAQs screen for faqs