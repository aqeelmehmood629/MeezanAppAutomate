package steps;

import io.cucumber.java.en.*;
import pages.ForgotPasswordPage;
import utils.CSVUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import utils.HybridAppStabilizer;
import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;

public class ForgotPasswordSteps {

    ForgotPasswordPage page;
    private AndroidDriver driver;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (page == null && driver != null) {
            page = new ForgotPasswordPage(driver);
        }
    }

    private List<Map<String, String>> csvData;
    private Map<String, String> currentData;

    @Given("user clicks on Forgot Password")
    public void click_forgot_password() {
        init();
        HybridAppStabilizer.stabilizeApp(driver);
        HybridAppStabilizer.ensureNative(driver);
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (csvData == null) {
            String filePath = "src/test/resources/ForgotPasswordData.csv";
            
            try {
                csvData = CSVUtils.getAllData(filePath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load Forgot Password test data from " + filePath + ". Please ensure the file exists and is readable.", e);
            }
            
            // Safety check to ensure the file isn't empty before trying to access index 0
            if (csvData == null || csvData.isEmpty()) {
                throw new RuntimeException("The CSV file is empty or could not be read properly at path: " + filePath);
            }
        }

        currentData = csvData.get(0); // only 1 row needed
        page.clickForgotPassword();
    }

    @And("user enters cnic from csv for password reset")
    public void enter_cnic() {
        init();
        page.enterCNIC(currentData.get("cnic"));
    }

    @And("user enters account number from csv for password reset")
    public void enter_account() {
        init();
        page.enterAccountNumber(currentData.get("account"));
    }

    @And("user clicks Next button for password reset")
    public void click_next() {
        init();
        page.clickNext();
    }

    @And("user enters otp for password reset")
    public void enter_otp() {
        init();
        page.enterOTP(currentData.get("otp"));
    }
    
    @And("user clicks process button for password reset")
    public void click_process() {
        init();
        page.clickProcess();
    }

    @And("user enters new password for password reset")
    public void enter_new_password() {
        init();
        page.enterNewPassword(currentData.get("newPassword"));
    }

    @And("user re-enters new password for password reset")
    public void reenter_password() {
        init();
        page.reEnterPassword(currentData.get("newPassword"));
    }

    @And("user clicks Reset Password button")
    public void click_reset() {
        init();
        page.clickResetPassword();
    }
    
    @Then("user should see password reset success message")
    public void verifyMsg() {
        init();
        page.verifySuccessMsg();
    }
}