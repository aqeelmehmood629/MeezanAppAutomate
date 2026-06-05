package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import driver.DriverFactory;

public class FAQsPage extends BasePage {

    public FAQsPage() {
        super(DriverFactory.getDriver(), 250);
    }

    // ================= LOCATORS =================

    private By sideMenu =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By faqsOption =
            By.xpath("//android.widget.TextView[@text='FAQs']");

    private By faqScreen =
            By.xpath("//android.widget.TextView[@text='FAQs']");
    private By userClickHomeIconFaqsBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ================= ACTIONS =================

    public void clickSideMenu() {
        safeClick(sideMenu, 250);
        System.out.println("✅ Side menu clicked");
    }

    public void clickFaqs() {
        safeClick(faqsOption, 250);
        System.out.println("✅ FAQs clicked");
    }

    public void verifyFaqScreen() {
        safeWait(faqScreen, 250);
        System.out.println("✅ FAQs screen displayed");
    }
    public void userClickHomeIconFaqs() {
        safeWait(userClickHomeIconFaqsBtn, 250);
        
    }
}