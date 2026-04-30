package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;

import java.time.Duration;

public class DealsDiscountsPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public DealsDiscountsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
    }

    // ================= LOCATORS =================

    private By sideMenu =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By dealsOption =
            By.xpath("//android.widget.TextView[contains(@text,'Deals')]");
    
    private By backBtn =
            By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[2]/com.horcrux.svg.SvgView/com.horcrux.svg.GroupView/com.horcrux.svg.PathView");
    // ================= ACTIONS =================

    public void clickSideMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(sideMenu)).click();
        System.out.println("✅ Side menu clicked");
    }

    public void clickDeals() {
        wait.until(ExpectedConditions.elementToBeClickable(dealsOption)).click();
        System.out.println("✅ Deals clicked");
    }

    public void clickBackBtn() {
    	wait.until(ExpectedConditions.elementToBeClickable(backBtn)).click();
        System.out.println("✅ Back Button clicked");	
    }
    }
