package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;
import driver.DriverFactory;
import pages.LogoutPage;

public class LogoutSteps {

    LogoutPage logoutPage;

    private void init() {
        if (logoutPage == null) {
            logoutPage = new LogoutPage(DriverFactory.getDriver());
        }
    }

    @When("user clicks logout icon and app exits")
    public void user_clicks_logout_icon_and_app_exits() {
        init();
        logoutPage.clickLogout();
        Assert.assertFalse(logoutPage.isUsernameDisplayed());
    }
}