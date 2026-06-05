package pages;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class ForgotPasswordPage extends BasePage {

    public ForgotPasswordPage(AndroidDriver driver) {
        super(driver);
    }

    // ========= LOCATORS =========

    private By forgotPasswordBtn = By.xpath("//android.widget.TextView[@text='Forgot Password?']");
    private By cnicField = By.xpath("//android.widget.EditText[@resource-id=\"partyId|input\"]");
    private By accountField = By.xpath("//android.widget.EditText[@resource-id=\'accountNumber|input']");
    private By nextBtn = By.xpath("//android.widget.Button[@text=\"Next\"]");
    private By otpField1 = By.xpath("//android.widget.EditText[@resource-id=\"otp_1|input\"]");
    private By otpField2 = By.xpath("//android.widget.EditText[@resource-id=\"otp_2|input\"]");
    private By otpField3 = By.xpath("//android.widget.EditText[@resource-id=\"otp_3|input\"]");
    private By otpField4 = By.xpath("//android.widget.EditText[@resource-id=\"otp_4|input\"]");
    private By otpField5 = By.xpath("//android.widget.EditText[@resource-id=\"otp_5|input\"]");
    private By otpField6 = By.xpath("//android.widget.EditText[@resource-id=\"otp_6|input\"]");
    private By processBtn = By.xpath("//android.widget.TextView[@text=\"Proceed\"]");
    private By newPasswordField = By.xpath("//android.widget.EditText[@resource-id=\"newpwd|input\"]");
    private By confirmPasswordField = By.xpath("//android.widget.EditText[@resource-id=\"conpwd|input\"]");
    private By resetBtn = By.xpath("//android.widget.Button[@text=\"Reset Password\"]");
    private By verifySuccessMsgBtn = By.xpath("//android.widget.TextView[@text=\"Your Password has been reset successfully!\"]");

    // ========= ACTIONS =========

    public void clickForgotPassword() {
        safeClick(forgotPasswordBtn, 250);
    }

    public void enterCNIC(String cnic) {
        safeSendKeys(cnicField, cnic, 250);
    }

    public void enterAccountNumber(String acc) {
        safeSendKeys(accountField, acc, 250);
    }

    public void clickNext() {
        safeClick(nextBtn, 250);
    }
    public void enterOTP(String otp) {

        By[] fields = new By[]{
                otpField1, otpField2, otpField3,
                otpField4, otpField5, otpField6
        };

        if (otp == null || otp.length() != 6) {
            throw new IllegalArgumentException("OTP must be 6 digits");
        }

        Actions actions = new Actions(driver);

        for (int i = 0; i < 6; i++) {

            safeWait(fields[i], 250);
            WebElement el = driver.findElement(fields[i]);

            el.click();

            actions
                    .moveToElement(el)
                    .click()
                    .sendKeys(String.valueOf(otp.charAt(i)))
                    .perform();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}

        System.out.println("✅ OTP Entered via Actions: " + otp);
    }

    public void clickProcess() {
        safeClick(processBtn, 250);
    }

    public void enterNewPassword(String pass) {
        safeSendKeys(newPasswordField, pass, 250);
    }

    public void reEnterPassword(String pass) {
        safeSendKeys(confirmPasswordField, pass, 250);
    }

    public void clickResetPassword() {
        safeClick(resetBtn, 250);
    }
    public void verifySuccessMsg() {
        safeClick(verifySuccessMsgBtn, 250);
    }
}