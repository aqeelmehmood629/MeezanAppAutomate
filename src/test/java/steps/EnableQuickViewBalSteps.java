package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;

import pages.EnableQuickViewBalPage;
import utils.HybridAppStabilizer;
import driver.DriverFactory;

public class EnableQuickViewBalSteps {
	
	EnableQuickViewBalPage page;
    boolean beforeState;

    private void init() {
        if (page == null) {
            page = new EnableQuickViewBalPage(DriverFactory.getDriver());
        }
    }
	
	@Given("user clicks on Side Menu for Enable View Bal")
	public void user_clicks_on_side_menu_for_enable_view_bal() {
	    init();
		page.clickSideMenuForEnableQucikViewBal();
	
	}
	@When("user clicks on Settings for Enabale View Bal")
	public void user_clicks_on_settings_for_enabale_view_bal() {
	    init();
		page.clickSettingsForEnableQucikViewBal();
	}
	@And("user clicks on Quick View Balance for Enabale View Bal")
	public void user_clicks_on_quick_view_balance_for_enabale_view_bal() {
	    init();
		page.clickQuickViewBalanceForEnableQucikViewBal();
		page.waitForQuickViewScreen();
	}
	@And("user toggles Quick View Balance for Enabale View Bal")
	public void user_toggles_quick_view_balance_for_enabale_view_bal() {
	    init();
		page.toggleQuickViewForEnableQucikViewBal();
	}
	@Then("user click home button after enable quick view Balance")
	public void userClickHomeIcon() {
	    init();
		page.clickHomeIconViewBal();
		
	}

}