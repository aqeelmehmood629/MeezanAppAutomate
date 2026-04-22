package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SadqaZakatPage {

    AndroidDriver driver;
    WebDriverWait wait,wait1;

    public SadqaZakatPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(400));
        this.wait1 = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    // 🔹 Locators
    By zakatsadqaBtn = By.xpath("//*[contains(@text,'Zakat') or contains(@text,'Sadqa')]");
    By sadqaTab = By.xpath("//android.widget.TextView[@resource-id=\"sadaqat\"]");
    By searchBox = By.xpath("//android.widget.EditText[@resource-id=\"search-box\"]");
    By amountField = By.xpath("//android.widget.EditText[@resource-id=\"amount|input\"]");
    By nextBtn = By.xpath("//android.widget.Button[@text='Next']");
    By payZakatBtn = By.xpath("//android.widget.Button[contains(@text,'Pay Zakat Now')]");
    By paySadqaBtn = By.xpath("//android.widget.Button[contains(@text,'Pay Sadqa Now')]");
    By donationSuccessMsg = By.xpath("//android.widget.TextView[@text='Transaction Successful']");
    public void clickZakatSadqaBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(zakatsadqaBtn)).click();
    }
	  public void clickSadqaTab() {
	  wait.until(ExpectedConditions.elementToBeClickable(sadqaTab)).click(); }
	 
	  public void searchFoundation(String foundation) {

		    WebElement element = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(searchBox)
		    );

		    element.click();
		    element.clear();

		    foundation = foundation.trim();

		    // Step 1: type full value (ONLY ONCE from CSV)
		    element.sendKeys(foundation);

		    // small wait for UI sync
		    try {
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }

		    // Step 2: create modified value (-1 logic)
		    String modified = foundation.substring(0, foundation.length() - 1);

		    // IMPORTANT: clear before re-entering (prevents append issue)
		    element.clear();

		    // Step 3: type modified value
		    element.sendKeys(modified);

		    // optional keyboard handling
		    try {
		        driver.hideKeyboard();
		    } catch (Exception e) {
		        // ignore if not present
		    }
		}
    public void selectFoundation(String foundation) {
        By locator = By.xpath("//android.widget.TextView[contains(@text,'" + foundation.trim() + "')]");

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    public void enterAmount(String amount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(amountField)).sendKeys(amount);
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
    }

    public void clickSadqaNow() {
        wait.until(ExpectedConditions.elementToBeClickable(paySadqaBtn)).click();
    }
    
    public void clickZakatNow() {
        wait.until(ExpectedConditions.elementToBeClickable(payZakatBtn)).click();
    }

    public void waitForDonationSuccess() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(donationSuccessMsg));
    }
}