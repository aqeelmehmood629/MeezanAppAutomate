package steps;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.LoginPage;
import pages.DashboardPage;

public class LoginSteps extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    // flag to avoid redundant login in Dashboard scenarios
    private boolean loginDone = false;

    public LoginSteps() {
        AndroidDriver driver = (AndroidDriver) getDriver();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }
    @Given("the Meezan Bank app is launched")
    public void the_meezan_bank_app_is_launched() {
        System.out.println("Launching Meezan Bank app...");
        // If your BaseTest already launches the app in setup, you can leave it empty.
        // Otherwise, call driver.launchApp() or the logic you already use.
    }

    // ---- Used in Dashboard feature file Background ----
    @Given("user is logged in with username {string} and password {string}")
    public void user_is_logged_in_with_username_and_password(String username, String password) {
        // conditionalLogin will log in only if login screen is visible
        loginPage.conditionalLogin(username, password);
        loginDone = true;
    }

    // ---- Used in Login feature file ----
    @When("user enters username {string} and password {string}")
    public void enterCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @And("user taps on login button")
    public void tapLogin() {
        loginPage.clickLogin();
    }

    
    @Then("user should be logged in successfully")
    public void verifyLogin() {
        // Switch to native context
        ((AndroidDriver) driver).context("NATIVE_APP");

        DashboardPage dashboard = new DashboardPage((AndroidDriver) driver);

        // Explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean dashboardVisible = wait.until(d -> dashboardPage.isSendMoneyTextVisible());

        Assert.assertTrue(dashboardVisible, "Dashboard is not visible after login!");
    }

    // helper to check login status from DashboardSteps
    public boolean isLoginDone() {
        return loginDone;
    }
}
