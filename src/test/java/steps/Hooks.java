package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.*;

import com.aventstack.extentreports.*;
import utils.ExtentManager;
import utils.ReportManager;
import utils.ScreenshotUtil;
import utils.PopupHandler;
import utils.ScreenDetector;
import utils.ScreenDetector.AppScreen;
import pages.DashboardPage;
import utils.LoginHelper;
import utils.HybridAppStabilizer;

/**
 * 🎯 Hooks — Cucumber lifecycle management.
 *
 * Navigation is TAG-DRIVEN (no hardcoded exclusion lists):
 *   @NeedsLogin       → ensures user is logged in
 *   @NoNeedLogin      → ensures user is logged out
 *   @NeedsDashboard   → ensures app is on Dashboard (uses ScreenDetector)
 *
 * New modules automatically work without modifying this file.
 * Just add the appropriate tags to your feature files.
 */
public class Hooks {

	private static ExtentReports extent = ExtentManager.getExtentReports();

	/**
	 * ✅ @Before: Runs before EVERY scenario
	 *
	 * Flow:
	 * 1. Initialize or recover driver (detects UiAutomator2 crash)
	 * 2. Create Extent report test entry
	 * 3. Clear any lingering popup from previous scenario
	 * 4. If @NeedsLogin → perform login
	 * 5. If @NeedsDashboard → smart navigate (only if not already there)
	 */
	@Before
	public void setup(Scenario scenario) {

		// ✅ Step 1: Initialize driver — will auto-recover if UiAutomator2 crashed
		AndroidDriver driver = DriverFactory.initializeDriver();

		// ✅ Step 2: Start Extent test
		ExtentTest extentTest = extent.createTest(scenario.getName());
		ReportManager.setTest(extentTest);

		System.out.println("═══════════════════════════════════════════");
		System.out.println("▶️ STARTING SCENARIO: " + scenario.getName());
		System.out.println("Tags: " + scenario.getSourceTagNames());
		System.out.println("═══════════════════════════════════════════");

		// ✅ Step 3: Clear any lingering popup from a previous scenario
		try {
			PopupHandler.handlePopupIfPresent(driver, "Before Scenario");
		} catch (Exception ignored) {}

		// ✅ Step 4: Tag-based login control
		if (scenario.getSourceTagNames().contains("@NeedsLogin")) {
			System.out.println("🔐 @NeedsLogin → ensuring login...");
			try {
				LoginHelper.ensureLoggedIn(driver);
			} catch (Exception e) {
				System.out.println("⚠️ Login attempt failed: " + e.getMessage());
			}
		} else if (scenario.getSourceTagNames().contains("@NoNeedLogin")) {
			System.out.println("🔓 @NoNeedLogin → ensuring logged out...");
			try {
				LoginHelper.ensureLoggedOut(driver);
			} catch (Exception e) {
				System.out.println("⚠️ Logout attempt failed: " + e.getMessage());
			}
		}

		// ✅ Step 5: Smart navigation — only for @NeedsDashboard scenarios
		smartNavigate(scenario, driver, "before");
	}

	/**
	 * ✅ @After: Runs after EVERY scenario
	 *
	 * Flow:
	 * 1. Log scenario result to Extent
	 * 2. If failed → popup recovery
	 * 3. Final popup check
	 * 4. If @NeedsDashboard → smart navigate back to Dashboard
	 */
	@After
	public void tearDown(Scenario scenario) {

		String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9_]", "_");

		try { Thread.sleep(1000); } catch (Exception ignored) {}

		// ✅ Log scenario result
		if (scenario.isFailed()) {
			try {
				ReportManager.getTest().fail("❌ Scenario FAILED: " + scenarioName);
			} catch (Exception ignored) {}

			// 🛡️ POPUP RECOVERY: dismiss any error popup left by the failed scenario
			try {
				AndroidDriver driver = DriverFactory.getDriver();
				PopupHandler.handlePopupIfPresent(driver, "After Failed Scenario");
			} catch (Exception ignored) {}
		} else {
			try {
				ReportManager.getTest().pass("✅ Scenario PASSED: " + scenarioName);
			} catch (Exception ignored) {}
		}

		// 🛡️ Final popup check (always — even on pass)
		try {
			AndroidDriver driver = DriverFactory.getDriver();
			PopupHandler.handlePopupIfPresent(driver, "After Scenario Cleanup");
		} catch (Exception ignored) {}

		// ✅ Smart navigate back to Dashboard for next scenario
		try {
			AndroidDriver driver = DriverFactory.getDriver();
			smartNavigate(scenario, driver, "after");
		} catch (Exception e) {
			System.out.println("⚠️ Post-test navigation failed (non-fatal): " + e.getMessage());
		}

		System.out.println("═══════════════════════════════════════════");
		System.out.println("⏹️ FINISHED SCENARIO: " + scenario.getName()
				+ " → " + scenario.getStatus());
		System.out.println("═══════════════════════════════════════════");
	}

	/**
	 * ✅ @AfterAll: Runs ONCE after ALL scenarios are done
	 */
	@AfterAll
	public static void afterAll() {
		System.out.println("🛑 All scenarios finished — cleaning up...");

		try {
			DriverFactory.quitDriver();
		} catch (Exception e) {
			System.out.println("⚠️ Driver quit failed: " + e.getMessage());
		}

		try {
			ExtentManager.getExtentReports().flush();
			System.out.println("📊 Extent Report flushed");
		} catch (Exception e) {
			System.out.println("⚠️ Report flush failed: " + e.getMessage());
		}
	}

	/**
	 * 🎯 Smart Navigation — TAG-DRIVEN, SCREEN-AWARE.
	 *
	 * Runs only when ALL of these are true:
	 *   1. Scenario has @NeedsDashboard tag
	 *   2. User is logged in (LoginHelper.isLoggedIn())
	 *   3. Driver is responsive
	 *
	 * Uses ScreenDetector to check the current screen first:
	 *   → DASHBOARD  → skips navigation entirely (already there)
	 *   → LOGIN      → skips (wrong state — ensureLoggedIn should handle this)
	 *   → UNKNOWN    → navigates to Dashboard via Home icon
	 *
	 * After navigation, verifies Dashboard was actually reached.
	 * All blocks are wrapped in try-catch — never kills the next scenario.
	 *
	 * NO hardcoded scenario/tag exclusion list.
	 * New modules need ZERO changes here — just add @NeedsDashboard to their feature.
	 */
	private void smartNavigate(Scenario scenario, AndroidDriver driver, String phase) {

		// ── Guard 1: Only for @NeedsDashboard scenarios ──────────────────────
		if (!scenario.getSourceTagNames().contains("@NeedsDashboard")) {
			// Scenario doesn't require Dashboard — skip silently
			return;
		}

		// ── Guard 2: Must be logged in ────────────────────────────────────────
		if (!LoginHelper.isLoggedIn()) {
			System.out.println("ℹ️ Skipping dashboard navigation (" + phase + ") — not logged in.");
			return;
		}

		// ── Guard 3: Driver must be alive ─────────────────────────────────────
		if (!DriverFactory.isDriverResponsive()) {
			System.out.println("⚠️ Skipping dashboard navigation (" + phase + ") — driver unresponsive.");
			return;
		}

		// ── Detect current screen ─────────────────────────────────────────────
		System.out.println("🔍 Detecting current screen (" + phase + " scenario: " + scenario.getName() + ")...");
		AppScreen currentScreen = ScreenDetector.detectCurrentScreen(driver);

		switch (currentScreen) {
			case DASHBOARD:
				System.out.println("✅ Already on Dashboard — skipping navigation (" + phase + ")");
				return;

			case LOGIN:
				System.out.println("⚠️ App is on Login screen (" + phase + ") — skipping Home navigation. "
						+ "ensureLoggedIn() should handle this state.");
				return;

			case UNKNOWN:
			default:
				System.out.println("📍 Screen is UNKNOWN (" + phase + ") — navigating to Dashboard...");
				break;
		}

		// ── Navigate to Dashboard ─────────────────────────────────────────────
		try {
			HybridAppStabilizer.ensureNative(driver);
			DashboardPage dashboard = new DashboardPage(driver);
			dashboard.clickHome();
			Thread.sleep(2000);

			// ── Verify Dashboard was actually reached ──────────────────────────
			AppScreen verifiedScreen = ScreenDetector.detectCurrentScreen(driver);
			if (verifiedScreen == AppScreen.DASHBOARD) {
				System.out.println("✅ Dashboard confirmed after navigation (" + phase + ")");
			} else {
				System.out.println("⚠️ Dashboard not confirmed after navigation (" + phase
						+ ") — current screen: " + verifiedScreen
						+ ". Next scenario may handle navigation independently.");
			}

		} catch (Exception e) {
			System.out.println("⚠️ Dashboard navigation failed (" + phase + "): " + e.getMessage());
		}
	}
}
