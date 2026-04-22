package pages;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChangeNotificationLanguagePage {

    AndroidDriver driver;
    WebDriverWait wait;

    public ChangeNotificationLanguagePage(AndroidDriver driver) {
    	this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.driver = driver;
    }

    // Locators
    By sideMenuforNotificationBtn = By.xpath("//android.widget.Image[@resource-id='hamBurger']");
    By settingsBtnNotification = By.xpath("//android.widget.TextView[@text='Settings']");
    By manageNotifications = By.xpath("//android.widget.TextView[@text='Manage Notifications']");
    By langBtn = By.xpath("//android.widget.TextView[@text='Urdu']");
    By langBtn2 = By.xpath("//android.widget.TextView[@text='English']");
    private By showBalanceBtnLan = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");
    private By homeIconLan = By.xpath("//android.widget.Image[@resource-id='home-icon']");
  //  By langBtn = By.xpath("//android.widget.TextView[@resource-id='urdu']");
   // By langBtn2 = By.xpath("//android.widget.TextView[@resource-id=\"english\"]");

    // Actions
    public void clickSideMenu() {
    	wait.until(ExpectedConditions.elementToBeClickable(sideMenuforNotificationBtn)).click();
    }
    

    public void clickSettings() {
    	wait.until(ExpectedConditions.elementToBeClickable(settingsBtnNotification)).click();
    }

    public void clickManageNotifications() {
    	wait.until(ExpectedConditions.elementToBeClickable(manageNotifications)).click();
    }

    public void selectLanguage() {

        try {
            if (wait.until(ExpectedConditions.elementToBeClickable(langBtn)).isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(langBtn)).click();
                return;
            }
        } catch (Exception ignored) {}

        try {
            if (wait.until(ExpectedConditions.elementToBeClickable(langBtn2)).isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(langBtn2)).click();
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Neither language button is clickable");
        }
    }
    public void homeIconselectLanguage() {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(homeIconLan)).click();
    	
}

/*
 * public void ShowBalselectLanguage() {
 * wait.until(ExpectedConditions.elementToBeClickable(homeIconLan)).click();
 * 
 * 
 * }
 */
}
