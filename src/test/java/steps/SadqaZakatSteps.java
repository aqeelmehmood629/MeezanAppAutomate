package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.SadqaZakatPage;
import utils.CSVUtils;
import utils.LoginHelper;

import java.util.List;
import java.util.Map;

public class SadqaZakatSteps {

    AndroidDriver driver;
    SadqaZakatPage page;

    List<Map<String, String>> donationData;
    int index = 0;
    String foundation;
    String amount;
    String donationType;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (page == null && driver != null) {
            page = new SadqaZakatPage(driver);
        }
    }

    // 🔥 Dynamic (Sadqa + Zakat)
    @Given("user is logged in and on dashboard for {string}")
    public void userOnDashboard(String type) {
        LoginHelper.loginWithDefaultUser();
        init();

        donationData = CSVUtils.getDonationData(type);
        index = 0;
        donationType = type;

        System.out.println("✅ Dashboard ready for " + type + " donation");
    }

    @When("user clicks on Zakat Sadqa button")
    public void clickZakatSadqaBtn() {
        init();
        page.clickZakatSadqaBtn();
    }

    @And("user click on Sadqa tab")
    public void clickSadqaBtn() {
        init();
        page.clickSadqaTab();
    }

    @When("user searches foundation from CSV")
    public void searchFoundation() throws InterruptedException {
        init();

        if (donationData.isEmpty()) {
            throw new RuntimeException("❌ No data found in CSV");
        }

        Map<String, String> row = donationData.get(index);

        foundation = row.get("FoundationName");
        amount = row.get("Amount");

        System.out.println("🔍 Foundation: " + foundation);
        System.out.println("💰 Amount: " + amount);

        page.searchFoundation(foundation);
    }

    @And("user selects foundation from results")
    public void selectFoundation() {
        init();
        page.selectFoundation(foundation);
    }

    @And("user enters amount from CSV")
    public void enterAmount() {
        init();
        page.enterAmount(amount);
    }

    @And("user clicks on Next button")
    public void clickNext() {
        init();
        page.clickNext();
    }

    @And("user clicks on Pay Now button for {string}")
    public void clickPayNow(String type) {
        init();

        if (type.equalsIgnoreCase("Zakat")) {
            page.clickZakatNow();
        } else if (type.equalsIgnoreCase("Sadqa")) {
            page.clickSadqaNow();
        }
    }

    @Then("donation should be successful")
    public void validateDonation() {
        init();
        page.waitForDonationSuccess();

        System.out.println("✅ " + donationType + " donation successful for "
                + foundation + " amount " + amount);

        index++;
    }
}