package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ChangeOTPPreferencePage;
import utils.HybridAppStabilizer;

public class ChangeOTPPreferenceSteps {

    private AndroidDriver driver;
    private ChangeOTPPreferencePage otpPage;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (otpPage == null && driver != null) {
            otpPage = new ChangeOTPPreferencePage(driver);
        }
    }

    @When("user clicks on Side Menu for otp")
    public void user_clicks_on_side_menu() {
        init();
        otpPage.clickSideMenu();
    }

    @And("user clicks on Settings for otp")
    public void user_clicks_on_settings() {
        init();
        otpPage.clickSettings();
    }

    @And("user clicks on Change OTP Preference")
    public void user_clicks_on_change_otp_preference() {
        init();
        otpPage.clickChangeOTPPreference();
    }

    @And("user selects option for otp")
    public void user_selects_option() {
        init();
        HybridAppStabilizer.ensureWebView(driver);
        otpPage.selectsOptions();
    }

    @And("user clicks on Save Changes for otp")
    public void user_clicks_on_save_changes() {
        init();
        otpPage.clickSaveChanges();
    }

    @Then("OTP preference should be updated successfully")
    public void otp_preference_should_be_updated_successfully() {
        init();
        otpPage.verifyOTPPreferenceUpdated();
    }
}