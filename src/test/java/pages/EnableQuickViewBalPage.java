package pages;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;

public class EnableQuickViewBalPage extends BasePage {

    public EnableQuickViewBalPage(AndroidDriver driver) {
    	super(driver);
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
    	
        	safeClick(sideMenu, 250);
        }
    

    public void clickSettingsForEnableQucikViewBal() {
    	safeClick(settings, 250);
    }

    public void clickQuickViewBalanceForEnableQucikViewBal() {
    	safeClick(quickViewBalance, 250);
    }

    public void toggleQuickViewForEnableQucikViewBal() {
    	safeClick(toggleBtn, 250);
    }
    By header = By.xpath("//android.widget.TextView[@text='Quick View Balance']");

    public void waitForQuickViewScreen() {
        safeWait(header, TIMEOUT_SHORT);
    }
    public void clickHomeIconViewBal() {
    	safeClick(clickHomeIconViewBalBtn, 250);
    	
    }
}
