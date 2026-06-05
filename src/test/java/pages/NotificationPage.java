package pages;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import driver.DriverFactory;

public class NotificationPage extends BasePage {

    public NotificationPage() {
        super(DriverFactory.getDriver()); // assuming singleton driver
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
        safeClick(sideMenuBtn, 250);
        System.out.println("✅ Side menu opened");
    }

    public void clickNotification() {
        safeClick(NotificatioBtn, 250);
        System.out.println("✅ Side menu opened");
    }

    public void verifyShownotification() {
        safeClick(verifyBtn, 250);
        System.out.println("✅ Side menu opened");
    }
    public void clickHomeIconNotifications() {
    	safeClick(clickHomeIconNotificationsBtn, 250);
    	
    }
}