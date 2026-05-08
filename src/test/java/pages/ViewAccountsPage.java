package pages;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import utils.HybridAppStabilizer;

public class ViewAccountsPage {

    AndroidDriver driver;
    WebDriverWait wait;

    public ViewAccountsPage() {
        this.driver = DriverFactory.initializeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===== LOCATORS =====

    private By dashboardTitle =
            By.xpath("//android.widget.TextView[@text='FARAZ AHMED KHAN']");
    private By sideMenuBtn =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By myAccountsBtn =
            By.xpath("//android.widget.TextView[@text='My Accounts']");

    private By accountTitle =
            By.xpath("//android.view.View[contains(@resource-id,'label')]");
    private By homeIconAfterViewAccountBtn = 
            By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ===== ACTIONS =====

    // Anchor for waiting to ensure dashboard is fully loaded
    private By showBalanceBtnAnchor = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");

    public String getDashboardAccountTitle() {
        HybridAppStabilizer.ensureNative(driver);
        
        System.out.println("⏳ Waiting for Dashboard to fully load...");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(showBalanceBtnAnchor));
        } catch (Exception e) {
            System.out.println("⚠️ Warning: 'SHOW BALANCE' not found, proceeding anyway...");
        }

        String titleText = "";

        try {
            // Try specific locator first (fallback)
            WebElement el = driver.findElement(dashboardTitle);
            if (el.isDisplayed()) {
                titleText = el.getText().trim();
                System.out.println("✅ Found title using fallback locator.");
            }
        } catch (Exception e) {
            System.out.println("🔄 Specific locator failed, attempting dynamic discovery...");
            // Dynamic approach: Find the first major text view that looks like a name
            java.util.List<WebElement> textViews = driver.findElements(By.xpath("//android.widget.TextView"));
            for (WebElement tv : textViews) {
                String text = tv.getText().trim();
                // Heuristic: Name usually > 5 chars, might have a space, and is not a known button
                if (text.length() > 5 
                    && !text.equalsIgnoreCase("SHOW BALANCE") 
                    && !text.equalsIgnoreCase("Meezan Bank")
                    && !text.equalsIgnoreCase("Home")
                    && !text.equalsIgnoreCase("My Profile")
                    && !text.equalsIgnoreCase("Deals & Discounts")) {
                    
                    titleText = text;
                    System.out.println("✅ Dynamically discovered title: " + text);
                    break;
                }
            }
        }

        if (titleText.isEmpty()) {
            throw new RuntimeException("❌ Dashboard account title could not be found dynamically or statically.");
        }

        // Improved extraction: Handle "Good Morning, FRAZ" or "Welcome FRAZ"
        String lowerText = titleText.toLowerCase();
        if (lowerText.contains("good morning")) titleText = titleText.replaceFirst("(?i)good morning", "").trim();
        else if (lowerText.contains("good afternoon")) titleText = titleText.replaceFirst("(?i)good afternoon", "").trim();
        else if (lowerText.contains("good evening")) titleText = titleText.replaceFirst("(?i)good evening", "").trim();
        else if (lowerText.contains("welcome")) titleText = titleText.replaceFirst("(?i)welcome", "").trim();
        else if (lowerText.contains("hi")) titleText = titleText.replaceFirst("(?i)hi", "").trim();
        
        if (titleText.startsWith(",")) {
            titleText = titleText.substring(1).trim();
        }

        System.out.println("📌 Final Extracted Dashboard Title: " + titleText);
        return titleText;
    }

    public void clickSideMenu() {
        HybridAppStabilizer.ensureNative(driver);
        wait.until(ExpectedConditions.elementToBeClickable(sideMenuBtn)).click();
        System.out.println("✅ Side menu clicked");
    }

    public void clickMyAccounts() {
        HybridAppStabilizer.ensureNative(driver);
        wait.until(ExpectedConditions.elementToBeClickable(myAccountsBtn)).click();
        System.out.println("✅ My Accounts clicked");
    }

    public String getMyAccountTitle() {
        HybridAppStabilizer.ensureNative(driver);
        
        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(accountTitle)
        );

        String text = el.getText().trim();
        if (text.isEmpty()) {
            throw new RuntimeException("❌ My Accounts title is empty");
        }

        System.out.println("📌 Captured My Account Title: " + text);
        return text;
    }

    public void homeIconAfterViewAccount() {
        HybridAppStabilizer.ensureNative(driver);
        wait.until(ExpectedConditions.elementToBeClickable(homeIconAfterViewAccountBtn)).click();
        System.out.println("🏠 Home icon clicked after viewing accounts");
    }
}
    