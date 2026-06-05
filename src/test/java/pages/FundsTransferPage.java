package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;
import utils.HybridAppStabilizer;

import java.time.Duration;

public class FundsTransferPage extends BasePage {

    public FundsTransferPage(AndroidDriver driver) {
        super(driver);
    }

    // ================= LOCATORS =================
    private By sendMoneyBtn = By.xpath("//android.widget.Button[@text='Send Money']");
    private By amountField = By.xpath("//android.widget.EditText[contains(@resource-id,'mznInput') or contains(@resource-id,'input')]");
    private By nextBtn = By.xpath("//android.widget.Button[@text='Next']");
    private By sendNowBtn = By.xpath("//android.widget.Button[@text='Send Now']");
    private By successMsg = By.xpath("//android.widget.TextView[@text='Transaction Successful']");
    private By purposeDropdown = By.xpath("//android.widget.TextView[contains(@text,'Others')]");
    private By searchBox = By.xpath("//android.widget.EditText[@resource-id='search-box']");
    private By ownAccount = By.xpath("//android.widget.Button[@text='Own Account']");
    private By ftOwnPurposeDropdown =
            By.xpath("//android.widget.EditText[contains(@resource-id,'bottomButton')]");
    
    private By raastPymentBtn = By.xpath("//android.widget.Button[@text=\"Raast Payment\"]");
    
    private By raastSendnowBtn = By.xpath("//android.widget.Button[@text='Send Now']");
    private By raastclickpurposeDropdownBtn = By.xpath("//android.widget.Image[@text=\"arrow-drop-down\"]");
    
    private By homeBtn = By.xpath("//android.widget.Image[@text='home-icon-purple']");
    private By showBal = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");
    
    

    private By accountOption(String account) {
        return By.xpath("//android.widget.TextView[@text='" + account + "']");
    }

    private By purposeOption(String purpose) {
        return By.xpath("//android.widget.TextView[@text='" + purpose + "']");
    }

    // ================= DASHBOARD =================
    public void waitForDashboardToLoad() {
        safeWait(sendMoneyBtn, 250);
    }

    public boolean isSendMoneyButtonVisible() {
        try {
            return safeWait(sendMoneyBtn, 250).isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Send Money not visible");
            return false;
        }
    }

    public void clickSendMoney() {
        safeClick(sendMoneyBtn, 250);
        waitForPageLoad();
        System.out.println("✅ Clicked Send Money");
    }
    public void selectOwnAccount() {

        safeClick(ownAccount, 250);
        waitForPageLoad();

        System.out.println("✅ Own Account Selected");
    }

    // ================= ACCOUNT & AMOUNT =================
    public void selectAccount(String account) {
        safeClick(accountOption(account), 250);
        waitForPageLoad();
        System.out.println("✅ Account Selected: " + account);
    }

    public void enterAmount(String amount) {
        safeSendKeys(amountField, amount, 250);
        System.out.println("✅ Amount Entered: " + amount);
    }

    // ================= PURPOSE DROPDOWN =================
    public void clickPurposeDropdown() {
        ensureNativeContext();
        safeClick(purposeDropdown, TIMEOUT_SHORT);
    }

    public void enterPurposeOfTransfer(String purpose) {
        safeSendKeys(searchBox, purpose, 250);
        System.out.println("✅ Purpose typed: " + purpose);
    }

    public void selectPurposeDynamically(String purpose) {
        try {
            ensureNativeContext();

            driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                    ".scrollIntoView(new UiSelector().text(\"" + purpose + "\"))"
            ));

            safeClick(purposeOption(purpose), TIMEOUT_SHORT);
            System.out.println("✅ Purpose Selected Dynamically: " + purpose);

        } catch (Exception e) {
            throw new RuntimeException("Purpose not found: " + purpose + " - " + e.getMessage());
        }
    }

    // ================= NEXT & SEND =================
    public void clickNext() {
        safeClick(nextBtn, 250);
    }

    public void clickSendNow() {
        safeClick(sendNowBtn, 250);
    }

    public boolean isTransactionSuccessful() {
        try {
            safeWait(successMsg, 250);
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
    public void waitForPurposeScreen() {
        safeWait(purposeDropdown, 250);
    }
    public void selectPurpose(String type, String value) {

        if (type.equalsIgnoreCase("FT")) {

            // 🔹 FT: SEARCH + SELECT
            safeSendKeys(searchBox, value, 250);

            By option = By.xpath("//android.widget.TextView[@text='" + value + "']");

            safeClick(option, 250);

            System.out.println("✅ FT purpose selected: " + value);

        } else if (type.equalsIgnoreCase("FTOwn")) {

            // 🔹 FTOwn: ONLY DROPDOWN + SELECT (NO SEARCH)

            By dropdown = By.xpath("//android.widget.EditText[contains(@resource-id,'bottomButton')]");

            safeClick(dropdown, 250);

            By option = By.xpath("//android.widget.TextView[@text='" + value + "']");

            safeClick(option, 250);

            System.out.println("✅ FTOwn purpose selected: " + value);
        }
    }
    public void openFTOwnPurposeDropdown() {

        ensureNativeContext();

        safeClick(By.xpath("//android.widget.EditText[contains(@resource-id,'bottomButton')]"), 250);

        System.out.println("✅ FTOwn purpose dropdown opened");
    }
    public void openRaastPayment() {
        safeClick(raastPymentBtn, 250);
        waitForPageLoad();
        System.out.println("✅ Raast Payment Screen Opened");
    }
    
    public void raastSendNowBtn() {
        safeClick(raastSendnowBtn, 250);
        waitForPageLoad();
        System.out.println("✅ Raast Send now Button Clicked");
}

    public void raastclickpurposeDropdown() {
        safeClick(raastclickpurposeDropdownBtn, TIMEOUT_SHORT);
        waitForPageLoad();
        System.out.println("✅ Raast Payment click purpose dropdown");
}
    public void clickHomeBtn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        for (int i = 0; i < 3; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.accessibilityId("home-icon-purple")
                )).click();
                return;
            } catch (Exception e) {
                System.out.println("Retry clicking home button: " + i);
            }
        }

        throw new RuntimeException("Home button not clickable after retries");
    }
    public void clickShowBalance() {
    	
    	safeClick(showBal, 250);
    	System.out.println("✅ Show Balance Clicked");
        
    	
    }
    
}


