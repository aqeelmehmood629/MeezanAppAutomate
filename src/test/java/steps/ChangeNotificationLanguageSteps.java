package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;

import pages.ChangeNotificationLanguagePage;
import pages.FundsTransferPage;
import driver.DriverFactory;

public class ChangeNotificationLanguageSteps {

    ChangeNotificationLanguagePage page;
    FundsTransferPage sendMoneyPage;

    private void initPages() {
        if (page == null) {
            page = new ChangeNotificationLanguagePage(DriverFactory.getDriver());
            sendMoneyPage = new FundsTransferPage(DriverFactory.getDriver());
        }
    }

    @Given("user clicks on Side Menu for change notification language")
    public void clickSideMenu() {
        initPages();
        page.clickSideMenu();
    }

    @When("user clicks on Settings for change notification language")
    public void clickSettings() {
        initPages();
        page.clickSettings();
    }

    @And("user clicks on Manage Notifications")
    public void clickManageNotifications() {
        initPages();
        page.clickManageNotifications();
    }

    @And("user clicks on Language change for change notification language")
    public void clickLanguageChange() {
        initPages();
        page.selectLanguage();
    }

    @Then("user clicks on home icon after Language Change")
    public void clickhomeLanguageChange() {
        initPages();
        page.homeIconselectLanguage();
    }
}