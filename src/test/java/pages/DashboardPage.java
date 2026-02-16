package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {

    private AndroidDriver driver;
    private WebDriverWait wait;
    // ✅ LOCATORS
    private By dashboardTitle = By.xpath("//android.widget.Button[@text='Send Money']");
    private By sendMoneyPageTitle = By.xpath("//android.widget.TextView[@text='Send Money']");

    public DashboardPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ✅ VERIFY DASHBOARD (just text/button visible)
    public boolean isDashboardDisplayed() {
        try {
            driver.context("NATIVE_APP");
            return driver.findElement(dashboardTitle).isDisplayed();
        } catch (Exception e) {
            System.out.println("Dashboard element not found: " + e.getMessage());
            return false;
        }
    }

    // ✅ VERIFY "Send Money" text is visible (before clicking)
    public boolean isSendMoneyTextVisible() {
        try {
            driver.context("NATIVE_APP");

            wait.until(ExpectedConditions.presenceOfElementLocated(dashboardTitle));

            WebElement sendMoneyText = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(dashboardTitle));

            return sendMoneyText.isDisplayed();

        } catch (Exception e) {
            System.out.println("Send Money text not visible on Dashboard: " + e.getMessage());
            return false;
        }
    }


    // ✅ CLICK "Send Money" button/text
    public void goToSendMoney() {
        try {
            driver.context("NATIVE_APP");
            WebElement sendMoneyButton = wait.until(
                    ExpectedConditions.elementToBeClickable(dashboardTitle));
            sendMoneyButton.click();
        } catch (Exception e) {
            System.out.println("Unable to click Send Money: " + e.getMessage());
        }
    }

    // ✅ VERIFY SEND MONEY PAGE IS DISPLAYED
    public boolean isSendMoneyPageDisplayed() {
        try {
            driver.context("NATIVE_APP");
            WebElement page = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(sendMoneyPageTitle));
            return page.isDisplayed();
        } catch (Exception e) {
            System.out.println("Send Money page not displayed: " + e.getMessage());
            return false;
        }
    }
}
