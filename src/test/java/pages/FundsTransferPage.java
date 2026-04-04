package pages;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;

import java.time.Duration;

public class FundsTransferPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public FundsTransferPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ================= LOCATORS =================
    private By sendMoneyBtn = By.xpath("//android.widget.Button[@text='Send Money']");
    private By amountField = By.xpath("//android.widget.EditText[contains(@resource-id,'mznInput') or contains(@resource-id,'input')]");
    private By nextBtn = By.xpath("//android.widget.Button[@text='Next']");
    private By sendNowBtn = By.xpath("//android.widget.Button[@text='Send Now']");
    private By successMsg = By.xpath("//android.widget.TextView[@text='Transaction Successful']");
    private By purposeDropdown = By.xpath("//android.widget.TextView[contains(@text,'Others')]");
    private By searchBox = By.xpath("//android.widget.EditText[@resource-id='search-box']");

    private By accountOption(String account) {
        return By.xpath("//android.widget.TextView[@text='" + account + "']");
    }

    private By purposeOption(String purpose) {
        return By.xpath("//android.widget.TextView[@text='" + purpose + "']");
    }

    // ================= DASHBOARD =================
    public void waitForDashboardToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sendMoneyBtn));
    }

    public boolean isSendMoneyButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(sendMoneyBtn)).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Send Money not visible");
            return false;
        }
    }

    public void clickSendMoney() {
        wait.until(ExpectedConditions.elementToBeClickable(sendMoneyBtn)).click();
        waitForPageLoad();
        System.out.println("✅ Clicked Send Money");
    }

    // ================= ACCOUNT & AMOUNT =================
    public void selectAccount(String account) {
        wait.until(ExpectedConditions.elementToBeClickable(accountOption(account))).click();
        waitForPageLoad();
        System.out.println("✅ Account Selected: " + account);
    }

    public void enterAmount(String amount) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(amountField));
        el.clear();
        el.sendKeys(amount);
        System.out.println("✅ Amount Entered: " + amount);
    }

    // ================= PURPOSE DROPDOWN =================
    public void clickPurposeDropdown() {
        driver.context("NATIVE_APP");
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(purposeDropdown));
        dropdown.click();
    }

    public void enterPurposeOfTransfer(String purpose) {
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        search.click();
        search.clear();
        search.sendKeys(purpose);
        System.out.println("✅ Purpose typed: " + purpose);
    }

    public void selectPurposeDynamically(String purpose) {
        try {
            driver.context("NATIVE_APP");

            driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                    ".scrollIntoView(new UiSelector().text(\"" + purpose + "\"))"
            ));

            WebElement purposeOptionEl = wait.until(ExpectedConditions.elementToBeClickable(purposeOption(purpose)));
            purposeOptionEl.click();
            System.out.println("✅ Purpose Selected Dynamically: " + purpose);

        } catch (Exception e) {
            throw new RuntimeException("Purpose not found: " + purpose + " - " + e.getMessage());
        }
    }

    // ================= NEXT & SEND =================
    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
    }

    public void clickSendNow() {
        wait.until(ExpectedConditions.elementToBeClickable(sendNowBtn)).click();
    }

    public boolean isTransactionSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================= NAVIGATION =================
    public void navigateBackToDashboard() {
        driver.navigate().back();
        waitForPageLoad();
        driver.navigate().back();
        waitForPageLoad();
    }

    // ================= UTIL =================
    private void waitForPageLoad() {
        try {
            Thread.sleep(1000); // short wait for transition
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}