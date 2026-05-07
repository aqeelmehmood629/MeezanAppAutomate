package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class ChangeOTPPreferencePage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public ChangeOTPPreferencePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Locator 1
    private By sideMenu = By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    // Locator 2
    private By settingsButton = By.xpath("//android.widget.TextView[@text='Settings']");

    // Locator 3
    private By changeOTPPreference = By.xpath("//android.widget.TextView[@text='Change OTP Preference']");

    // Locator 4


    // Locator 5
    private By saveChangesButton = By.xpath("//span[normalize-space()='Save Changes']");

    // Locator 6
    private By successPopupMessage = By.xpath("//span[contains(text(),'Your OTP Preference has been succesfully changed')]");

    public void clickSideMenu() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(sideMenu));
        element.click();
        System.out.println("Clicked Side Menu");
    }

    public void clickSettings() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(settingsButton));
        element.click();
        System.out.println("Clicked Settings");
    }

    public void clickChangeOTPPreference() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(changeOTPPreference));
        element.click();
        System.out.println("Clicked Change OTP Preference");
    }

    public void selectsOptions() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    WebElement sms = driver.findElement(By.xpath("//input[@id='SMS|rb']"));
    WebElement email = driver.findElement(By.xpath("//input[@id='Email|rb']"));

    boolean isSmsSelected = sms.isSelected();

    if (isSmsSelected) {
        // SMS is selected → switch to EMAIL
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", email);

    } else {
        // EMAIL is selected → switch to SMS
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", sms);
    }
    }

    public void clickSaveChanges() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saveChangesButton));
        element.click();
        System.out.println("Clicked Save Changes");
    }

    public void verifyOTPPreferenceUpdated() {
        WebElement element = wait.until(
            ExpectedConditions.visibilityOfElementLocated(successPopupMessage));

    if (element.isDisplayed()) {

        System.out.println("OTP Preference updated successfully");

        // Click Done button
        WebElement doneBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[text()='Done']"))
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", doneBtn);

    } else {
         System.out.println("OTP Preference update failed");
    }
}
}
