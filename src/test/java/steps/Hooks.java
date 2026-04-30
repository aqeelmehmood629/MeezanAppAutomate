package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.*;

import com.aventstack.extentreports.*;
import utils.ExtentManager;
import utils.ReportManager;
import utils.ScreenshotUtil;

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
}
