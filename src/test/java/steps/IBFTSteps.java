package steps;

import io.cucumber.java.en.*;

import java.util.Map;

import org.testng.Assert;
import driver.DriverFactory;
import pages.IBFTPage;
import utils.CSVUtils;

public class IBFTSteps {
	
	String bankName;
    String account;
    String amount;

    IBFTPage page = new IBFTPage(DriverFactory.getDriver());

    @When("user clicks Send Money for IBFT Transfer")
    public void click_send_money() {
        page.clickSendMoneyIBFT();
    }

    @And("user clicks Send Money to a new Account for IBFT Transfer")
    public void click_new_account() {
        page.clickNewAccountIBFT();
    }

    @And("user searches Bank using CSV for IBFT Transfer")
    public void search_bank() {

            Map<String, String> data =
                    CSVUtils.getAllData("src/test/resources/IBFT_Data.csv").get(0);

            bankName = data.get("BankName");
            account = data.get("account");
            amount = data.get("amount");

            page.searchBankIBFT(bankName);
    }

    @And("user selects Bank for IBFT Transfer")
    public void select_bank() {
    	page.selectBankIBFT(bankName);
    }

    @And("user enters Account Number using CSV for IBFT Transfer")
    public void enter_account() {
    	page.enterAccountIBFT(account);
    }

    @And("user clicks Fetch Account Details for IBFT Transfer")
    public void fetch_details() {
        page.fetchDetails(); // ok
    }

    @And("user clicks Next for IBFT Transfer")
    public void click_next() {
        page.clickNextIBFT();
    }

    @And("user enters Amount using CSV for IBFT Transfer")
    public void enter_amount() {
    	page.enterAmountIBFT(amount);
    }

    @And("user clicks Next after amount for IBFT Transfer")
    public void next_after_amount() {
        page.clickNextIBFT();
    }

    @And("user clicks Send Now button for IBFT Transfer")
    public void send_now() {
        page.clickSendNowIBFT();
    }

    @Then("user should see Transaction Successful message")
    public void verify_success() {

        Assert.assertTrue(page.isTransactionSuccessful(),
                "Transaction Failed - Success message not displayed");
    }

    // ✅ BEST PRACTICE (USE THIS)
    @When("user performs IBFT transfer using CSV")
    public void perform_ibft_using_csv() {

        page.performIBFT("src/test/resources/IBFT_Data.csv");
    }
}