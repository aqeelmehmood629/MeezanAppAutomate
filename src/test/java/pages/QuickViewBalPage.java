package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class QuickViewBalPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public QuickViewBalPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Locator 1
    private By viewBalanceBtn = By.xpath("//div[contains(text(),'View Balance')]");

    // Locator 2
    private By balanceText = By.xpath("//android.widget.TextView[contains(@text,'Balance')]");

    public void clickViewBalance() {
        wait.until(ExpectedConditions.elementToBeClickable(viewBalanceBtn)).click();
        System.out.println("Clicked on View Balance");
    }

    public void verifyBalanceVisible() {
        WebElement balance = wait.until(ExpectedConditions.visibilityOfElementLocated(balanceText));
        if (balance.isDisplayed()) {
            System.out.println("Balance is visible");
        } else {
            System.out.println("Balance is not visible");
        }
    }
}