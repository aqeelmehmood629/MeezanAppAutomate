package pages;

import driver.DriverFactory;
import org.openqa.selenium.By;

public class ViewProfilePage extends BasePage {

    public ViewProfilePage() {
        super(DriverFactory.getDriver(), 250);
    }

    // ================= LOCATORS =================

    private By sideMenuBtn = By.xpath("//android.widget.Image[@resource-id=\"hamBurger\"]");
    
    private By myProfileMenu = By.xpath("//android.widget.TextView[@text='My Profile']");

    private By viewEditDetails = By.xpath("//android.widget.Button[@text=\"Edit Personal Details\"]");
    
    private By clickHomeIconAfterProfileBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ================= ACTIONS =================

    public void openSideMenu() {
        safeClick(sideMenuBtn, 250);
        System.out.println("✅ Side menu opened");
    }

    public void clickMyProfile() {
        safeClick(myProfileMenu, 250);
        System.out.println("✅ My Profile clicked");
    }

    public boolean isMyProfileDisplayed() {
        return safeWait(viewEditDetails, 250).isDisplayed();
    }
    
    public void clickHomeIconAfterProfile() {
    	safeClick(clickHomeIconAfterProfileBtn, 250);
    }
}