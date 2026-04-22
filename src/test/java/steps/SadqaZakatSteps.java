package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.SadqaZakatPage;
import utils.CSVUtils;

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

    // 🔥 Dynamic (Sadqa + Zakat)
    @Given("user is logged in and on dashboard for {string}")
    public void userOnDashboard(String type) {
        driver = DriverFactory.getDriver();
        page = new SadqaZakatPage(driver);

        donationData = CSVUtils.getDonationData(type);
        index = 0;
        donationType = type;

        System.out.println("✅ Dashboard ready for " + type + " donation");
    }

    @When("user clicks on Zakat Sadqa button")
    public void clickZakatSadqaBtn() {
        page.clickZakatSadqaBtn();
    }

    @And("user click on Sadqa tab")
    public void clickSadqaBtn() {
        page.clickSadqaTab();
    }

    @When("user searches foundation from CSV")
    public void searchFoundation() throws InterruptedException {

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
        page.selectFoundation(foundation);
    }

    @And("user enters amount from CSV")
    public void enterAmount() {
        page.enterAmount(amount);
    }

    @And("user clicks on Next button")
    public void clickNext() {
        page.clickNext();
    }

    @And("user clicks on Pay Now button for {string}")
    public void clickPayNow(String type) {

        if (type.equalsIgnoreCase("Zakat")) {
            page.clickZakatNow();
        } else if (type.equalsIgnoreCase("Sadqa")) {
            page.clickSadqaNow();
        }
    }

    @Then("donation should be successful")
    public void validateDonation() {
        page.waitForDonationSuccess();

        System.out.println("✅ " + donationType + " donation successful for "
                + foundation + " amount " + amount);

        index++;
    }
}