package steps;

import org.testng.Assert;

import base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.DashboardPage;
import pages.SendmoneyPage;

	public class SendmoneySteps extends BaseTest {

	    private SendmoneyPage sendMoneyPage;
	    private DashboardPage dashboardPage;
	    private AndroidDriver driver;

	    private void initPages() {
	        if (driver == null) {
	            driver = (AndroidDriver) getDriver();
	            sendMoneyPage = new SendmoneyPage(driver);
	            dashboardPage = new DashboardPage(driver);
	        }
	    }

	    @Given("user navigate to send money")
	    public void user_navigate_to_send_money() {
	        initPages();

	        Assert.assertTrue(dashboardPage.isSendMoneyTextVisible(),
	                "Send Money option not visible on Dashboard!");

	        dashboardPage.goToSendMoney();

	        Assert.assertTrue(sendMoneyPage.isSendMoneyPageDisplayed(),
	                "Send Money page not opened!");
	    }

	    @When("user selects account to transfer")
	    public void user_selects_account_to_transfer() {
	        initPages();
	        sendMoneyPage.selectAccount();
	    }
	    @When("user hides keyboard")
	    public void user_hides_keyboard() {
	        try {
	            driver.hideKeyboard(); // Appium method to hide keyboard
	        } catch (Exception e) {
	            System.out.println("Keyboard not visible or already hidden: " + e.getMessage());
	        }
	    }
	

	    @And("user clicks purpose of transfer dropdown")
	    public void user_clicks_purpose_of_transfer_dropdown() {
	        initPages();
	        sendMoneyPage.clickPurposeDropdown();
	    }

	    @And("user enters purpose of transfer {string}")
	    public void user_enters_purpose_of_transfer(String purpose) {
	        initPages();
	        sendMoneyPage.enterPurposeOfTransfer(purpose);
	    }

	    @And("user selects purpose of transfer dynamically {string}")
	    public void user_selects_purpose_of_transfer_dynamically(String purpose) {
	        initPages();
	        sendMoneyPage.selectPurposeDynamically(purpose);
	    }

	    @And("user enters amount {string}")
	    public void user_enters_amount(String amount) {
	        initPages();
	        sendMoneyPage.enterAmount(amount);
	    }

	    @And("user clicks Next button")
	    public void user_clicks_next_button() {
	        initPages();
	        sendMoneyPage.tapNext();
	    }

	    @And("user clicks Send Now button")
	    public void user_clicks_send_now_button() {
	        initPages();
	        sendMoneyPage.tapSendNow();
	    }

	    @Then("transaction should be successful")
	    public void transaction_should_be_successful() {
	        initPages();
	        Assert.assertTrue(sendMoneyPage.isTransactionSuccessful(),
	                "Transaction was not successful!");
	    }
	}
