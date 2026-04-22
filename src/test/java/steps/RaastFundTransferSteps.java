package steps;

import java.util.List;
import java.util.Map;

import driver.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.FundsTransferPage;
import utils.CSVUtils;

public class RaastFundTransferSteps {

	    FundsTransferPage page = new FundsTransferPage();

	    private List<Map<String, String>> csvData;
	    private Map<String, String> currentData;
	    private int currentIndex = 0;

	    // ================= NAVIGATION =================

	    @Given("user navigates to Raast payment screen for Raast Payment")
	    public void navigate_to_raast() {

	        DriverFactory.getDriver().context("NATIVE_APP");

	        if (csvData == null) {
	            csvData = CSVUtils.getAllData("src/test/resources/FundTransferData.csv");
	        }

	        currentData = csvData.stream()
	                .filter(d -> d.get("type").equalsIgnoreCase("Raast"))
	                .findFirst()
	                .orElseThrow(() -> new RuntimeException("No Raast data found"));

	        page.openRaastPayment(); // or page.clickRaastPayment() if separate button exists
	    }

	    // ================= ACCOUNT =================

	    @And("user selects account from csv for Raast Payment")
	    public void select_account() {
	        page.selectAccount(currentData.get("account"));
	    }

	    // ================= AMOUNT =================

	    @And("user enters amount from csv for Raast Payment")
	    public void enter_amount() {
	        page.enterAmount(currentData.get("amount"));
	    }

	    @And("user clicks Next button for Raast Payment")
	    public void click_next() {
	        page.clickNext();
	    }

	    @And("user clicks Send Now button for Raast Payment")
	    public void click_send_now() {
	        page.raastSendNowBtn();
	    }

	    // ================= VALIDATION =================

	    @Then("transaction should be successful for Raast Payment")
	    public void verify() {

	        if (!page.isTransactionSuccessful()) {
	            throw new AssertionError("❌ Raast transaction failed");
	        }

	        System.out.println("✅ Raast transaction successful");
	    }
	}
