package driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverFactory {

	private static AndroidDriver driver;

	/**
	 * ✅ Initialize driver ONLY if not already alive.
	 * Reuses existing session to keep app state across scenarios.
	 */
	public static synchronized AndroidDriver initializeDriver() {

		// ✅ Reuse existing driver if it's still alive
		if (driver != null && isDriverAlive()) {
			System.out.println("♻️ Reusing existing driver session: " + driver.getSessionId());
			return driver;
		}

		// ❌ Driver is null or dead → create new one
		try {
			DesiredCapabilities caps = new DesiredCapabilities();

			// Device details
			caps.setCapability("platformName", "Android");
			caps.setCapability("automationName", "UiAutomator2");
			caps.setCapability("deviceName", "Android Device");
			caps.setCapability("udid", "082653732Q003887");

			// App details
			caps.setCapability("appPackage", "com.ofss.tx.meezan");
			caps.setCapability("appActivity", "com.ofss.digx.mobile.android.SplashActivity");
			caps.setCapability("appWaitActivity", "com.ofss.*");

			// WebView handling
			caps.setCapability("chromedriverExecutable",
					"C:\\Users\\ma\\Documents\\Aqeel QA\\ChromeDriver\\chromedriver-win64 (3)\\chromedriver-win64\\chromedriver.exe");
			caps.setCapability("ensureWebviewsHavePages", true);
			caps.setCapability("webviewConnectTimeout", 30000);

			// Stability
			caps.setCapability("noReset", true);
			caps.setCapability("fullReset", false);
			caps.setCapability("autoGrantPermissions", true);
			caps.setCapability("adbExecTimeout", 120000);
			caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
			caps.setCapability("newCommandTimeout", 300);
			caps.setCapability("enforceXPath1", true);
			caps.setCapability("unlockType", "pin");
			caps.setCapability("unlockKey", "1234");
			caps.setCapability("autoWebview", false);
			caps.setCapability("webviewDevtoolsPort", "9222");

			driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);

			System.out.println("✅ Driver Initialized (new session)");
			System.out.println("Session ID: " + driver.getSessionId());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Driver initialization failed: " + e.getMessage());
		}

		return driver;
	}

	/**
	 * ✅ Check if current driver session is still alive
	 */
	public static boolean isDriverAlive() {
		try {
			if (driver == null) return false;
			driver.getSessionId(); // will throw if session is dead
			driver.getContext();   // verify it can communicate
			return true;
		} catch (Exception e) {
			System.out.println("⚠️ Driver session is dead: " + e.getMessage());
			driver = null;
			return false;
		}
	}

	public static AndroidDriver getDriver() {
		return driver;
	}

	// ✅ Quit driver (ONLY AT END of entire suite)
	public static void quitDriver() {
		try {
			if (driver != null) {
				driver.quit();
				driver = null;
				System.out.println("🛑 Driver Quit");
			}
		} catch (Exception e) {
			e.printStackTrace();
			driver = null;
		}
	}
}