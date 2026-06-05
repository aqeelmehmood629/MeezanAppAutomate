package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SadqaZakatPage extends BasePage {

    public SadqaZakatPage(AndroidDriver driver) {
        super(driver);
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
        safeClick(zakatsadqaBtn, 250);
    }
    public void clickSadqaTab() {
        safeClick(sadqaTab, 250);
    }

    public void searchFoundation(String foundation) {
        WebElement element = safeWait(searchBox, 250);
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

        safeWait(locator, 250);
        safeClick(locator, 250);
    }
    public void enterAmount(String amount) {
        safeSendKeys(amountField, amount, 250);
    }

    public void clickNext() {
        safeClick(nextBtn, 250);
    }

    public void clickSadqaNow() {
        safeClick(paySadqaBtn, 250);
    }
    
    public void clickZakatNow() {
        safeClick(payZakatBtn, 250);
    }

    public void waitForDonationSuccess() {
        safeWait(donationSuccessMsg, 250);
    }
}