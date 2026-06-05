package pages;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;

public class LogoutPage extends BasePage {

    public LogoutPage(AndroidDriver driver) {
        super(driver);
        driver.getCurrentPackage();
    }

    // Logout icon locator (update as per app)
    By logoutIcon = By.xpath("//android.widget.Image[@content-desc=\"Logout\"]");
    By usernameText = By.id("login_username");

    public void clickLogout() {
    	safeClick(logoutIcon, 250);
    }
    
    public boolean isUsernameDisplayed() {
        return driver.findElements(usernameText).size() > 0;
    }
}