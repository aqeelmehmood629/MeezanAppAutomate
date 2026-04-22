package steps;

import io.cucumber.java.en.*;
import pages.EnableDisableAccountsPage;

public class EnableDisableAccountsSteps {

    EnableDisableAccountsPage page = new EnableDisableAccountsPage();

    @Given("user opens side menu for Account Management")
    public void openSideMenu() {
        page.accountsSideMenu();
    }

    @When("user clicks on Settings for Account Management")
    public void clickSettings() {
        page.clickSettings();
    }

    @And("user clicks on Account Management")
    public void clickAccountManagement() {
        page.clickAccountManagement();
    }

    @And("user click on toggle button for Account Disable")
    public void deselectAccount() {
        page.deselectAccountManagement();
    }

    @And("user clicks Save Changes button for Account Management")
    public void clickSaveChanges() {
        page.clickSaveChanges();
    }

    @Then("account status should be UNLINKED for Account Management")
    public void verifyStatus() {
        page.verifyUnlinkedStatus();
    }
}