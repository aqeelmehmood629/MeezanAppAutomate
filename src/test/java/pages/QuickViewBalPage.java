package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class QuickViewBalPage extends BasePage {

    public QuickViewBalPage(AndroidDriver driver) {
        super(driver);
    }

    // Locator 1
    private By viewBalanceBtn = By.xpath("//div[contains(text(),'View Balance')]");

    // Locator 2
    private By balanceText = By.xpath("//android.widget.TextView[contains(@text,'Balance')]");

    public void clickViewBalance() {
        safeClick(viewBalanceBtn);
        System.out.println("Clicked on View Balance");
    }

    public void verifyBalanceVisible() {
        WebElement balance = safeWait(balanceText);
        if (balance.isDisplayed()) {
            System.out.println("Balance is visible");
        } else {
            System.out.println("Balance is not visible");
        }
    }
}