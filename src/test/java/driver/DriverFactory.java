package driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverFactory {

	private static AndroidDriver driver;

	// ✅ Initialize driver (ONLY ONCE)
	public static AndroidDriver initializeDriver() {

		try {
			if (driver == null) { // IMPORTANT (singleton check)

				DesiredCapabilities caps = new DesiredCapabilities();

				// Device details
				caps.setCapability("platformName", "Android");
				caps.setCapability("automationName", "UiAutomator2");
				caps.setCapability("deviceName", "Android Device");
				caps.setCapability("udid", "083672525V005614");

				// App details
				caps.setCapability("appPackage", "com.ofss.tx.meezan");
				caps.setCapability("appActivity", "com.ofss.digx.mobile.android.SplashActivity");

				// WebView handling
				caps.setCapability("chromedriverExecutable",
						"C:\\Users\\ma\\Documents\\Aqeel QA\\ChromeDriver\\chromedriver-win64 (2)\\chromedriver-win64\\chromedriver.exe");
				caps.setCapability("chromedriverAutodownload", true);
				caps.setCapability("ensureWebviewsHavePages", true);
				caps.setCapability("webviewConnectTimeout", 30000);

				// Stability
				caps.setCapability("noReset", true); // IMPORTANT
				caps.setCapability("fullReset", false);
				caps.setCapability("autoGrantPermissions", true);
				caps.setCapability("adbExecTimeout", 120000);
				caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
				caps.setCapability("newCommandTimeout", 300);
				caps.setCapability("enforceXPath1", true);

				driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);

				System.out.println(" Driver Initialized");
				System.out.println("Session ID: " + driver.getSessionId());

			} else {
				System.out.println(" Reusing existing driver session");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Driver initialization failed: " + e.getMessage());
		}

		return driver;
	}

	// Get driver
	public static AndroidDriver getDriver() {
		if (driver == null) {
			throw new RuntimeException("Driver not initialized! Call initializeDriver() first.");
		}
		return driver;
	}

	// Quit driver (ONLY AT END)
	public static void quitDriver() {
		try {
			if (driver != null) {
				driver.quit();
				driver = null;
				System.out.println("Driver Quit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}