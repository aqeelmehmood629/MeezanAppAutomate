package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.LoginPage;
import utils.HybridAppStabilizer;
import utils.CSVUtils;

import java.util.Map;

public class LoginSteps {

	private AndroidDriver driver;
	private LoginPage loginPage;

	@Given("the Meezan Bank app is launched")
	public void the_meezan_bank_app_is_launched() {

		driver = DriverFactory.getDriver();

		if (driver == null || driver.getSessionId() == null) {
			throw new RuntimeException("Driver not initialized!");
		}

		// 🔥 Switch to WebView
		HybridAppStabilizer.switchToWebView(driver);

		loginPage = new LoginPage(driver);

		System.out.println("App launched & WebView ready");
	}

	// ✅ CSV with row index (future multiple users)
	@When("user enters credentials from {string}")
	public void enterCredentialsFromCSV(String row) {

		int index = Integer.parseInt(row);

		Map<String, String> data = CSVUtils.getLoginData(index);

		String username = data.get("username");
		String password = data.get("password");

		loginPage.enterUsername(username);
		loginPage.enterPassword(password);

		System.out.println("Using CSV Data → " + username + " / " + password);
	}

	// ✅ Default (current single user)
	@When("user enters credentials from csv")
	public void enterCredentialsFromCSVDefault() {

		Map<String, String> data = CSVUtils.getLoginData(0);

		String username = data.get("username");
		String password = data.get("password");

		loginPage.enterUsername(username);
		loginPage.enterPassword(password);

		System.out.println("Using CSV Default User → " + username);
	}

	@And("user taps on login button")
	public void tapLogin() {
		loginPage.clickLogin();
	}

	@Then("user should be logged in successfully")
	public void verifyLogin() {
		System.out.println("Login step executed (add dashboard validation here)");
	}
}