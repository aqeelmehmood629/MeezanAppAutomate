package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;

import pages.ChangeNotificationLanguagePage;
import pages.FundsTransferPage;
import driver.DriverFactory;

public class ChangeNotificationLanguageSteps {

    ChangeNotificationLanguagePage page = new ChangeNotificationLanguagePage(DriverFactory.getDriver());
    FundsTransferPage sendMoneyPage = new FundsTransferPage();

    @Given("user clicks on Side Menu for change notification language")
    public void clickSideMenu() {
        page.clickSideMenu();
    }

    @When("user clicks on Settings for change notification language")
    public void clickSettings() {
        page.clickSettings();
    }

    @And("user clicks on Manage Notifications")
    public void clickManageNotifications() {
        page.clickManageNotifications();
    }

    @And("user clicks on Language change for change notification language")
    public void clickLanguageChange() {
        page.selectLanguage();
    }
    @Then("user clicks on home icon after Language Change")
    public void clickhomeLanguageChange() {
        page.homeIconselectLanguage();
    }
    
}