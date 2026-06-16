package driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverFactory {

	private static AndroidDriver driver;

	/**
	 * ✅ Initialize driver ONLY if not already alive AND fully responsive.
	 * If UiAutomator2 instrumentation has crashed, forces a fresh session.
	 */
	public static synchronized AndroidDriver initializeDriver() {

		// ✅ Deep check: reuse ONLY if UiAutomator2 instrumentation is responsive
		if (driver != null && isDriverResponsive()) {
			System.out.println("♻️ Reusing existing driver session: " + driver.getSessionId());
			return driver;
		}

		// ❌ Driver is null, HTTP session dead, or UiAutomator2 crashed → force new session
		if (driver != null) {
			System.out.println("⚠️ Session unresponsive (UiAutomator2 crash detected). Forcing new session...");
			try { driver.quit(); } catch (Exception ignored) {}
			driver = null;
			utils.LoginHelper.setLoggedIn(false);
		}

		// Create new driver session
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
					"C:\\Users\\ma\\Documents\\Aqeel QA\\chromedriver-win _148\\chromedriver-win64\\chromedriver.exe");
			caps.setCapability("ensureWebviewsHavePages", true);
			caps.setCapability("webviewConnectTimeout", 30000);

			// Stability
			caps.setCapability("noReset", true);
			caps.setCapability("fullReset", false);
			caps.setCapability("autoGrantPermissions", true);
			caps.setCapability("adbExecTimeout", 120000);
			caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
			caps.setCapability("newCommandTimeout", 600);
			caps.setCapability("enforceXPath1", true);
			caps.setCapability("unlockType", "pin");
			caps.setCapability("unlockKey", "1234");
			caps.setCapability("autoWebview", false);
			caps.setCapability("webviewDevtoolsPort", "9222");

			driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);

			// ✅ Reset global login state for new session
			utils.LoginHelper.setLoggedIn(false);

			System.out.println("✅ Driver Initialized (new session)");
			System.out.println("Session ID: " + driver.getSessionId());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Driver initialization failed: " + e.getMessage());
		}

		return driver;
	}

	/**
	 * ✅ DEEP responsiveness check — detects UiAutomator2 instrumentation crashes.
	 *
	 * The old getContext() only verified the HTTP session was alive.
	 * currentActivity() sends a real command to the Android instrumentation layer,
	 * so it will throw if UiAutomator2 has crashed — which is the real failure mode.
	 */
	public static boolean isDriverResponsive() {
		try {
			if (driver == null) return false;
			driver.getSessionId();    // HTTP session check
			driver.currentActivity(); // ← Real UiAutomator2 instrumentation check (KEY FIX)
			return true;
		} catch (Exception e) {
			System.out.println("⚠️ Driver unresponsive (UiAutomator2 may have crashed): " + e.getMessage());
			return false;
		}
	}

	/**
	 * ✅ Backward-compatible alias
	 */
	public static boolean isDriverAlive() {
		return isDriverResponsive();
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

	/**
	 * 🔄 RECOVER driver after a mid-scenario UiAutomator2 crash.
	 *
	 * Flow:
	 *   1. Force quit the dead/unresponsive session
	 *   2. Reset login state (new session = not logged in)
	 *   3. Create a fresh driver session via initializeDriver()
	 *
	 * Called by BasePage safe methods when interaction failures
	 * are NOT caused by a popup (i.e., driver itself is dead).
	 */
	public static synchronized AndroidDriver recoverDriver() {
		System.out.println("═══════════════════════════════════════════");
		System.out.println("🔄 DRIVER RECOVERY: UiAutomator2 crash detected");
		System.out.println("═══════════════════════════════════════════");

		// Force quit dead session
		if (driver != null) {
			try { driver.quit(); } catch (Exception ignored) {}
			driver = null;
		}

		// Reset login state — new session means not logged in
		utils.LoginHelper.setLoggedIn(false);

		// Create fresh session
		return initializeDriver();
	}

	/**
	 * ✅ Alias for isDriverResponsive() — more descriptive name.
	 */
	public static boolean isDriverHealthy() {
		return isDriverResponsive();
	}
}