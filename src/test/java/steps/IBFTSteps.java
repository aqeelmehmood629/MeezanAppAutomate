package steps;

import io.cucumber.java.en.*;

import java.util.Map;

import org.testng.Assert;
import driver.DriverFactory;
import pages.IBFTPage;
import utils.CSVUtils;
import utils.HybridAppStabilizer;

public class IBFTSteps {
	
	String bankName;
    String account;
    String amount;

    IBFTPage page;

    private IBFTPage getPage() {
        if (page == null) {
            page = new IBFTPage(DriverFactory.getDriver());
        }
        return page;
    }


    @When("user clicks Send Money for IBFT Transfer")
    public void click_send_money() {
        HybridAppStabilizer.ensureNative(DriverFactory.getDriver());
        getPage().clickSendMoneyIBFT();
    }

    @And("user clicks Send Money to a new Account for IBFT Transfer")
    public void click_new_account() {
        getPage().clickNewAccountIBFT();
    }

    @And("user searches Bank using CSV for IBFT Transfer")
    public void search_bank() {

            Map<String, String> data =
                    CSVUtils.getAllData("src/test/resources/IBFT_Data.csv").get(0);

            bankName = data.get("BankName");
            account = data.get("account");
            amount = data.get("amount");

            getPage().searchBankIBFT(bankName);
    }

    @And("user selects Bank for IBFT Transfer")
    public void select_bank() {
    	getPage().selectBankIBFT(bankName);
    }

    @And("user enters Account Number using CSV for IBFT Transfer")
    public void enter_account() {
    	getPage().enterAccountIBFT(account);
    }

    @And("user clicks Fetch Account Details for IBFT Transfer")
    public void fetch_details() {
        getPage().fetchDetails(); // ok
    }

    @And("user clicks Next for IBFT Transfer")
    public void click_next() {
        getPage().clickNextIBFT();
    }

    @And("user enters Amount using CSV for IBFT Transfer")
    public void enter_amount() {
    	getPage().enterAmountIBFT(amount);
    }

    @And("user clicks Next after amount for IBFT Transfer")
    public void next_after_amount() {
        getPage().clickNextIBFT();
    }

    @And("user clicks Send Now button for IBFT Transfer")
    public void send_now() {
        getPage().clickSendNowIBFT();
    }

    @Then("user should see Transaction Successful message")
    public void verify_success() {

        Assert.assertTrue(getPage().isTransactionSuccessful(),
                "Transaction Failed - Success message not displayed");
    }

    // ✅ BEST PRACTICE (USE THIS)
    @When("user performs IBFT transfer using CSV")
    public void perform_ibft_using_csv() {

        getPage().performIBFT("src/test/resources/IBFT_Data.csv");
    }
}