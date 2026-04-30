package pages;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ViewProfilePage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public ViewProfilePage() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(250));
    }

    // ================= LOCATORS =================

    private By sideMenuBtn = By.xpath("//android.widget.Image[@resource-id=\"hamBurger\"]");
    
    private By myProfileMenu = By.xpath("//android.widget.TextView[@text='My Profile']");

    private By viewEditDetails = By.xpath("//android.widget.Button[@text=\"Edit Personal Details\"]");
    
    private By clickHomeIconAfterProfileBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ================= ACTIONS =================

    public void openSideMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(sideMenuBtn)).click();
        System.out.println("✅ Side menu opened");
    }

    public void clickMyProfile() {
        wait.until(ExpectedConditions.elementToBeClickable(myProfileMenu)).click();
        System.out.println("✅ My Profile clicked");
    }

    public boolean isMyProfileDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(viewEditDetails)).isDisplayed();
    }
    public void clickHomeIconAfterProfile() {
    	wait.until(ExpectedConditions.elementToBeClickable(clickHomeIconAfterProfileBtn)).click();
    	
    }
}