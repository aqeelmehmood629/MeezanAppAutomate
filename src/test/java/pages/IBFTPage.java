package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.CSVUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBFTPage {

    AppiumDriver driver;
    WebDriverWait wait;

    public IBFTPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
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
    	    wait.until(ExpectedConditions.visibilityOfElementLocated(searchBank)).click();

    	    // Step 1: Type full value
    	    driver.findElement(searchBank).clear();
    	    driver.findElement(searchBank).sendKeys(bankName);

    	    // Small wait (UI stabilization)
    	    try { Thread.sleep(500); } catch (Exception e) {}

    	    // Step 2: Clear field
    	    driver.findElement(searchBank).clear();

    	    // Step 3: Remove last character and type again
    	    String modified = bankName.substring(0, bankName.length() - 1);
    	    driver.findElement(searchBank).sendKeys(modified);

    	    // Wait for results
    	    By bankResult = By.xpath("//android.widget.TextView[contains(@text,'" + modified + "')]");
    	    wait.until(ExpectedConditions.visibilityOfElementLocated(bankResult));
    	}

    // 🔥 FIXED: Dynamic bank selection (NO hardcoded value)
    public void selectBankIBFT(String bankName) {

        By bank = By.xpath("//android.widget.TextView[contains(@text,'" + bankName + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(bank)).click();
    }

    public void enterAccountIBFT(String account) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountField))
                .sendKeys(account);
    }
    public void fetchDetails() {
    	wait.until(ExpectedConditions.elementToBeClickable(fetchAccountDetails)).click();
    }

    public void clickNextIBFT() {
        wait.until(ExpectedConditions.elementToBeClickable(next)).click();
    }

    public void enterAmountIBFT(String amount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(amountField))
                .sendKeys(amount);
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