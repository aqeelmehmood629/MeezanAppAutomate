package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

public class ManageRegisteredDevicesPage extends BasePage {

    public ManageRegisteredDevicesPage(AndroidDriver driver) {
        super(driver);
    }


    // Locator 1
    private By sideMenu = By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    // Locator 2
    private By settingButton = By.xpath("//android.widget.TextView[@text='Settings']");

    // Locator 3
    private By manageRegisteredDevices = By.xpath("//android.widget.TextView[@text='Manage Registered Devices']");

    // Locator 4
  //  private By removeButton = By.xpath("//span[@id='switch' and text()='Remove']");

    private By removeButtonVerify = By.xpath("//li[contains(text(),'No items to display')]");

    public void clickSideMenu() {
        safeClick(sideMenu);
        System.out.println("Clicked Side Menu");
    }

    public void clickSetting() {
        safeClick(settingButton);
        System.out.println("Clicked Setting");
    }

    public void clickManageRegisteredDevices() {
        safeClick(manageRegisteredDevices);
        System.out.println("Clicked Manage Registered Devices");
    }

    public void clickAllRemoveButtons() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    while (true) {

        List<WebElement> removeBtns = driver.findElements(
                By.xpath("//span[@id='switch' and text()='Remove']")
        );

        // Stop when no buttons left
        if (removeBtns.size() == 0) {
            System.out.println("No Remove buttons left");
            break;
        }

        try {

            WebElement btn = removeBtns.get(0);

            wait.until(ExpectedConditions.visibilityOf(btn));

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", btn);

            System.out.println("Clicked Remove");

            // wait for DOM refresh
            Thread.sleep(2000);

        } catch (StaleElementReferenceException e) {

            System.out.println("Stale element, retrying...");

        } catch (Exception e) {

            System.out.println("Issue while clicking Remove: " + e.getMessage());
        }
    }
}

    public void verifyDeviceRemoved() {
        WebElement msg = safeWait(removeButtonVerify);
        Assert.assertTrue(msg.isDisplayed(), "Device is NOT removed, list still has items");
    }
}