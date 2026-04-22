package steps;

import io.cucumber.java.en.*;
import pages.FAQsPage;

public class FAQsSteps {

	FAQsPage faqPage = new FAQsPage();
	
	@Given("user is on dashboard screen for faqs")
	public void user_is_on_dashboard_screenfaq() {
        // optional validation
    }

    @When("user clicks on side menu for faqs")
    public void user_clicks_on_side_menu() {
        faqPage.clickSideMenu();
    }

    @And("user clicks on FAQs option for faqs")
    public void user_clicks_on_faqs_option() {
        faqPage.clickFaqs();
    }

    @Then("user should see FAQs screen for faqs")
    public void user_should_see_faqs_screen() {
        faqPage.verifyFaqScreen();
    }
}