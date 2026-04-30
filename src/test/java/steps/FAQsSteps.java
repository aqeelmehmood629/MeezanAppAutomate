package steps;

import io.cucumber.java.en.*;
import pages.FAQsPage;

public class FAQsSteps {

	FAQsPage faqPage;

    private void init() {
        if (faqPage == null) {
            faqPage = new FAQsPage();
        }
    }
	
	@Given("user is on dashboard screen for faqs")
	public void user_is_on_dashboard_screenfaq() {
	    init();
        // optional validation
    }

    @When("user clicks on side menu for faqs")
    public void user_clicks_on_side_menu() {
        init();
        faqPage.clickSideMenu();
    }

    @And("user clicks on FAQs option for faqs")
    public void user_clicks_on_faqs_option() {
        init();
        faqPage.clickFaqs();
    }

    @And("user should see FAQs screen for faqs")
    public void user_should_see_faqs_screen() {
        init();
        faqPage.verifyFaqScreen();
    }
    @Then("user click home icon after faqs")
    public void userClickHomeFaqs() {
        init();
    	faqPage.userClickHomeIconFaqs();
    	
    	
    }
    
}