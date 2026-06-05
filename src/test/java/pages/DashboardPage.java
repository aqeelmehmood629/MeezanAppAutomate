package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
}