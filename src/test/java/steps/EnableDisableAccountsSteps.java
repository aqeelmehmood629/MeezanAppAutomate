package steps;

import io.cucumber.java.en.*;
import pages.EnableDisableAccountsPage;

public class EnableDisableAccountsSteps {

    EnableDisableAccountsPage page;

    private void init() {
        if (page == null) {
            page = new EnableDisableAccountsPage();
        }
    }

    @Given("user opens side menu for Account Management")
    public void openSideMenu() {
        init();
        page.accountsSideMenu();
    }

    @When("user clicks on Settings for Account Management")
    public void clickSettings() {
        init();
        page.clickSettings();
    }

    @And("user clicks on Account Management")
    public void clickAccountManagement() {
        init();
        page.clickAccountManagement();
    }

    @And("user click on toggle button for Account Disable")
    public void deselectAccount() {
        init();
        page.deselectAccountManagement();
    }

    @And("user clicks Save Changes button for Account Management")
    public void clickSaveChanges() {
        init();
        page.clickSaveChanges();
    }

    @Then("account status should be UNLINKED for Account Management")
    public void verifyStatus() {
        init();
        page.verifyUnlinkedStatus();
    }
}