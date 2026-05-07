package steps;

import io.cucumber.java.en.*;
import pages.EnableDisableAccountsPage;
import utils.HybridAppStabilizer;
import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;

public class EnableDisableAccountsSteps {

    private EnableDisableAccountsPage page;
    private AndroidDriver driver;

    private void init() {
        if (driver == null) {
        driver = DriverFactory.getDriver();
    }

    if (page == null) {
        page = new EnableDisableAccountsPage(driver);
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
        HybridAppStabilizer.ensureWebView(driver);
        page.deselectAccountManagement();
    }

    @And("user clicks Save Changes button for Account Management")
    public void clickSaveChanges() {
        init();
        HybridAppStabilizer.ensureNative(driver);
        page.clickSaveChanges();
    }

    @Then("account status should be UNLINKED for Account Management")
    public void verifyStatus() {
        init();
        page.verifyAccountStatus();
    }
}