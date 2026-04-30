package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;

import java.time.Duration;

public class FundsTransferPage {

    private AndroidDriver driver;
    private WebDriverWait wait,wait1;

    public FundsTransferPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
        this.wait1 = new WebDriverWait(driver, Duration.ofSeconds(5));
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
    public void selectOwnAccount() {

        wait.until(ExpectedConditions.elementToBeClickable(ownAccount)).click();
        waitForPageLoad();

        System.out.println("✅ Own Account Selected");
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
        WebElement dropdown = wait1.until(ExpectedConditions.elementToBeClickable(purposeDropdown));
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

            WebElement purposeOptionEl = wait1.until(ExpectedConditions.elementToBeClickable(purposeOption(purpose)));
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
    public void waitForPurposeScreen() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(purposeDropdown));
    }
    public void selectPurpose(String type, String value) {

        if (type.equalsIgnoreCase("FT")) {

            // 🔹 FT: SEARCH + SELECT
            WebElement search = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(searchBox)
            );

            search.click();
            search.clear();
            search.sendKeys(value);

            By option = By.xpath("//android.widget.TextView[@text='" + value + "']");

            wait.until(ExpectedConditions.elementToBeClickable(option)).click();

            System.out.println("✅ FT purpose selected: " + value);

        } else if (type.equalsIgnoreCase("FTOwn")) {

            // 🔹 FTOwn: ONLY DROPDOWN + SELECT (NO SEARCH)

            By dropdown = By.xpath("//android.widget.EditText[contains(@resource-id,'bottomButton')]");

            wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();

            By option = By.xpath("//android.widget.TextView[@text='" + value + "']");

            wait.until(ExpectedConditions.elementToBeClickable(option)).click();

            System.out.println("✅ FTOwn purpose selected: " + value);
        }
    }
    public void openFTOwnPurposeDropdown() {

        driver.context("NATIVE_APP");

        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.EditText[contains(@resource-id,'bottomButton')]")
                )
        );

        dropdown.click();
        System.out.println("✅ FTOwn purpose dropdown opened");
    }
    public void openRaastPayment() {
        wait.until(ExpectedConditions.elementToBeClickable(raastPymentBtn)).click();
        waitForPageLoad();
        System.out.println("✅ Raast Payment Screen Opened");
    }
    
    public void raastSendNowBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(raastSendnowBtn)).click();
        waitForPageLoad();
        System.out.println("✅ Raast Send now Button Clicked");
}

    public void raastclickpurposeDropdown() {
        wait1.until(ExpectedConditions.elementToBeClickable(raastclickpurposeDropdownBtn)).click();
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
    	
    	wait.until(ExpectedConditions.elementToBeClickable(showBal)).click();
    	System.out.println("✅ Show Balance Clicked");
        
    	
    }
    
}


