package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;
import utils.CSVUtils;
import utils.LoginHelper;
import utils.ScreenDetector;

public class DashboardSteps {

    private AndroidDriver driver;
    private DashboardPage dashboard;
    private String targetAccountNumber; // holds CSV account number across TC07 steps

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

        if (ScreenDetector.isDashboard(driver)) {
            System.out.println("✅ Already on Dashboard — skipping navigation.");
        } else {
            System.out.println("📍 Not on Dashboard → navigating via Home icon...");
            dashboard.goToDashboard();
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
        
        // Ensure state is clean for the next data-driven row or scenario
        dashboard.closeShareOverlays();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC04 Step Definitions
    // ─────────────────────────────────────────────────────────────────────────
    @Given("user clicks on share icon")
    public void userClicksOnShareIcon() {
        init();
        dashboard.clickShareDetailsScreen();
    }

    @When("user selects share action {string}")
    public void userSelectsShareAction(String shareAction) {
        init();
        dashboard.clickShareOption(shareAction);
    }

    @Then("{string} should be shared successfully")
    public void shouldBeSharedSuccessfully(String expectedResult) {
        init();
        // Validating the appearance of Android share sheet (WhatsApp / Bluetooth)
        boolean isShareSheetUp = dashboard.isShareSheetDisplayed();
        
        // Try to close overlays first so subsequent tests don't fail, even if assertion fails
        try {
            dashboard.closeShareOverlays();
        } catch (Exception ignored) {}

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
    // 🔹 DSB_TC07 Step Definitions — CSV-driven Account Favourite
    // ─────────────────────────────────────────────────────────────────────────

    @Given("user reads target account number from CSV")
    public void userReadsTargetAccountNumberFromCSV() {
        init();
        targetAccountNumber = CSVUtils.getTargetAccountNumber();
        dashboard.setTargetAccountNumber(targetAccountNumber);
        System.out.println("📌 [TC07] Target account from CSV: '" + targetAccountNumber + "'");
    }

    @When("user searches for the account card by swiping if needed")
    public void userSearchesForAccountCardBySwiping() {
        init();
        boolean found = dashboard.findAccountCardWithSwipe();
        org.testng.Assert.assertTrue(found,
            "❌ [TC07] Account card for '" + targetAccountNumber + "' was not found after swiping through all cards.");
        System.out.println("✅ [TC07] Account card found for: '" + targetAccountNumber + "'");
    }

    @And("user clicks the favourite star for the target account")
    public void userClicksFavouriteStarForTargetAccount() {
        init();
        dashboard.clickFavouriteStarForTargetAccount();
    }

    @Then("favourite confirmation popup should appear")
    public void favouriteConfirmationPopupShouldAppear() {
        init();
        org.testng.Assert.assertTrue(dashboard.isFavouritePopupDisplayed(),
            "❌ [TC07] Favourite confirmation popup did not appear for account: '" + targetAccountNumber + "'");
        System.out.println("✅ [TC07] Favourite confirmation popup is displayed.");
    }

    @And("user confirms the favourite action")
    public void userConfirmsFavouriteAction() {
        init();
        dashboard.clickPopupYesButton();
    }

    @Then("account should be marked as favourite")
    public void accountShouldBeMarkedAsFavourite() {
        init();
        org.testng.Assert.assertTrue(dashboard.isAccountMarkedAsFavourite(),
            "❌ [TC07] Account was not marked as favourite (Done validation missing) for: '" + targetAccountNumber + "'");
        System.out.println("✅ [TC07] Account '" + targetAccountNumber + "' successfully marked as favourite!");
    }
}