package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;
import utils.HybridAppStabilizer;

public class DashboardSteps {

    private AndroidDriver driver;
    private DashboardPage dashboardPage;

    @Given("user is already on dashboard")
    public void userOnDashboard() {

        driver = DriverFactory.getDriver();

        if (driver == null || driver.getSessionId() == null) {
            throw new RuntimeException("Driver not initialized!");
        }

        // 🔄 Dynamic context switch for current screen
        HybridAppStabilizer.switchContextDynamic(driver);

        dashboardPage = new DashboardPage(driver);

        System.out.println("📌 User is on Dashboard (current context applied dynamically)");
    }

    @When("user clicks on Show Balance")
    public void clickShowBalance() {

        // 🔄 Dynamic context switch before interacting
        HybridAppStabilizer.switchContextDynamic(driver);

        // ✅ Click Show Balance via page method
        dashboardPage.clickShowBalance();

        System.out.println("✅ Show Balance clicked");
    }

    @When("user captures current balance")
    public void captureCurrentBalance() {

        // 🔄 Dynamic context switch before capturing
        HybridAppStabilizer.switchContextDynamic(driver);

        // ✅ Store the current balance using page method
        dashboardPage.storeCurrentBalance();

        System.out.println("💰 Current Balance captured: " + dashboardPage.getStoredBalance());
    }

    @Then("balance should be updated correctly")
    public void verifyBalanceUpdate() {

        // 🔄 Dynamic context switch before verification
        HybridAppStabilizer.switchContextDynamic(driver);

        // ✅ Optional: refresh balance
        dashboardPage.refreshBalance();
        dashboardPage.storeCurrentBalance();

        System.out.println("💹 Balance after refresh: " + dashboardPage.getStoredBalance());
    }
}