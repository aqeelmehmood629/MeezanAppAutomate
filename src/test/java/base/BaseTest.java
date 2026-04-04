package base;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.AfterAll;

public class BaseTest {

	protected static AndroidDriver driver;

	public static void initDriver() throws Exception {

		if (driver == null) {
			driver = DriverFactory.initializeDriver();
		}
	}

	@AfterAll
	public static void quitDriver() {

		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public static AndroidDriver getDriver() {
		return driver;
	}
}