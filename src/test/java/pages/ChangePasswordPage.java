package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ChangePasswordPage extends BasePage {

    public ChangePasswordPage(AndroidDriver driver) {
        super(driver);
    }

    // ================= LOCATORS =================

    private By sideMenu =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By settingsOption =
            By.xpath("//android.widget.TextView[@text='Settings']");

    private By changePassword =
            By.xpath("//android.widget.TextView[@text='Change Password']");

    private By currentPassword =
            By.xpath("//android.widget.EditText[@resource-id='oldpwd|input']");

    private By newPassword =
            By.xpath("//android.widget.EditText[@resource-id='newpwd|input']");

    private By confirmPassword =
            By.xpath("//android.widget.EditText[@resource-id='conpwd|input']");
    
    private By changePasswordSubmitBtn = By.xpath("//android.widget.Button[@text=\"Reset Password\"]");
    
    private By changePasswordSuccessMsg = By.xpath("//android.widget.TextView[@text=\"Your Password has been reset successfully!\"]");
    
    private By clickHomeIconPasswordBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");
    // ================= ACTIONS =================

    public void clickSideMenu() {
        safeClick(sideMenu, 250);
        System.out.println("✅ Side menu clicked");
    }

    public void clickSettings() {
        safeClick(settingsOption, 250);
        System.out.println("✅ Settings clicked");
    }

    public void clickChangePassword() {
        safeClick(changePassword, 250);
        System.out.println("✅ Change Password clicked");
    }

    public void enterCurrentPassword(String pwd) {
        safeSendKeys(currentPassword, pwd, 250);
        System.out.println("✅ Current password entered");
    }

    public void enterNewPassword(String pwd) {
        safeSendKeys(newPassword, pwd, 250);
        System.out.println("✅ New password entered");
    }

    public void enterConfirmPassword(String pwd) {
        safeSendKeys(confirmPassword, pwd, 250);
        System.out.println("✅ Confirm password entered");
    }
    
    public void clickChangePasswordSubmit() {
    	safeClick(changePasswordSubmitBtn, 250);
        System.out.println("✅ Clicked Submit Button");
    	
    }

    public void verifyFieldsFilled() {
        boolean isDisplayed = safeWait(changePasswordSuccessMsg, 250).isDisplayed();
        if (isDisplayed) {
            System.out.println("✅ Reset Password successfully");
        }
        
    }
    public void clickHomeIconPassword() {
    	
    	safeClick(clickHomeIconPasswordBtn, 250);
    	
    }
}