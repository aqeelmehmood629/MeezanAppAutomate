package pages;

import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EnableQuickViewBalPage {

	AndroidDriver driver;
    WebDriverWait wait;

    public EnableQuickViewBalPage(AndroidDriver driver) {
    	this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.driver = driver;
    }

    // Locators (update with real resource-ids if available)
    By sideMenu = By.xpath("//android.widget.Image[@resource-id='hamBurger']");
    By settings = By.xpath("//android.widget.TextView[@text='Settings']");
    By quickViewBalance = By.xpath("//android.widget.TextView[@text='Quick View Balance']");

    // Toggle (Switch or similar)
    By toggleBtn = By.xpath("//android.widget.TextView[@text='Enable Quick View Balance']/following::android.view.View[1]");
    By clickHomeIconViewBalBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // Actions
    public void clickSideMenuForEnableQucikViewBal() {
    	
        	wait.until(ExpectedConditions.elementToBeClickable(sideMenu)).click();
        }
    

    public void clickSettingsForEnableQucikViewBal() {
    	wait.until(ExpectedConditions.elementToBeClickable(settings)).click();
    }

    public void clickQuickViewBalanceForEnableQucikViewBal() {
    	wait.until(ExpectedConditions.elementToBeClickable(quickViewBalance)).click();
    }

    public void toggleQuickViewForEnableQucikViewBal() {
    	wait.until(ExpectedConditions.elementToBeClickable(toggleBtn)).click();
    }
    By header = By.xpath("//android.widget.TextView[@text='Quick View Balance']");

    public void waitForQuickViewScreen() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(header));
    }
    public void clickHomeIconViewBal() {
    	wait.until(ExpectedConditions.elementToBeClickable(clickHomeIconViewBalBtn)).click();
    	
    }
}
