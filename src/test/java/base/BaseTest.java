package base;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;

/**
 * Base test class — kept for backward compatibility.
 * Driver lifecycle is now managed by Hooks.java + DriverFactory.
 */
public class BaseTest {

	protected static AndroidDriver driver;

	public static void initDriver() throws Exception {

		if (driver == null) {
			driver = DriverFactory.initializeDriver();
		}
	}

	public static AndroidDriver getDriver() {
		return DriverFactory.getDriver();
	}

	// ✅ Driver quit is now handled by Hooks.@AfterAll
	// No need for @AfterAll here to avoid double-quit
}