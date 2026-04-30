package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {

    private AndroidDriver driver;
    private WebDriverWait wait,wait1;
    public DashboardPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(300));
        this.wait1 = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // 🔹 Locator for Show Balance button
    private By showBalanceBtn = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");
    private By homeIcon = By.xpath("//android.widget.Image[@text=\"home-icon-purple\"]");
    
    public void goToDashboard() {
        clickHome();
        clickShowBalance();
        System.out.println("✅ Home → Show Balance flow completed");
    }

    // 🔹 Click Home
    public void clickHome() {
        wait.until(ExpectedConditions.elementToBeClickable(homeIcon)).click();
        System.out.println("🏠 Home icon clicked");
    }

    // 🔹 Click Show Balance
    public void clickShowBalance() {
        wait.until(ExpectedConditions.elementToBeClickable(showBalanceBtn)).click();
        System.out.println("👁️ Show Balance clicked");
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

            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(showBalanceBtn)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}