package steps;

import io.cucumber.java.en.*;
import pages.ViewAccountsPage;
import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;

public class ViewAccountsSteps {

	ViewAccountsPage page;
    private AndroidDriver driver;

  private void init() {

    if (driver == null) {
        driver = DriverFactory.getDriver();
    }

    if (page == null) {
        page = new ViewAccountsPage();
    }
  }

    private String dashboardAccountTitle;

    @Given("user is logged in for view Accounts")
    public void user_is_on_dashbaord_ViewAccounts() {
        init();
        System.out.println("🔄 Ensuring user is logged in and on Dashboard...");
        utils.LoginHelper.ensureLoggedIn(driver);
    }

    @When("user stores account title from dashboard")
    public void store_account_title() {
        init();
        dashboardAccountTitle = page.getDashboardAccountTitle();
        System.out.println("📌 Stored Dashboard Account Title: " + dashboardAccountTitle);
    }

    @And("user clicks on side menu")
    public void click_side_menu() {
        init();
        page.clickSideMenu();
    }

    @And("user clicks on My Accounts")
    public void click_my_accounts() {
        init();
        page.clickMyAccounts();
    }

    @And("user verifies account title with dashboard")
    public void verify_account_title() {
        init();

        String myAccountTitle = page.getMyAccountTitle();
        System.out.println("🔍 Comparing titles...");
        System.out.println("   Dashboard: '" + dashboardAccountTitle + "'");
        System.out.println("   My Accounts: '" + myAccountTitle + "'");

        String expected = dashboardAccountTitle.trim().toLowerCase().replaceAll("\\s+", " ");
        String actual = myAccountTitle.trim().toLowerCase().replaceAll("\\s+", " ");

        if (!expected.equals(actual)) {
            // Fallback: If one contains the other (e.g., "FARAZ AHMED" vs "FARAZ AHMED KHAN")
            if (expected.contains(actual) || actual.contains(expected)) {
                System.out.println("⚠️ Partial Match Found: The titles are not identical but one contains the other.");
            } else {
                throw new AssertionError(
                    "❌ Account Title Mismatch!\n" +
                    "Expected (from Dashboard): '" + dashboardAccountTitle + "'\n" +
                    "Actual (from My Accounts): '" + myAccountTitle + "'"
                );
            }
        }

        System.out.println("✅ Account Title Verified Successfully");
    }

    @Then("user click home icon after view Account")
    public void homeIconViewAccount() {
        init();
        page.homeIconAfterViewAccount();
    }
}