package pages;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class LoginPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private By usernameField = By.id("login_username");
    private By passwordField = By.id("login_password");
    private By loginButton   = By.xpath("//android.widget.Button[@text='Log In']");

    public void enterUsername(String username) {

        driver.context("WEBVIEW_invo8.meezan.mb");  // switch to webview

        WebElement user = wait.until(
            ExpectedConditions.visibilityOfElementLocated(usernameField)
        );

        user.click();

        // JS se clear
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value='';", user);

        // JS se set value
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value='" + username + "';", user);
    }


    public void enterPassword(String password) {

        driver.context("WEBVIEW_invo8.meezan.mb"); // switch to webview

        WebElement pass = wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordField)
        );
      //  pass.clear();
        pass.sendKeys(password);
        hideKeyboard();
    }

    public void clickLogin() {
    	driver.context("NATIVE_APP"); // switch to native

        WebElement btn = wait.until(
            ExpectedConditions.elementToBeClickable(loginButton)
        );
        btn.click();
    }

    public boolean isLoginScreenVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void conditionalLogin(String username, String password) {
        if (isLoginScreenVisible()) {
            enterUsername(username);
            enterPassword(password);
            clickLogin();
        } else {
            System.out.println("Already logged in. Skipping login step.");
        }
    }

    private void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}
    }
}
