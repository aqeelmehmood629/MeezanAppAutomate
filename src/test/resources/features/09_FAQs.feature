Feature: FAQs Feature
  
  @FAQs
  Scenario: View FAQs
    Given user is logged in
    And user is on dashboard screen for faqs
    When user clicks on side menu for faqs
    And user clicks on FAQs option for faqs
    And user should see FAQs screen for faqs
    Then user click home icon after faqs