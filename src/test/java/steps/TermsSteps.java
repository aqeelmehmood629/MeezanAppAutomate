package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.DashboardPage;
import pages.TermsPage;

public class TermsSteps {
	private TermsPage termspage;

	private void init() {
		if (termspage == null) {
			termspage = new TermsPage(DriverFactory.getDriver());
		}
	}

	@Given("click on side menu")
	public void click_on_side_menu() {
		init();
		termspage.clicksideMenu();
	}

	@When("click on Terms and Conditions")
	public void click_on_terms_and_conditions() {
		init();
		termspage.clickTerms();
	}

	@And("user should see terms screen")
	public void user_should_see_terms_screen() {
		init();
		termspage.checkEnglishTab();

	}

	@Then("user click logout icon after view terms")
	public void user_click_logout_icon_after_view_terms() {
		init();
		termspage.clickLogoutIcon();

	}

}