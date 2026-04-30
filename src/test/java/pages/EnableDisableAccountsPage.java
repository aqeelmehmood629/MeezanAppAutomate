package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;

public class EnableDisableAccountsPage {
	
	private AndroidDriver driver;
    private WebDriverWait wait;
	
	public EnableDisableAccountsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
    }

    // Locators
	private By sideMenuAccounts =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By settingsOptionAccounts =
            By.xpath("//android.widget.TextView[@text='Settings']");
    private By accountManagement = By.xpath("//android.widget.TextView[@text='Account Management']");
    private By saveChangesAccounts = By.xpath("//android.widget.Button[@text='Save Changes']");
    private By status = By.xpath("//android.widget.TextView[@text='Account Status: UNLINKED']");
    
    private By deselect = By.xpath("//android.view.View[@resource-id=\"maincontent\"]/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.TextView");
    private By clickAccountHomeIcon = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    public void accountsSideMenu() {
            wait.until(ExpectedConditions.elementToBeClickable(sideMenuAccounts)).click();
            System.out.println("✅ Side menu clicked");
        }
    

    public void clickSettings() {
    	wait.until(ExpectedConditions.elementToBeClickable(settingsOptionAccounts)).click();
        System.out.println("✅ Setting menu clicked");
    }

    public void clickAccountManagement() {
    	wait.until(ExpectedConditions.elementToBeClickable(accountManagement)).click();
        System.out.println("✅ clicked Account Management Button");
    }

    public void deselectAccountManagement() {
    	wait.until(ExpectedConditions.elementToBeClickable(deselect)).click();
        System.out.println("✅ Disable Account");
    }

    public void clickSaveChanges() {
    	wait.until(ExpectedConditions.elementToBeClickable(saveChangesAccounts)).click();
        System.out.println("✅ Clicked Save Changes Button");
    }

    public void verifyUnlinkedStatus() {
    	WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(status));
    	Assert.assertTrue(el.isDisplayed(), "Account is not UNLINKED");
    }
    public void accountclickHome() {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(clickAccountHomeIcon)).click();
    	
    }
}