package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.Given;
import pages.DashboardPage;
import utils.LoginHelper;

public class CommomSteps {
	

    @Given("user is logged in and on dashboard")
    public void user_logged_in_dashboard() {
        LoginHelper.loginWithDefaultUser();
    }

    @Given("user is logged in")
    public void user_is_logged_in() {
        LoginHelper.loginWithDefaultUser();
    }
}
