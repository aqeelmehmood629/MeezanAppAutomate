package steps;

import io.cucumber.java.en.*;
import pages.ViewAccountsPage;

public class ViewAccountsSteps {

	ViewAccountsPage page;

    private void init() {
        if (page == null) {
            page = new ViewAccountsPage();
        }
    }

    String dashboardAccountTitle;

    @Given("user stores account title from dashboard")
    public void store_account_title() {
        init();
        dashboardAccountTitle = page.getDashboardAccountTitle();
        System.out.println("📌 Dashboard Account Title: " + dashboardAccountTitle);
    }

    @When("user clicks on side menu")
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

        if (!dashboardAccountTitle.equals(myAccountTitle)) {
            throw new AssertionError(
                "❌ Account Title Mismatch \nDashboard: " 
                + dashboardAccountTitle + 
                "\nMy Accounts: " + myAccountTitle
            );
        }

        System.out.println("✅ Account Title Verified Successfully");
    }
    @Then("user click home icon after view Account")
    public void homeIconViewAccount() {
        init();
    	page.homeIconAfterViewAccount();
    }
    
}