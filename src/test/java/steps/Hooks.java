package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.*;

import com.aventstack.extentreports.*;
import utils.ExtentManager;
import utils.ReportManager;
import utils.ScreenshotUtil;
import pages.DashboardPage;
import utils.LoginHelper;
import utils.HybridAppStabilizer;

public class Hooks {

	private static ExtentReports extent = ExtentManager.getExtentReports();

	/**
	 * ✅ @Before: Runs before EVERY scenario
	 *
	 * Execution order:
	 * 1. Initialize or RECOVER driver (detects UiAutomator2 crash and creates new session)
	 * 2. Create Extent report test entry
	 * 3. If scenario has @NeedsLogin tag → perform login
	 * 4. Navigate to dashboard if needed (for non-excluded scenarios)
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

		// ✅ Step 3: Tag-based login control
		// Add @NeedsLogin to any scenario that requires the user to be logged in.
		// No feature files are modified — tags are checked here at runtime.
		if (scenario.getSourceTagNames().contains("@NeedsLogin")) {
			System.out.println("🔐 @NeedsLogin tag detected → ensuring login...");
			try {
				LoginHelper.ensureLoggedIn(driver);
			} catch (Exception e) {
				System.out.println("⚠️ Login attempt failed: " + e.getMessage());
			}
		} else if (scenario.getSourceTagNames().contains("@NoNeedLogin")) {
			System.out.println("🔓 @NoNeedLogin tag detected → ensuring logged out state...");
			try {
				LoginHelper.ensureLoggedOut(driver);
			} catch (Exception e) {
				System.out.println("⚠️ Logout attempt failed: " + e.getMessage());
			}
		}

		// ✅ Step 4: Navigate to dashboard before test (for applicable scenarios)
		ensureDashboardState(scenario, "before");
	}

	/**
	 * ✅ @After: Runs after EVERY scenario
	 *
	 * Logs scenario-level PASS/FAIL summary to Extent Report.
	 * Step-level logging + inline screenshots are handled by CucumberListener.
	 * All blocks wrapped in try-catch so @After NEVER propagates to next scenario.
	 */
	@After
	public void tearDown(Scenario scenario) {

		String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9_]", "_");

		try { Thread.sleep(1000); } catch (Exception ignored) {}

		// ✅ Scenario-level summary (step details already logged by CucumberListener)
		if (scenario.isFailed()) {
			try {
				ReportManager.getTest().fail("❌ Scenario FAILED: " + scenarioName);
			} catch (Exception ignored) {}
		} else {
			try {
				ReportManager.getTest().pass("✅ Scenario PASSED: " + scenarioName);
			} catch (Exception ignored) {}
		}

		// ✅ Navigate back to dashboard — wrapped so it NEVER kills the next scenario
		try {
			ensureDashboardState(scenario, "after");
		} catch (Exception e) {
			System.out.println("⚠️ Post-test dashboard navigation failed (non-fatal): " + e.getMessage());
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
	 * ✅ Dynamic Navigation: navigates to Dashboard before/after test.
	 * Only runs when driver is responsive AND scenario is not excluded.
	 * Skipped for scenarios that manage their own navigation (Login, Logout, etc).
	 */
	private void ensureDashboardState(Scenario scenario, String phase) {
		// Scenarios that manage their own navigation — skip dashboard enforcement
		boolean isExcluded = scenario.getSourceTagNames().contains("@Login")
				|| scenario.getSourceTagNames().contains("@Dashboard")
				|| scenario.getSourceTagNames().contains("@FT")
				|| scenario.getSourceTagNames().contains("@Logout")
				|| scenario.getSourceTagNames().contains("@ChangePassword")
				|| scenario.getSourceTagNames().contains("@Feedback")
				|| scenario.getSourceTagNames().contains("@AccountEnableDisable")
				|| scenario.getSourceTagNames().contains("@ForgotPassword")
				|| scenario.getSourceTagNames().contains("@noNavigation")
				|| scenario.getSourceTagNames().contains("@NoNeedLogin");

		if (isExcluded) return;
		if (!LoginHelper.isLoggedIn()) return;

		// Only attempt navigation if driver is alive
		if (!DriverFactory.isDriverResponsive()) {
			System.out.println("⚠️ Skipping dashboard navigation (" + phase + ") — driver unresponsive.");
			return;
		}

		System.out.println("🔄 Checking app state (" + phase + " test)...");
		try {
			AndroidDriver driver = DriverFactory.getDriver();
			DashboardPage dashboard = new DashboardPage(driver);

			if (!dashboard.isDashboardVisible()) {
				System.out.println("🏠 Navigating to Dashboard...");
				HybridAppStabilizer.ensureNative(driver);
				dashboard.clickHome();
				Thread.sleep(2000);
			} else {
				System.out.println("✅ App is already on Dashboard.");
			}
		} catch (Exception e) {
			System.out.println("⚠️ Could not navigate to Dashboard: " + e.getMessage());
		}
	}
}
