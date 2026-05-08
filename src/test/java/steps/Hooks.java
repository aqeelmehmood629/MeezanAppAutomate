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
	 * - Ensures driver is alive (reuses existing session)
	 * - Creates Extent report test entry
	 * - Does NOT quit/restart driver between scenarios
	 */
	@Before
	public void setup(Scenario scenario) {

		// ✅ Initialize driver (singleton) – will reuse existing session
		DriverFactory.initializeDriver();

		// ✅ Start Extent test
		ExtentTest extentTest = extent.createTest(scenario.getName());
		ReportManager.setTest(extentTest);

		System.out.println("═══════════════════════════════════════════");
		System.out.println("▶️ STARTING SCENARIO: " + scenario.getName());
		System.out.println("═══════════════════════════════════════════");
		
		// ✅ Ensure we start on Dashboard before the test case runs
		ensureDashboardState(scenario, "before");
	}

	/**
	 * ✅ @After: Runs after EVERY scenario
	 * - Captures screenshot on failure
	 * - Logs pass/fail to Extent report
	 * - Does NOT quit driver (session persists for next scenario)
	 */
	@After
	public void tearDown(Scenario scenario) {

		String scenarioName = scenario.getName().replaceAll(" ", "_");

		try {
			Thread.sleep(1000);
		} catch (Exception e) {}

		if (scenario.isFailed()) {

			ReportManager.getTest().fail("❌ Test Failed");

			try {
				String screenshotPath = ScreenshotUtil.captureScreenshot(scenarioName, "FAIL");

				if (screenshotPath != null) {
					ReportManager.getTest()
							.addScreenCaptureFromPath("./" + screenshotPath);
				}

			} catch (Exception e) {
				System.out.println("⚠️ Screenshot blocked (secure screen)");
			}

		} else {
			ReportManager.getTest().pass("✅ Test Passed");
		}

		// ✅ Ensure we navigate back to Dashboard after the test case finishes
		ensureDashboardState(scenario, "after");

		System.out.println("═══════════════════════════════════════════");
		System.out.println("⏹️ FINISHED SCENARIO: " + scenario.getName()
				+ " → " + scenario.getStatus());
		System.out.println("═══════════════════════════════════════════");

		// ✅ Do NOT quit driver here — session persists for next scenario
	}

	/**
	 * ✅ @AfterAll: Runs ONCE after ALL scenarios are done
	 * - Quits driver
	 * - Flushes Extent report
	 */
	@AfterAll
	public static void afterAll() {

		System.out.println("🛑 All scenarios finished — cleaning up...");

		// ✅ Quit driver ONCE at the very end
		DriverFactory.quitDriver();

		// ✅ Flush Extent report
		ExtentManager.getExtentReports().flush();
		System.out.println("📊 Extent Report flushed");
	}

	// Driver quit is handled in @AfterAll for independence

	/**
	 * ✅ Dynamic Navigation Logic:
	 * Automatically navigates to the Dashboard before/after tests 
	 * (excluding Login, Dashboard, and Logout tests).
	 */
	private void ensureDashboardState(Scenario scenario, String phase) {
		boolean isExcluded = scenario.getSourceTagNames().contains("@Login") || 
							 scenario.getSourceTagNames().contains("@Dashboard") ||
							 scenario.getSourceTagNames().contains("@FT") || 
							 scenario.getSourceTagNames().contains("@Logout") ||
							 scenario.getSourceTagNames().contains("@ChangePassword") ||
							 scenario.getSourceTagNames().contains("@Feedback") ||
							 scenario.getSourceTagNames().contains("@AccountEnableDisable");
		
		if (!isExcluded && LoginHelper.isLoggedIn()) {
			System.out.println("🔄 Checking app state (" + phase + " test)...");
			try {
				DashboardPage dashboard = new DashboardPage(DriverFactory.getDriver());
				
				// isDashboardVisible() automatically switches to NATIVE context
				if (!dashboard.isDashboardVisible()) {
					System.out.println("🏠 Navigating to Dashboard...");
					HybridAppStabilizer.ensureNative(DriverFactory.getDriver());
					dashboard.clickHome();
					Thread.sleep(2000); // Wait for transition
				} else {
					System.out.println("✅ App is already on Dashboard.");
				}
			} catch (Exception e) {
				System.out.println("⚠️ Could not navigate to Dashboard: " + e.getMessage());
			}
		}
	}
}
