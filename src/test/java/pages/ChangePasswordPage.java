package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;

import java.time.Duration;

public class ChangePasswordPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public ChangePasswordPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(250));
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
        wait.until(ExpectedConditions.elementToBeClickable(sideMenu)).click();
        System.out.println("✅ Side menu clicked");
    }

    public void clickSettings() {
        wait.until(ExpectedConditions.elementToBeClickable(settingsOption)).click();
        System.out.println("✅ Settings clicked");
    }

    public void clickChangePassword() {
        wait.until(ExpectedConditions.elementToBeClickable(changePassword)).click();
        System.out.println("✅ Change Password clicked");
    }

    public void enterCurrentPassword(String pwd) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(currentPassword)).sendKeys(pwd);
        System.out.println("✅ Current password entered");
    }

    public void enterNewPassword(String pwd) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(newPassword)).sendKeys(pwd);
        System.out.println("✅ New password entered");
    }

    public void enterConfirmPassword(String pwd) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPassword)).sendKeys(pwd);
        System.out.println("✅ Confirm password entered");
    }
    
    public void clickChangePasswordSubmit() {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(changePasswordSubmitBtn)).click();
        System.out.println("✅ Clicked Submit Button");
    	
    }

    public void verifyFieldsFilled() {
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(changePasswordSuccessMsg)).isDisplayed();
        if (isDisplayed) {
            System.out.println("✅ Reset Password successfully");
        }
        
    }
    public void clickHomeIconPassword() {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(clickHomeIconPasswordBtn)).click();
    	
    }
}