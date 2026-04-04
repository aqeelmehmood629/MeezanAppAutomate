package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {

	private AndroidDriver driver;
	private WebDriverWait wait;

	// ✅ Constructor
	public DashboardPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(280));
	}

	// ✅ Only 2 locators as requested
	private By showBalanceBtn = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");
	private By balanceText = By.xpath("//android.widget.TextView[contains(@text,'PKR')]");

	private double storedBalance;

	// ✅ Click Show Balance
	public void clickShowBalance() {
		wait.until(ExpectedConditions.elementToBeClickable(showBalanceBtn)).click();
		System.out.println("✅ Show Balance clicked");
	}

	// ✅ Store current balance
	public void storeCurrentBalance() {
		WebElement balanceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(balanceText));
		String balanceStr = balanceElement.getText().replaceAll("[^0-9.]", ""); // Remove non-numeric
		storedBalance = Double.parseDouble(balanceStr);
		System.out.println("💰 Stored Balance: " + storedBalance);
	}

	public double getStoredBalance() {
		return storedBalance;
	}

	// ✅ Optional: refresh balance by clicking Show Balance again
	public void refreshBalance() {
		clickShowBalance();
		System.out.println("🔄 Dashboard balance refreshed via Show Balance button");
	}
}