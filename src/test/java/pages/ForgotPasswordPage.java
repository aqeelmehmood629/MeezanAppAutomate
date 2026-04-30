package pages;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ForgotPasswordPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public ForgotPasswordPage() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(250));
    }

    // ========= LOCATORS =========

    private By forgotPasswordBtn = By.xpath("//android.widget.TextView[@text=\"Forgot Password?\"]");
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
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordBtn)).click();
    }

    public void enterCNIC(String cnic) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(cnicField));
        el.clear();
        el.sendKeys(cnic);
    }

    public void enterAccountNumber(String acc) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(accountField));
        el.clear();
        el.sendKeys(acc);
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
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

            WebElement el = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(fields[i])
            );

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
        wait.until(ExpectedConditions.elementToBeClickable(processBtn)).click();
    }

    public void enterNewPassword(String pass) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordField));
        el.sendKeys(pass);
    }

    public void reEnterPassword(String pass) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField));
        el.sendKeys(pass);
    }

    public void clickResetPassword() {
        wait.until(ExpectedConditions.elementToBeClickable(resetBtn)).click();
    }
    public void verifySuccessMsg() {
        wait.until(ExpectedConditions.elementToBeClickable(verifySuccessMsgBtn)).click();
    }
}