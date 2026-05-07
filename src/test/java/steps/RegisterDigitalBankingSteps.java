package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.RegisterDigitalBankingPage;
import utils.CSVUtils;
import utils.HybridAppStabilizer;

import java.util.List;
import java.util.Map;

public class RegisterDigitalBankingSteps {

    private AndroidDriver driver;
    private RegisterDigitalBankingPage registerPage;
    private List<Map<String, String>> csvData;
    private Map<String, String> currentData;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (registerPage == null && driver != null) {
            registerPage = new RegisterDigitalBankingPage(driver);
        }
    }

    @Given("User clicks on Register")
    public void clickRegister() {
        init();
        HybridAppStabilizer.stabilizeApp(driver);
       try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        if (csvData == null) {
            csvData = CSVUtils.getAllData("src/test/resources/RegisterDigitalBanking.csv");
        }
        currentData = csvData.get(0);

        registerPage.clickRegister();
    }

    @When("User clicks on Register for digital banking")
    public void clickRegisterDigitalBanking() {
        init();
        registerPage.clickRegisterDigitalBanking();
    }

    @And("User enters NIC number from CSV for digital banking")
    public void enterNic() {
        init();
        registerPage.enterNic(currentData.get("nic"));
    }

    @And("User enters Account number from CSV for digital banking")
    public void enterAccount() {
        init();
        registerPage.enterAccount(currentData.get("accno"));
    }

    @And("User clicks on Next button for digital banking")
    public void clickNext() {
        init();
        registerPage.clickNext();
    }

    @And("User enters OTP from CSV for digital banking")
    public void enterOtp() {
        init();
        HybridAppStabilizer.ensureNative(driver);
        registerPage.enterOTP(currentData.get("otp"));
    }

    @And("User sets Username from CSV for digital banking")
    public void setUsername() {
        init();
        HybridAppStabilizer.ensureWebView(driver);
        registerPage.setUsername(currentData.get("username"));
    }

    @And("User clicks on Next button again for digital banking")
    public void clickNextAgain() {
        init();
        registerPage.clickNext();
    }

    @Then("User registration should be completed for digital banking")
    public void verifyRegistration() {
        init();
        registerPage.verifySuccess();
    }
}