package steps;

import driver.DriverFactory;
import io.cucumber.java.en.*;
import pages.FundsTransferPage;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;

public class FundTransferOwnSteps {

    FundsTransferPage page = new FundsTransferPage();

    private List<Map<String, String>> csvData;
    private Map<String, String> currentData;

    // ================= LOAD DATA =================
    private void loadData() {
        if (csvData == null) {
            csvData = CSVUtils.getAllData("src/test/resources/FundTransferData.csv");
        }
    }

    private void getFTOwnRow() {
        loadData();

        currentData = csvData.stream()
                .filter(row -> row.get("type").equalsIgnoreCase("FTOwn"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("FTOwn data not found"));
    }

    // ================= FLOW =================

    @Given("user navigate to send money for ftown")
    public void navigate_to_send_money() {

        DriverFactory.getDriver().context("NATIVE_APP");

        page.waitForDashboardToLoad();
        page.clickSendMoney();
    }

    @When("user selects own account for ftown")
    public void select_own_account() {

        getFTOwnRow();   // 🔥 load CSV row here

        page.selectOwnAccount();
    }
    @And("user hides keyboard for ftown")
    public void hide_keybaord() {

        driver.DriverFactory.getDriver().hideKeyboard();
    }
    

    @And("user opens purpose dropdown for ftown")
    public void open_dropdown() {

        page.openFTOwnPurposeDropdown();
    }

    @And("user selects purpose of transfer for ftown")
    public void select_purpose() {

        String purpose = currentData.get("purpose");

        page.selectPurpose("FTOwn", purpose);

        System.out.println("✅ Purpose from CSV: " + purpose);
    }

    @And("user enters amount for ftown")
    public void enter_amount() {

        String amount = currentData.get("amount");

        page.enterAmount(amount);

        System.out.println("✅ Amount from CSV: " + amount);
    }

    @And("user clicks Next button for ftown")
    public void click_next() {

        page.clickNext();
    }

    @And("user clicks Send Now button for ftown")
    public void click_send_now() {

        page.clickSendNow();
    }

    @Then("transaction should be successful for ftown")
    public void verify_transaction() {

        if (!page.isTransactionSuccessful()) {
            throw new AssertionError("❌ FTOwn transaction failed");
        }

        System.out.println("✅ FTOwn transaction successful");
    }
}