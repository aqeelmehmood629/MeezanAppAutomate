package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.CSVUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.HybridAppStabilizer;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;

public class IBFTPage {

    AndroidDriver driver;
    WebDriverWait wait;

    public IBFTPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // =========================
    // LOCATORS
    // =========================

    By sendMoney = By.xpath("//android.widget.Button[@text='Send Money']");
    By newAccount = By.xpath("//android.widget.Button[@text='Send Money To A New Account']");
    By searchBank = By.xpath("//android.widget.EditText[@resource-id='search-box']");
    By accountField = By.xpath("//android.widget.EditText[@resource-id='mznInput|input']");
    By fetchAccountDetails = By.xpath("//android.widget.Button[@text=\"Fetch Account Details\"]");
    By next = By.xpath("//android.widget.Button[@text='Next']");
    By amountField = By.xpath("//android.widget.EditText[@resource-id='mznInput|input']");
    By sendNow = By.xpath("//android.widget.Button[@text='Send Now']");
    By successMsg = By.xpath("//android.widget.TextView[@text='Transaction Successful']");

    // =========================
    // BASIC ACTION METHODS
    // =========================

    public void clickSendMoneyIBFT() {
        wait.until(ExpectedConditions.elementToBeClickable(sendMoney)).click();
    }

    public void clickNewAccountIBFT() {
        wait.until(ExpectedConditions.elementToBeClickable(newAccount)).click();
    }

    public void searchBankIBFT(String bankName) {
        HybridAppStabilizer.ensureNative(driver);

        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchBank));

        search.click();
        search.clear();

        // 1. Enter full bank name (Controlled way)
        search.sendKeys(bankName);
        
        try {
            Thread.sleep(800); // Small pause before backspace
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Remove one character from the end to trigger list refresh (Using Native Key Event)
        driver.pressKey(new KeyEvent(AndroidKey.DEL));

        try {
            Thread.sleep(1000); // Wait for list to populate after backspace
        } catch (Exception e) {}

        // 3. Wait until the correct bank appears in the list (Case-insensitive XPath)
        String lowerBankName = bankName.toLowerCase();
        By bankResult = By.xpath(
                "//android.widget.TextView[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + lowerBankName + "')]"
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(bankResult));
        System.out.println("✅ Bank list refreshed and '" + bankName + "' is visible.");
    }

    // 🔥 FIXED: Dynamic bank selection with Case-Insensitive XPath
    public void selectBankIBFT(String bankName) {
        String lowerBankName = bankName.toLowerCase();
        By bank = By.xpath("//android.widget.TextView[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + lowerBankName + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(bank)).click();
    }

    public void enterAccountIBFT(String account) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(accountField));
        element.click();
        element.clear();
        element.sendKeys(account);
        HybridAppStabilizer.hideKeyboard(driver);
    }
    
    public void fetchDetails() {
    	wait.until(ExpectedConditions.elementToBeClickable(fetchAccountDetails)).click();
    }

    public void clickNextIBFT() {
        wait.until(ExpectedConditions.elementToBeClickable(next)).click();
    }

    public void enterAmountIBFT(String amount) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(amountField));
        element.click();
        element.clear();
        element.sendKeys(amount);
        HybridAppStabilizer.hideKeyboard(driver);
    }

    public void clickSendNowIBFT() {
        wait.until(ExpectedConditions.elementToBeClickable(sendNow)).click();
    }

    // =========================
    // CSV FLOW METHOD (FIXED)
    // =========================

    public void performIBFT(String csvPath) {

        List<Map<String, String>> data = CSVUtils.getAllData(csvPath);

        for (Map<String, String> row : data) {

            clickSendMoneyIBFT();
            clickNewAccountIBFT();

            String bankName = row.get("BankName");
            String account = row.get("account");
            String amount = row.get("amount");

            searchBankIBFT(bankName);
            selectBankIBFT(bankName);   // 🔥 FIXED

            enterAccountIBFT(account);
            clickNextIBFT();

            enterAmountIBFT(amount);
            clickNextIBFT();

            clickSendNowIBFT();
        }
    }

    public boolean isTransactionSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}