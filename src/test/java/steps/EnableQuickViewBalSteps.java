package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;

import pages.EnableQuickViewBalPage;
import utils.HybridAppStabilizer;
import driver.DriverFactory;

public class EnableQuickViewBalSteps {
	
	HybridAppStabilizer content = new HybridAppStabilizer();
	
	EnableQuickViewBalPage page = new EnableQuickViewBalPage(DriverFactory.getDriver());
    boolean beforeState;
	
	@Given("user clicks on Side Menu for Enable View Bal")
	public void user_clicks_on_side_menu_for_enable_view_bal() {
		page.clickSideMenuForEnableQucikViewBal();
	
	}
	@When("user clicks on Settings for Enabale View Bal")
	public void user_clicks_on_settings_for_enabale_view_bal() {
		page.clickSettingsForEnableQucikViewBal();
	}
	@And("user clicks on Quick View Balance for Enabale View Bal")
	public void user_clicks_on_quick_view_balance_for_enabale_view_bal() {
		page.clickQuickViewBalanceForEnableQucikViewBal();
		page.waitForQuickViewScreen();
	}
	@And("user toggles Quick View Balance for Enabale View Bal")
	public void user_toggles_quick_view_balance_for_enabale_view_bal() {
		page.toggleQuickViewForEnableQucikViewBal();
	}

}