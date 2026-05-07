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
	
	public EnableDisableAccountsPage(AndroidDriver driver) {
        this.driver =driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
    }

    // Locators
	private By sideMenuAccounts =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By settingsOptionAccounts =
            By.xpath("//android.widget.TextView[@text='Settings']");
    private By accountManagement = By.xpath("//android.widget.TextView[@text='Account Management']");
    private By saveChangesAccounts = By.xpath("//android.widget.Button[@text='Save Changes']");
    private By unlinkedStatus = By.xpath("//android.widget.TextView[@text='Account Status: UNLINKED']");
    private By linkedStatus = By.xpath("(//android.widget.TextView[@text='Account Status: LINKED'])[1]");
    
    private By deselect = By.xpath("(//div[@class='toggle-switch ot-none bg-success-dark'])[1]");
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

    public void verifyAccountStatus() {

    boolean statusFound = false;

    try {

        WebElement unlinked = wait.until(
                ExpectedConditions.visibilityOfElementLocated(unlinkedStatus));

        if (unlinked.isDisplayed()) {
            statusFound = true;
            System.out.println("UNLINKED status found");
        }

    } catch (Exception e) {

        try {

            WebElement linked = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(linkedStatus));

            if (linked.isDisplayed()) {
                statusFound = true;
                System.out.println("LINKED status found");
            }

        } catch (Exception ex) {
            System.out.println("No status found");
        }
    }

    Assert.assertTrue(statusFound, "Account status not found");
}
    public void accountclickHome() {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(clickAccountHomeIcon)).click();
    	
    }
}