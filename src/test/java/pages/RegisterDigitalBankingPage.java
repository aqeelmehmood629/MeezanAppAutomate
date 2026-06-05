package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class RegisterDigitalBankingPage extends BasePage {

    public RegisterDigitalBankingPage(AndroidDriver driver) {
        super(driver);
    }

    // Locators
    private By registerBtn = By.xpath("//a[@role='tab']//span[normalize-space()='Register']");
  //  private By registerDigitalBanking = By.xpath("//*[contains(normalize-space(.),'Register for Digital Banking')]");

    private By nicInput = By.xpath("//input[@placeholder='XXXXX-XXXXXXX-X']");
    private By accountInput = By.xpath("//input[@placeholder='XXXXXXXXXXXXXX']");
    private By nextBtn = By.xpath("//button[.//div[normalize-space()='Next']]");
    private By otpInput = By.xpath("//input[@type='text' or @type='tel']");
    private By usernameInput = By.xpath("//input[@id='username|input']");
    private By successMsg = By.xpath("//div[contains(@class,'popup-header-height') and normalize-space()='Login Restricted']");
    private By otpField1 = By.xpath("//android.widget.EditText[@resource-id=\"otp_1|input\"]");
    private By otpField2 = By.xpath("//android.widget.EditText[@resource-id=\"otp_2|input\"]");
    private By otpField3 = By.xpath("//android.widget.EditText[@resource-id=\"otp_3|input\"]");
    private By otpField4 = By.xpath("//android.widget.EditText[@resource-id=\"otp_4|input\"]");
    private By otpField5 = By.xpath("//android.widget.EditText[@resource-id=\"otp_5|input\"]");
    private By otpField6 = By.xpath("//android.widget.EditText[@resource-id=\"otp_6|input\"]");
    private By processBtn = By.xpath("//android.widget.TextView[@text=\"Proceed\"]");
  //  private By newPasswordField = By.xpath("//android.widget.EditText[@resource-id=\"newpwd|input\"]");

    // Actions
    public void clickRegister() {
        safeClick(registerBtn, TIMEOUT_LONG);
    }

    public void clickRegisterDigitalBanking() {
    By registerDigitalBanking =
            By.xpath("//span[contains(@class,'options') and contains(normalize-space(.),'Register for Digital Banking')]");

    safeClick(registerDigitalBanking, TIMEOUT_SHORT);
    }

    public void enterNic(String nic) {
        safeSendKeys(nicInput, nic, TIMEOUT_LONG);
    }

    public void enterAccount(String acc) {
        safeSendKeys(accountInput, acc, TIMEOUT_LONG);
    }

    public void clickNext() {
        safeClick(nextBtn, TIMEOUT_LONG);
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

            WebElement el = safeWait(fields[i], TIMEOUT_LONG);

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
    }

    public void setUsername(String username) {
        safeSendKeys(usernameInput, username, TIMEOUT_LONG);
    }

    public void verifySuccess() {
        Assert.assertTrue(
                safeWait(successMsg, TIMEOUT_LONG).isDisplayed(),
                "Registration failed"
        );
    }
}