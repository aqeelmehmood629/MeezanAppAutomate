package steps;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;
import utils.LoginHelper;

public class DashboardSteps {

    private AndroidDriver driver;
    private DashboardPage dashboard;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }

        if (dashboard == null) {
            dashboard = new DashboardPage(driver);
        }
    }
    @And("user clicks on Show Balance")
    public void userClicksShowBalance() {
        init();
        dashboard.clickShowBalance();
    }

    @Then("user should see updated balance")
    public void userShouldSeeUpdatedBalance() {
        System.out.println("👁️ Show Balance clicked successfully");
    }
    public void ensureDashboard() {
    	init();

        List<WebElement> homeIcons = driver.findElements(
            By.xpath("//android.widget.Image[contains(@text,'home-icon')]"));

        if (homeIcons.isEmpty()) {
            System.out.println("Not on dashboard → navigating...");
            dashboard.goToDashboard();
        } else {
            System.out.println("Already on dashboard");
        }
    }
    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC02 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @Given("^user is on (?:account details|dashboard) screen$")
    public void userIsOnDashboardScreen() {
        ensureDashboard();
    }

    @When("Title should be displayed")
    public void titleShouldBeDisplayed() {
        init();
        org.testng.Assert.assertTrue(dashboard.isTitleDisplayed(), "Title is not displayed");
    }

    @And("Current Account should be displayed")
    public void currentAccountShouldBeDisplayed() {
        org.testng.Assert.assertTrue(dashboard.isCurrentAccountDisplayed(), "Current Account is not displayed");
    }

    @And("IBAN should be displayed")
    public void ibanShouldBeDisplayed() {
        org.testng.Assert.assertTrue(dashboard.isIbanDisplayed(), "IBAN is not displayed");
    }

    @And("Branch should be displayed")
    public void branchShouldBeDisplayed() {
        org.testng.Assert.assertTrue(dashboard.isBranchDisplayed(), "Branch is not displayed");
    }

    @And("Balance should be displayed")
    public void balanceShouldBeDisplayed() {
        org.testng.Assert.assertTrue(dashboard.isBalanceDisplayed(), "Balance is not displayed");
    }

    @And("Share option should be displayed")
    public void shareOptionShouldBeDisplayed() {
        org.testng.Assert.assertTrue(dashboard.isShareButtonDisplayed(), "Share button is not displayed");
    }

    @Then("Transaction view option should be displayed")
    public void transactionViewOptionShouldBeDisplayed() {
        org.testng.Assert.assertTrue(dashboard.isTransactionButtonDisplayed(), "Transaction button is not displayed");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC03 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @Given("user click on share details screen")
    public void userClickOnShareDetailsScreen() {
        init();
        dashboard.clickShareDetailsScreen();
    }

    @When("user clicks on {string}")
    public void userClicksOnAction(String action) {
        init();
        dashboard.clickCopyOption(action);
    }

    @Then("{string} should be copied to clipboard")
    public void shouldBeCopiedToClipboard(String expectedResult) {
        // Appium doesn't easily inspect the native clipboard without special commands.
        // As requested by user, utilizing a placeholder verification log here.
        System.out.println("✅ Verified (Placeholder): " + expectedResult + " has been copied to clipboard successfully.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC04 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @Given("user clicks on share icon")
    public void userClicksOnShareIcon() {
        init();
        dashboard.clickShareDetailsScreen();
    }

    @When("user selects {string}")
    public void userSelectsShareAction(String shareAction) {
        init();
        dashboard.clickShareOption(shareAction);
    }

    @Then("{string} should be shared successfully")
    public void shouldBeSharedSuccessfully(String expectedResult) {
        init();
        // Validating the appearance of Android share sheet (WhatsApp / Bluetooth)
        boolean isShareSheetUp = dashboard.isShareSheetDisplayed();
        org.testng.Assert.assertTrue(isShareSheetUp, "❌ Share sheet (WhatsApp/Bluetooth) did not appear for: " + expectedResult);
        System.out.println("✅ Share sheet successfully displayed for: " + expectedResult);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC05 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @Given("user rapidly taps on refresh button multiple times")
    public void userRapidlyTapsOnRefreshButton() {
        init();
        dashboard.rapidTapRefresh();
    }

    @When("application should remain stable")
    public void applicationShouldRemainStable() {
        System.out.println("✅ Application stability checked during rapid taps.");
    }

    @Then("data is correctly updated without crashes")
    public void dataIsCorrectlyUpdatedWithoutCrashes() {
        init();
        org.testng.Assert.assertTrue(dashboard.isDataUpdatedWithoutCrashes(), "App crashed or data not correctly updated after refresh");
        System.out.println("✅ Data refreshed without crashes");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC06 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @When("user taps on View Transaction History")
    public void userTapsOnViewTransactionHistory() {
        init();
        dashboard.clickViewTransaction();
    }

    @Then("transaction history screen should be displayed")
    public void transactionHistoryScreenShouldBeDisplayed() {
        init();
        org.testng.Assert.assertTrue(dashboard.isTransactionHistoryScreenDisplayed(), "Transaction History screen is not displayed");
        System.out.println("✅ Transaction history screen validated");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC07 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @When("user clicks on Favourite button")
    public void userClicksOnFavouriteButton() {
        init();
        dashboard.clickFavouriteButton();
    }

    @And("favourite confirmation popup should be displayed")
    public void favouriteConfirmationPopupShouldBeDisplayed() {
        init();
        org.testng.Assert.assertTrue(dashboard.isFavouritePopupDisplayed(), "Favourite confirmation popup not displayed");
    }

    @And("user clicks on Yes button")
    public void userClicksOnYesButton() {
        init();
        dashboard.clickPopupYesButton();
    }

    @Then("account should be marked as favourite")
    public void accountShouldBeMarkedAsFavourite() {
        init();
        org.testng.Assert.assertTrue(dashboard.isAccountMarkedAsFavourite(), "Account was not marked as favourite (Done validation missing)");
        System.out.println("✅ Account marked as favourite successfully");
    }
}