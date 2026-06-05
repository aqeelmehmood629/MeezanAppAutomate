package pages;

import org.openqa.selenium.By;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;

public class IBFTPage extends BasePage {

    public IBFTPage(AndroidDriver driver) {
        super(driver);
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
        safeClick(sendMoney);
    }

    public void clickNewAccountIBFT() {
        safeClick(newAccount);
    }

    public void searchBankIBFT(String bankName) {
        ensureNativeContext();

        WebElement search = safeWait(searchBank);

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

        safeWait(bankResult);
        System.out.println("✅ Bank list refreshed and '" + bankName + "' is visible.");
    }

    // 🔥 FIXED: Dynamic bank selection with Case-Insensitive XPath
    public void selectBankIBFT(String bankName) {
        String lowerBankName = bankName.toLowerCase();
        By bank = By.xpath("//android.widget.TextView[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + lowerBankName + "')]");
        safeClick(bank);
    }

    public void enterAccountIBFT(String account) {
        safeSendKeys(accountField, account);
        hideKeyboard();
    }
    
    public void fetchDetails() {
    	safeClick(fetchAccountDetails);
    }

    public void clickNextIBFT() {
        safeClick(next);
    }

    public void enterAmountIBFT(String amount) {
        safeSendKeys(amountField, amount);
        hideKeyboard();
    }

    public void clickSendNowIBFT() {
        safeClick(sendNow);
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
            safeWait(successMsg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}