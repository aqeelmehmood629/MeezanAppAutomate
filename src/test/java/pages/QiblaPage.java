package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;

import java.time.Duration;

public class QiblaPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public QiblaPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ================= LOCATORS =================

    private By sideMenu =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By qiblaOption =
            By.xpath("//android.widget.TextView[@text='Qibla']");

    private By qiblaScreen =
            By.xpath("//android.widget.TextView[@resource-id=\"comp-head\"]");

    // ================= ACTIONS =================

    public void clickSideMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(sideMenu)).click();
        System.out.println("✅ Side menu clicked");
    }

    public void clickQibla() {
        wait.until(ExpectedConditions.elementToBeClickable(qiblaOption)).click();
        System.out.println("✅ Qibla clicked");
    }

    public void verifyQiblaScreen() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(qiblaScreen));
        System.out.println("✅ Qibla screen displayed");
    }
}