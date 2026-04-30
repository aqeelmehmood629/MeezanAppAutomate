package steps;

import io.cucumber.java.en.*;
import pages.ChangePasswordPage;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;

public class ChangePasswordSteps {

    ChangePasswordPage page;

    private void init() {
        if (page == null) {
            page = new ChangePasswordPage();
        }
    }

    List<Map<String, String>> data;
    String currentPwd;
    String newPwd;
    String confirmPwd;

    @Given("user is on dashboard screen for change password")
    public void user_is_on_dashboard_screen_for_change_password() {
        init();
        System.out.println("User is on dashboard");
    }

    @When("user clicks on side menu for change password")
    public void user_clicks_on_side_menu_for_change_password() {
        init();
        page.clickSideMenu();
    }

    @And("user clicks on Settings for change password")
    public void user_clicks_on_settings_for_change_password() {
        init();
        page.clickSettings();
    }

    @And("user clicks on Change Password")
    public void user_clicks_on_change_password() {
        init();
        page.clickChangePassword();
    }

    @And("user reads test data from CSV for change password")
    public void user_reads_test_data_from_csv_for_change_password() {
        init();

        data = CSVUtils.getAllData("src/test/resources/ChangePassword.csv");

        Map<String, String> row = data.get(0);

        currentPwd = row.get("currentPassword");
        newPwd = row.get("newPassword");
        confirmPwd = row.get("confirmPassword");
    }

    @And("user enters current password for change password")
    public void user_enters_current_password_for_change_password() {
        init();
        page.enterCurrentPassword(currentPwd);
    }

    @And("user enters new password for change password")
    public void user_enters_new_password_for_change_password() {
        init();
        page.enterNewPassword(newPwd);
    }

    @And("user re-enters new password for change password")
    public void user_reenters_new_password_for_change_password() {
        init();
        page.enterConfirmPassword(confirmPwd);
    }
    @And("user clicks on submit button for change password")
    public void user_clicks_on_submit_button_for_change_password() {
        init();
    	page.clickChangePasswordSubmit();
    }

    @And("password fields should be filled successfully")
    public void password_fields_should_be_filled_successfully() {
        init();
        page.verifyFieldsFilled();
    }
    @Then("user clicks home icon after reset Password")
    public void homeIconpassword() {
        init();
    	page.clickHomeIconPassword();
    }
}