package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;

import java.time.Duration;

public class FAQsPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public FAQsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
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
        wait.until(ExpectedConditions.elementToBeClickable(sideMenu)).click();
        System.out.println("✅ Side menu clicked");
    }

    public void clickFaqs() {
        wait.until(ExpectedConditions.elementToBeClickable(faqsOption)).click();
        System.out.println("✅ FAQs clicked");
    }

    public void verifyFaqScreen() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(faqScreen));
        System.out.println("✅ FAQs screen displayed");
    }
    public void userClickHomeIconFaqs() {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(userClickHomeIconFaqsBtn));
    	
    }
}