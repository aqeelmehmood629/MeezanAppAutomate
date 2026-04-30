package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogoutPage {

    AppiumDriver driver;
    WebDriverWait wait;
    public LogoutPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
        ((AndroidDriver) driver).getCurrentPackage();
    }

    // Logout icon locator (update as per app)
    By logoutIcon = By.xpath("//android.widget.Image[@content-desc=\"Logout\"]");
    By usernameText = By.id("login_username");

    public void clickLogout() {
    	wait.until(ExpectedConditions.elementToBeClickable(logoutIcon)).click();
    }
    
    public boolean isUsernameDisplayed() {
        return driver.findElements(usernameText).size() > 0;
    }
}