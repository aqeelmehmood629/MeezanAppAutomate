package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class RegisterDigitalBankingPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public RegisterDigitalBankingPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(50));
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
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public void clickRegisterDigitalBanking() {
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(5));

    By registerDigitalBanking =
            By.xpath("//span[contains(@class,'options') and contains(normalize-space(.),'Register for Digital Banking')]");

    wait1.until(ExpectedConditions.elementToBeClickable(registerDigitalBanking)).click();
    }

    public void enterNic(String nic) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nicInput)).sendKeys(nic);
    }

    public void enterAccount(String acc) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountInput)).sendKeys(acc);
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
    }

    public void setUsername(String username) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        el.clear();
        el.sendKeys(username);
    }

    public void verifySuccess() {
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg)).isDisplayed(),
                "Registration failed"
        );
    }
}