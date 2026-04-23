package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import driver.DriverFactory;

public class NotificationPage {

    private AndroidDriver driver;
    WebDriverWait wait;

    public NotificationPage() {
        this.driver = DriverFactory.getDriver(); // assuming singleton driver
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ================= LOCATORS =================

    private By sideMenuBtn =
            By.xpath("//android.widget.Image[@resource-id=\"hamBurger\"]");
    private By NotificatioBtn =
            By.xpath("//android.widget.TextView[@text='Notifications']");

    private By verifyBtn =
            By.xpath("//android.view.View[@text=\"Non Financial\"]");
    
    private By clickHomeIconNotificationsBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ================= ACTIONS =================

    public void openSideMenuNotification() {
        wait.until(ExpectedConditions.elementToBeClickable(sideMenuBtn)).click();
        System.out.println("✅ Side menu opened");
    }

    public void clickNotification() {
        wait.until(ExpectedConditions.elementToBeClickable(NotificatioBtn)).click();
        System.out.println("✅ Side menu opened");
    }

    public void verifyShownotification() {
        wait.until(ExpectedConditions.elementToBeClickable(verifyBtn)).click();
        System.out.println("✅ Side menu opened");
    }
    public void clickHomeIconNotifications() {
    	wait.until(ExpectedConditions.elementToBeClickable(clickHomeIconNotificationsBtn)).click();
    	
    }
}