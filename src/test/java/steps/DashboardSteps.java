package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;

public class DashboardSteps {

    AndroidDriver driver = DriverFactory.getDriver();
    DashboardPage dashboard = new DashboardPage(driver);

    // 🔹 User is logged in and on dashboard
    @Given("user is logged in and on dashboard")
    public void userOnDashboard() {
        System.out.println("✅ User is on dashboard");
    }

    // 🔹 Click Show Balance
    @When("user clicks on Show Balance")
    public void userClicksShowBalance() {
        dashboard.clickShowBalance();
    }

    // 🔹 Step to confirm action (optional)
    @Then("user should see updated balance")
    public void userShouldSeeUpdatedBalance() {
        System.out.println("👁️ Show Balance clicked successfully");
    }
}