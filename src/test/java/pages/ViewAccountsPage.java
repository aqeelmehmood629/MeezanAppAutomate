package pages;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ViewAccountsPage {

    AndroidDriver driver;
    WebDriverWait wait;

    public ViewAccountsPage() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(250));
    }

    // ===== LOCATORS =====

    private By dashboardTitle =
            By.xpath("//android.widget.TextView[contains(@text,'FRAZ')]");
    private By sideMenuBtn =
            By.xpath("//android.widget.Image[@resource-id=\"hamBurger\"]");

    private By myAccountsBtn =
            By.xpath("//android.widget.TextView[@text='My Accounts']");

    private By accountTitle =
            By.xpath("//android.view.View[contains(@resource-id,'label')]");
    private By homeIconAfterViewAccountBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ===== ACTIONS =====

    public String getDashboardAccountTitle() {

        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(dashboardTitle)
        );

        String text = el.getText().trim();

        if (text.isEmpty()) {
            throw new RuntimeException("❌ Dashboard account title is empty");
        }

        return text;
    }
    public void clickSideMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(sideMenuBtn)).click();
    }

    public void clickMyAccounts() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountsBtn)).click();
    }

    public String getMyAccountTitle() {

        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(accountTitle)
        );

        String text = el.getText().trim();

        if (text.isEmpty()) {
            throw new RuntimeException("❌ My Accounts title is empty");
        }

        return text;
    }
    public void homeIconAfterViewAccount() {
    	wait.until(ExpectedConditions.elementToBeClickable(homeIconAfterViewAccountBtn)).click();
    	
    }
}
    