package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SadqaZakatPage {

    AndroidDriver driver;
    WebDriverWait wait,wait1;

    public SadqaZakatPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
        this.wait1 = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    // 🔹 Locators
    By zakatsadqaBtn = By.xpath("//*[contains(@text,'Zakat') or contains(@text,'Sadqa')]");
    By sadqaTab = By.xpath("//android.widget.TextView[@resource-id=\"sadaqat\"]");
    By searchBox = By.xpath("//android.widget.EditText[@resource-id=\"search-box\"]");
    By amountField = By.xpath("//android.widget.EditText[@resource-id=\"amount|input\"]");
    By nextBtn = By.xpath("//android.widget.Button[@text='Next']");
    By payZakatBtn = By.xpath("//android.widget.Button[contains(@text,'Pay Zakat Now')]");
    By paySadqaBtn = By.xpath("//android.widget.Button[contains(@text,'Pay Sadqa Now')]");
    By donationSuccessMsg = By.xpath("//android.widget.TextView[@text='Transaction Successful']");

    public void clickZakatSadqaBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(zakatsadqaBtn)).click();
    }
    public void clickSadqaTab() {
        wait.until(ExpectedConditions.elementToBeClickable(sadqaTab)).click();
    }

    public void searchFoundation(String foundation) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        element.click();
        
        try { Thread.sleep(500); } catch (Exception e) {} // Wait for keyboard and focus

        // Clear field once at the start
        element.clear();
        foundation = foundation.trim();

        // Type character by character using native keyboard Actions 
        // This ensures text is appended natively without clearing the previous characters
        org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        for (char c : foundation.toCharArray()) {
            actions.sendKeys(String.valueOf(c)).perform();
            try { Thread.sleep(200); } catch (Exception e) {} // Small delay for UI to register
        }

        try { Thread.sleep(1500); } catch (Exception e) {} // Wait for API to fetch results

        try {
            driver.hideKeyboard();
        } catch (Exception e) {}
    }
    public void selectFoundation(String foundation) {
        By locator = By.xpath("//android.widget.TextView[contains(@text,'" + foundation.trim() + "')]");

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    public void enterAmount(String amount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(amountField)).sendKeys(amount);
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
    }

    public void clickSadqaNow() {
        wait.until(ExpectedConditions.elementToBeClickable(paySadqaBtn)).click();
    }
    
    public void clickZakatNow() {
        wait.until(ExpectedConditions.elementToBeClickable(payZakatBtn)).click();
    }

    public void waitForDonationSuccess() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(donationSuccessMsg));
    }
}