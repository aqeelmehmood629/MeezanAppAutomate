package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class SendmoneyPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    // ✅ LOCATORS
    private By sendMoneyTitle =
            By.xpath("//android.widget.TextView[@text='Send Money']");

    private By accountNumberField =
            By.xpath("//android.widget.TextView[@text=\"SH-TITLE-SH-TITLE0105385978\"]");

    private By amountField = By.xpath("//android.widget.EditText[@resource-id=\"mznInput\"]");

    private By continueButton =
            By.xpath("//android.widget.Button[@text='Next']");

    private By sendnowButton =
            By.xpath("//android.widget.Button[@text='Send Now']");

    private By searchbox = By.xpath("//android.widget.EditText[@resource-id=\"search-box\"]");

    private By transactiontext =
            By.xpath("//android.widget.TextView[@text='Transaction Successful']");

    // ✅ Dropdown (label ke baad first view)
    private By purposeDropdown =
            By.xpath("//android.widget.TextView[@text='Purpose of Transfer']/following::android.view.View[1]");

    // ✅ Dynamic list items inside dropdown
    private By purposeListItems =
            By.xpath("//android.widget.ListView//android.widget.TextView");

    public SendmoneyPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ✅ VERIFY PAGE
    public boolean isSendMoneyPageDisplayed() {
        try {
            driver.context("NATIVE_APP");
            WebElement title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(sendMoneyTitle));
            return title.isDisplayed();
        } catch (Exception e) {
            System.out.println("Send Money page not visible: " + e.getMessage());
            return false;
        }
    }

    // ✅ SELECT ACCOUNT
    public void selectAccount() {
        WebElement accField = wait.until(
                ExpectedConditions.elementToBeClickable(accountNumberField));
        accField.click();
    }

    // ✅ HIDE KEYBOARD
    public void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            System.out.println("Keyboard already hidden");
        }
    }

    // ✅ CLICK PURPOSE DROPDOWN (clean version)
    public void clickPurposeDropdown() {

        hideKeyboard();

        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(purposeDropdown));

        dropdown.click();
    }

    // ✅ ENTER PURPOSE IN SEARCH BOX
    public void enterPurposeOfTransfer(String purpose) {
        WebElement search = wait.until(
                ExpectedConditions.elementToBeClickable(searchbox));

        search.click();
        search.clear();
        search.sendKeys(purpose);
    }

    // ✅ SELECT PURPOSE DYNAMICALLY FROM LIST
    public void selectPurposeDynamically(String purpose) {
        try {
            // Ensure Native context (dropdown is usually native)
            driver.context("NATIVE_APP");

            // Scroll into view using UiScrollable
            driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().text(\"" + purpose + "\"))"
            ));

            // Click the element directly using exact text
            WebElement purposeOption = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + purpose + "\"]"));
            purposeOption.click();

        } catch (Exception e) {
            throw new RuntimeException("Purpose not found: " + purpose + " - " + e.getMessage());
        }
    }


    // ✅ ENTER AMOUNT
    public void enterAmount(String amount) {
        WebElement amtField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(amountField));

        amtField.click();
        amtField.clear();
        amtField.sendKeys(amount);
    }

    // ✅ CLICK NEXT
    public void tapNext() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(continueButton));
        btn.click();
    }

    // ✅ CLICK SEND NOW
    public void tapSendNow() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(sendnowButton));
        btn.click();
    }

    // ✅ VERIFY TRANSACTION SUCCESS
    public boolean isTransactionSuccessful() {
        try {
            WebElement success = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(transactiontext));
            return success.isDisplayed();
        } catch (Exception e) {
            System.out.println("Transaction not successful: " + e.getMessage());
            return false;
        }
    }
}
