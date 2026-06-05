package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import io.appium.java_client.android.AndroidDriver;

public class EnableDisableAccountsPage extends BasePage {
	
	public EnableDisableAccountsPage(AndroidDriver driver) {
        super(driver);
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
            safeClick(sideMenuAccounts);
            System.out.println("✅ Side menu clicked");
        }
    

    public void clickSettings() {
    	safeClick(settingsOptionAccounts);
        System.out.println("✅ Setting menu clicked");
    }

    public void clickAccountManagement() {
    	safeClick(accountManagement);
        System.out.println("✅ clicked Account Management Button");
    }

    public void deselectAccountManagement() {
    	safeClick(deselect);
        System.out.println("✅ Disable Account");
    }

    public void clickSaveChanges() {
    	safeClick(saveChangesAccounts);
        System.out.println("✅ Clicked Save Changes Button");
    }

    public void verifyAccountStatus() {

    boolean statusFound = false;

    try {

        WebElement unlinked = safeWait(unlinkedStatus, 250);

        if (unlinked.isDisplayed()) {
            statusFound = true;
            System.out.println("UNLINKED status found");
        }

    } catch (Exception e) {

        try {

            WebElement linked = safeWait(linkedStatus, 250);

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
    	
    	safeClick(clickAccountHomeIcon);
    	
    }
}