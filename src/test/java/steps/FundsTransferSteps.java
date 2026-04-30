package steps;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;
import pages.FundsTransferPage;
import utils.CSVUtils;
import utils.HybridAppStabilizer;
import driver.DriverFactory;

import java.util.List;
import java.util.Map;

public class FundsTransferSteps {

	    private FundsTransferPage sendMoneyPage;
	    private List<Map<String, String>> csvData;
	    private int currentIndex = 0;
	    private AndroidDriver driver;

	    private void init() {
	        if (driver == null) {
	            driver = DriverFactory.getDriver();
	        }

	        if (sendMoneyPage == null) {
	            sendMoneyPage = new FundsTransferPage(driver);
	        }
	    }

	    private void loadData() {
	        if (csvData == null) {
	            csvData = CSVUtils.getAllData("src/test/resources/FundTransferData.csv");
	        }
	    }

	    @And("user navigate to send money")
	    public void navigate() {
	        init();

	        // ✅ Use safe context switching
	        HybridAppStabilizer.ensureNative(driver);

	        sendMoneyPage.waitForDashboardToLoad();
	        sendMoneyPage.clickSendMoney();
	    }

	    @And("user selects account to transfer")
	    public void selectAccount() {
	        init();
	        loadData();

	        String account = csvData.get(currentIndex).get("account");
	        sendMoneyPage.selectAccount(account);
	    }
	    @And("user hides keyboard")
	    public void user_hides_keyboard() {
	        init();
	        HybridAppStabilizer.hideKeyboard(driver);
	    }

	    @And("user clicks purpose of transfer dropdown")
	    public void user_clicks_purpose_of_transfer_dropdown() {
	        init();
	        sendMoneyPage.clickPurposeDropdown();
	    }

	    @And("user enters purpose of transfer")
	    public void user_enters_purpose_of_transfer() {
	        init();
	        loadData();
	        String purpose = csvData.get(currentIndex).get("purpose");
	        sendMoneyPage.enterPurposeOfTransfer(purpose);
	    }

	    @And("user selects purpose of transfer dynamically")
	    public void user_selects_purpose_of_transfer_dynamically() {
	        init();
	        loadData();
	        String purpose = csvData.get(currentIndex).get("purpose");
	        sendMoneyPage.selectPurposeDynamically(purpose);
	    }

	    @And("user enters amount")
	    public void user_enters_amount() {
	        init();
	        loadData();
	        String amount = csvData.get(currentIndex).get("amount");
	        sendMoneyPage.enterAmount(amount);
	    }

	    @And("user clicks Next button")
	    public void user_clicks_next_button() {
	        init();
	        sendMoneyPage.clickNext();
	    }

	    @And("user clicks Send Now button")
	    public void user_clicks_send_now_button() {
	        init();
	        sendMoneyPage.clickSendNow();
	    }

	    @Then("transaction should be successful")
	    public void verifyTransfer() {
	        init();

	        if (!sendMoneyPage.isTransactionSuccessful()) {
	            throw new AssertionError("Transfer Failed");
	        }

	        System.out.println("✅ Transfer successful");
	    }
	}