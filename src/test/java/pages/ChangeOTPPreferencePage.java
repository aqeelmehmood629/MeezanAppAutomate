package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

public class ChangeOTPPreferencePage extends BasePage {

    public ChangeOTPPreferencePage(AndroidDriver driver) {
        super(driver);
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
        safeClick(sideMenu);
        System.out.println("Clicked Side Menu");
    }

    public void clickSettings() {
        safeClick(settingsButton);
        System.out.println("Clicked Settings");
    }

    public void clickChangeOTPPreference() {
        safeClick(changeOTPPreference);
        System.out.println("Clicked Change OTP Preference");
    }

    public void selectsOptions() {

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
        safeClick(saveChangesButton);
        System.out.println("Clicked Save Changes");
    }

    public void verifyOTPPreferenceUpdated() {
        safeWait(successPopupMessage);
        WebElement element = driver.findElement(successPopupMessage);

    if (element.isDisplayed()) {

        System.out.println("OTP Preference updated successfully");

        // Click Done button
        By doneBtnLoc = By.xpath("//span[text()='Done']");
        safeWait(doneBtnLoc);
        WebElement doneBtn = driver.findElement(doneBtnLoc);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", doneBtn);

    } else {
         System.out.println("OTP Preference update failed");
    }
}
}
