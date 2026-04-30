package steps;

import org.testng.Assert;

import driver.DriverFactory;
import io.cucumber.java.en.*;
import pages.DealsDiscountsPage;


public class DealsDiscountsSteps {

    DealsDiscountsPage dealsPage;

    private void init() {
        if (dealsPage == null) {
            dealsPage = new DealsDiscountsPage();
        }
    }
    
    @Given("user is on dashboard screen for deals")
    public void user_is_on_dashbaord_screendeals() {
        init();
    }

    @When("user clicks on side menu for deals")
    public void user_clicks_on_side_menu() {
        init();
        dealsPage.clickSideMenu();
    }

    @And("user clicks on Deals and Discounts")
    public void user_clicks_on_deals_and_discounts() {
        init();
        dealsPage.clickDeals();
    }

    @Then("user click back button")
    public void userbackapp() {
        init();
    	dealsPage.clickBackBtn();
    }
}