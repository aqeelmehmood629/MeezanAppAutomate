package pages;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;

import org.openqa.selenium.By;

public class ChangeNotificationLanguagePage extends BasePage {

    public ChangeNotificationLanguagePage(AndroidDriver driver) {
        super(driver);
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
    	safeClick(sideMenuforNotificationBtn, 250);
    }
    

    public void clickSettings() {
    	safeClick(settingsBtnNotification, 250);
    }

    public void clickManageNotifications() {
    	safeClick(manageNotifications, 250);
    }

    public void selectLanguage() {

        try {
            if (safeWait(langBtn, 250).isDisplayed()) {
                safeClick(langBtn, 250);
                return;
            }
        } catch (Exception ignored) {}

        try {
            if (safeWait(langBtn2, 250).isDisplayed()) {
                safeClick(langBtn2, 250);
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Neither language button is clickable");
        }
    }
    public void homeIconselectLanguage() {
    	
    	safeClick(homeIconLan, 250);
    	
}

/*
 * public void ShowBalselectLanguage() {
 * wait.until(ExpectedConditions.elementToBeClickable(homeIconLan)).click();
 * 
 * 
 * }
 */
}
