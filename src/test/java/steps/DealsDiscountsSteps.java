package steps;

import org.testng.Assert;

import driver.DriverFactory;
import io.cucumber.java.en.*;
import pages.ChangeNotificationLanguagePage;
import pages.DealsDiscountsPage;


public class DealsDiscountsSteps {

    DealsDiscountsPage dealsPage = new DealsDiscountsPage();
    
    @Given("user is on dashboard screen for deals")
    public void user_is_on_dashbaord_screendeals() {
    }

    @When("user clicks on side menu for deals")
    public void user_clicks_on_side_menu() {
        dealsPage.clickSideMenu();
    }

    @And("user clicks on Deals and Discounts")
    public void user_clicks_on_deals_and_discounts() {
        dealsPage.clickDeals();
    }

    @Then("user click back button")
    public void userbackapp() {
    	dealsPage.clickBackBtn();
    }
}