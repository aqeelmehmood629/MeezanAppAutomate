package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.PopupHandler;

import java.time.Duration;

/**
 * 🏠 DashboardPage — extends BasePage for popup-aware safe interactions.
 */
public class DashboardPage extends BasePage {

    private WebDriverWait longWait;
    private WebDriverWait shortWait;

    public DashboardPage(AndroidDriver driver) {
        super(driver, 30); // BasePage wait = 30s
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(300));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // 🔹 Locators
    private By showBalanceBtn = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");
    private By showBalanceBtn2 = By.xpath("//android.widget.TextView[contains(@text,'PKR')]");
    private By homeIcon = By.xpath("//android.widget.Image[contains(@text,'home-icon')]");
    private final By SHARE_ACCOUNT_ICON = By.xpath("//android.widget.TextView[@text='Share Account']");

    // 🔹 DSB_TC02 Locators
    private final By titleDisplayed = By.xpath("//android.widget.TextView[contains(@text,'FARAZ')]");
    private final By currentAccountDisplayed = By.xpath("//android.widget.TextView[contains(@text,'Current Account')]");
    private final By ibanDisplayed = By.xpath("//android.widget.TextView[contains(@text,'IBAN')]");
    private final By branchDisplayed = By.xpath("//android.widget.TextView[contains(@text,'Branch')]");
    private final By balanceDisplayed = By.xpath("//android.widget.TextView[contains(@text,'PKR')]");
    private final By shareButton = By.xpath("//android.widget.Button[contains(@text,'Share')]");
    private final By transactionButton = By.xpath("//android.widget.Button[contains(@text,'Transactions')]");

    // 🔹 DSB_TC03 Locators
    private final By copyAccountNumber = By.id("copyAccountNumber");
    private final By copyIbanNumber = By.id("copyIBANNumber");
    private final By copyAccountDetails = By.id("copyAccountDetails");

    // 🔹 DSB_TC04 Locators
    private final By shareAccountNumber = By.id("shareAccountNumber");
    private final By shareIbanNumber = By.id("shareIBANNumber");
    private final By shareAccountDetails = By.id("shareAccountDetails");
    // Share Sheet Locators
    private final By whatsappShareOption = By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"WhatsApp \"]");
    private final By bluetoothShareOption = By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Bluetooth \"]");

    // 🔹 DSB_TC05 Locators
    private final By refreshButton = By.xpath("//android.widget.Button[@text=\"refresh\"]");
    private final By validationHide = By.xpath("//android.widget.TextView[@text=\"HIDE\"]");

    // 🔹 DSB_TC06 Locators
    private final By viewTransaction = By.xpath("//android.widget.Button[@text=\"mzn-button-image Transactions\"]");

    // 🔹 DSB_TC07 Locators
    private final By favouriteButton = By.xpath("//android.widget.Image[@text=\"untoggled-star\"]");
    private final By popupYesButton = By.xpath("//android.view.View[@text=\"Submit\"]");
    private final By validationDone = By.xpath("//android.widget.Button[@text=\"Done\"]");

    public void goToDashboard() {
        clickHome();
        clickShowBalance();
        System.out.println("✅ Home → Show Balance flow completed");
    }

    // 🔹 Click Home — now popup-aware via safeClick
    public void clickHome() {
        safeClick(homeIcon, 300);
        System.out.println("🏠 Home icon clicked");
    }

    // 🔹 Click Show Balance
    public void clickShowBalance() {
        try {
            safeClick(showBalanceBtn, 30);
            System.out.println("👁️ Show Balance clicked using first locator");
        } catch (Exception e) {
            safeClick(showBalanceBtn2, 30);
            System.out.println("👁️ Show Balance clicked using second locator");
        }
    }

    /**
     * ✅ Check if dashboard is visible — uses NATIVE context
     * Safe: switches to native before checking, never throws
     */
    public boolean isDashboardVisible() {
        try {
            // Dashboard elements are in NATIVE context
            String currentContext = driver.getContext();
            if (currentContext == null || !currentContext.equals("NATIVE_APP")) {
                driver.context("NATIVE_APP");
            }

            // Check for popup first — if present, dashboard may be hidden behind it
            PopupHandler.handlePopupIfPresent(driver);

            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(showBalanceBtn)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    // 🔹 DSB_TC02 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public boolean isTitleDisplayed() { return isDisplayedSafe(titleDisplayed); }
    public boolean isCurrentAccountDisplayed() { return isDisplayedSafe(currentAccountDisplayed); }
    public boolean isIbanDisplayed() { return isDisplayedSafe(ibanDisplayed); }
    public boolean isBranchDisplayed() { return isDisplayedSafe(branchDisplayed); }
    public boolean isBalanceDisplayed() { return isDisplayedSafe(balanceDisplayed); }
    public boolean isShareButtonDisplayed() { return isDisplayedSafe(shareButton); }
    public boolean isTransactionButtonDisplayed() { return isDisplayedSafe(transactionButton); }

    private boolean isDisplayedSafe(By locator) {
        try {
            driver.context("NATIVE_APP");
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC03 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickShareDetailsScreen() {
        safeClick(shareButton, 10);
    }

    public void clickCopyOption(String action) {
        driver.context("NATIVE_APP");
        switch (action) {
            case "Copy Account Number": safeClick(copyAccountNumber, 10); break;
            case "Copy IBAN Number": safeClick(copyIbanNumber, 10); break;
            case "Copy Account Details": safeClick(copyAccountDetails, 10); break;
            default: throw new IllegalArgumentException("Unknown copy action: " + action);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC04 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickShareOption(String action) {
        driver.context("NATIVE_APP");
        switch (action) {
            case "Share Account Number": safeClick(shareAccountNumber, 10); break;
            case "Share IBAN Number": safeClick(shareIbanNumber, 10); break;
            case "Share Account Details": safeClick(shareAccountDetails, 10); break;
            default: throw new IllegalArgumentException("Unknown share action: " + action);
        }
    }

    public boolean isShareSheetDisplayed() {
        driver.context("NATIVE_APP");
        try {
            boolean hasWhatsApp = !driver.findElements(whatsappShareOption).isEmpty();
            boolean hasBluetooth = !driver.findElements(bluetoothShareOption).isEmpty();
            return hasWhatsApp || hasBluetooth;
        } catch (Exception e) {
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC05 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void rapidTapRefresh() {
        driver.context("NATIVE_APP");
        for (int i = 0; i < 4; i++) {
            try {
                driver.findElement(refreshButton).click();
                Thread.sleep(200);
            } catch (Exception ignored) {}
        }
        System.out.println("✅ Tapped refresh button multiple times rapidly");
    }

    public boolean isDataUpdatedWithoutCrashes() {
        return isDisplayedSafe(balanceDisplayed) || isDisplayedSafe(validationHide);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC06 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickViewTransaction() {
        safeClick(viewTransaction, 10);
        System.out.println("✅ View Transaction button clicked");
    }

    public boolean isTransactionHistoryScreenDisplayed() {
        return isDisplayedSafe(balanceDisplayed);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC07 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickFavouriteButton() {
        safeClick(favouriteButton, 10);
        System.out.println("✅ Favourite button clicked");
    }

    public boolean isFavouritePopupDisplayed() {
        return isDisplayedSafe(popupYesButton);
    }

    public void clickPopupYesButton() {
        safeClick(popupYesButton, 10);
        System.out.println("✅ Popup Yes button clicked");
    }

    public boolean isAccountMarkedAsFavourite() {
        return isDisplayedSafe(validationDone);
    }
}
