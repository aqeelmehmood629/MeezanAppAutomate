package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import pages.QuickViewBalPage;

public class QuickViewBalsteps {

    private AndroidDriver driver;
    private QuickViewBalPage viewBalancePage;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (viewBalancePage == null && driver != null) {
            viewBalancePage = new QuickViewBalPage(driver);
        }
    }

    @When("user clicks on View Balance for viewbal")
    public void user_clicks_on_view_balance() {
        init();
        viewBalancePage.clickViewBalance();
    }

    @Then("user should see balance for viewbal")
    public void user_should_see_balance() {
        init();
        viewBalancePage.verifyBalanceVisible();
    }
}