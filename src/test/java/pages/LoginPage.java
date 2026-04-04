package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;
import io.appium.java_client.android.AndroidDriver;
import utils.HybridAppStabilizer;

import java.time.Duration;

public class LoginPage {

	private AndroidDriver driver;
	private WebDriverWait wait;

	public LoginPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(280));
	}

	// ✅ WEBVIEW locators
	private By usernameField = By.id("login_username");
	private By passwordField = By.id("login_password");
	private By loginButton = By.xpath("//android.widget.Button[@text='Log In']");

	public void enterUsername(String username) {
		WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
		user.sendKeys(username);
	}

	public void enterPassword(String password) {
		WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
		pass.sendKeys(password);
	}

	public void clickLogin() {
		HybridAppStabilizer.hideKeyboard(driver);
		HybridAppStabilizer.switchToNative(driver);
		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		btn.click();
	}
}