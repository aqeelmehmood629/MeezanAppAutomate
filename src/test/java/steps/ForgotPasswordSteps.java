package steps;

import io.cucumber.java.en.*;
import pages.ForgotPasswordPage;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;

public class ForgotPasswordSteps {

    ForgotPasswordPage page;

    private void init() {
        if (page == null) {
            page = new ForgotPasswordPage();
        }
    }

    private List<Map<String, String>> csvData;
    private Map<String, String> currentData;

    @Given("user clicks on Forgot Password")
    public void click_forgot_password() {
        init();

        if (csvData == null) {
            csvData = CSVUtils.getAllData("src/test/resources/ForgotPasswordData.csv");
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