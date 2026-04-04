package steps;

import io.cucumber.java.en.*;
import pages.FundsTransferPage;
import utils.CSVUtils;
import driver.DriverFactory;

import java.util.List;
import java.util.Map;

public class FundsTransferSteps {

    FundsTransferPage sendMoneyPage = new FundsTransferPage();
    private List<Map<String, String>> csvData;
    private int currentIndex = 0;

    @Given("user navigate to send money")
    public void user_navigate_to_send_money() {
        DriverFactory.getDriver().context("NATIVE_APP");
        sendMoneyPage.waitForDashboardToLoad();
        sendMoneyPage.clickSendMoney();
    }

    @When("user selects account to transfer")
    public void user_selects_account_to_transfer() {
        csvData = CSVUtils.getAllData("src/test/resources/FundTransferData.csv");
        String account = csvData.get(currentIndex).get("account");
        sendMoneyPage.selectAccount(account);
    }

    @And("user hides keyboard")
    public void user_hides_keyboard() {
        try {
            DriverFactory.getDriver().hideKeyboard();
        } catch (Exception ignored) {}
    }

    @And("user clicks purpose of transfer dropdown")
    public void user_clicks_purpose_of_transfer_dropdown() {
        sendMoneyPage.clickPurposeDropdown();
    }

    @And("user enters purpose of transfer")
    public void user_enters_purpose_of_transfer() {
        String purpose = csvData.get(currentIndex).get("purpose");
        sendMoneyPage.enterPurposeOfTransfer(purpose);
    }

    @And("user selects purpose of transfer dynamically")
    public void user_selects_purpose_of_transfer_dynamically() {
        String purpose = csvData.get(currentIndex).get("purpose");
        sendMoneyPage.selectPurposeDynamically(purpose);
    }

    @And("user enters amount")
    public void user_enters_amount() {
        String amount = csvData.get(currentIndex).get("amount");
        sendMoneyPage.enterAmount(amount);
    }

    @And("user clicks Next button")
    public void user_clicks_next_button() {
        sendMoneyPage.clickNext();
    }

    @And("user clicks Send Now button")
    public void user_clicks_send_now_button() {
        sendMoneyPage.clickSendNow();
    }

    @Then("transaction should be successful")
    public void transaction_should_be_successful() {
        if (!sendMoneyPage.isTransactionSuccessful()) {
            throw new AssertionError("❌ Transfer Failed for row: " + currentIndex);
        }

        System.out.println("✅ Transfer successful for row: " + currentIndex);

        // Prepare for next CSV row if any
        currentIndex++;
        if (currentIndex < csvData.size()) {
            // Navigate back and click Send Money again
            sendMoneyPage.navigateBackToDashboard();
            sendMoneyPage.clickSendMoney();
            // Note: next scenario iteration will pick next row
        }
    }
}