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

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

	// Carries the stable grouping key from @Before into @After for the same scenario row
	private static final ThreadLocal<String> currentLineKey = new ThreadLocal<>();

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

		// ✅ Step 2: Start Extent test — grouped for Scenario Outlines
		createExtentNode(scenario);

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

		// ✅ Log scenario result + track counters
		String lineKey = currentLineKey.get() != null ? currentLineKey.get() : scenario.getId();
		boolean passed = !scenario.isFailed();

		ReportManager.markTestCaseResult(lineKey, passed);

		if (scenario.isFailed()) {
			try {
				ReportManager.getTest().fail("❌ Test Case FAILED");
			} catch (Exception ignored) {}

			// 🛡️ POPUP RECOVERY: dismiss any error popup left by the failed scenario
			try {
				AndroidDriver driver = DriverFactory.getDriver();
				PopupHandler.handlePopupIfPresent(driver, "After Failed Scenario");
			} catch (Exception ignored) {}
		} else {
			try {
				ReportManager.getTest().pass("✅ Test Case PASSED");
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
			// Finalize scenario-level pass/fail counts
			ReportManager.finalizeScenarioCounts();

			// Print to console
			System.out.println("═════════════════════════════════════════");
			System.out.println("📊 EXECUTION SUMMARY");
			System.out.println("   Scenarios → Total: " + ReportManager.getTotalScenarios()
				+ "  Passed: " + ReportManager.getPassedScenarios()
				+ "  Failed: " + ReportManager.getFailedScenarios());
			System.out.println("   Test Cases → Total: " + ReportManager.getTotalTestCases()
				+ "  Passed: " + ReportManager.getPassedTestCases()
				+ "  Failed: " + ReportManager.getFailedTestCases());
			System.out.println("═════════════════════════════════════════");

			// 📊 Create a STANDALONE Execution Summary node — completely separate from scenario details.
			// Note: Extent's built-in dashboard counter will include this node (+1),
			// but our custom counts below are accurate.
			ExtentTest summary = extent.createTest("📊 Execution Summary");

			summary.info(
				"<table style='border-collapse:collapse;width:100%;font-size:14px'>"
				+ "<tr style='background:#1a7a4a;color:#fff;font-weight:bold'>"
				+ "<td style='padding:8px 14px'>Metric</td>"
				+ "<td style='padding:8px 14px;text-align:center'>Total</td>"
				+ "<td style='padding:8px 14px;text-align:center;color:#aeffca'>Passed</td>"
				+ "<td style='padding:8px 14px;text-align:center;color:#ffaaaa'>Failed</td>"
				+ "</tr>"
				+ "<tr style='background:#f4f9f6'>"
				+ "<td style='padding:8px 14px;font-weight:bold'>Scenarios</td>"
				+ "<td style='padding:8px 14px;text-align:center'><b>" + ReportManager.getTotalScenarios() + "</b></td>"
				+ "<td style='padding:8px 14px;text-align:center;color:#1a7a4a;font-weight:bold'>" + ReportManager.getPassedScenarios() + "</td>"
				+ "<td style='padding:8px 14px;text-align:center;color:#c0392b;font-weight:bold'>" + ReportManager.getFailedScenarios() + "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td style='padding:8px 14px;font-weight:bold'>Test Cases</td>"
				+ "<td style='padding:8px 14px;text-align:center'><b>" + ReportManager.getTotalTestCases() + "</b></td>"
				+ "<td style='padding:8px 14px;text-align:center;color:#1a7a4a;font-weight:bold'>" + ReportManager.getPassedTestCases() + "</td>"
				+ "<td style='padding:8px 14px;text-align:center;color:#c0392b;font-weight:bold'>" + ReportManager.getFailedTestCases() + "</td>"
				+ "</tr>"
				+ "</table>");

			ExtentManager.getExtentReports().flush();
			System.out.println("📊 Extent Report flushed with Execution Summary.");
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

		// ── Guard 1: Only for @NeedsDashboard or @Dashboard scenarios ──────────
		boolean needsDashboard = scenario.getSourceTagNames().contains("@NeedsDashboard")
				|| scenario.getSourceTagNames().contains("@Dashboard");
		if (!needsDashboard) {
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

	// ══════════════════════════════════════════════════════════════════════════
	// 🔹 Scenario Outline Detection & Grouped Extent Node Creation (FILE PARSING)
	// ══════════════════════════════════════════════════════════════════════════

	/**
	 * Builds the correct Extent node for this scenario.
	 *
	 * Cucumber 7.x ID issue: For Scenario Outlines, scenario.getId() might just return
	 * "file:///path/to/feature:line" and DOES NOT contain a semicolon with slug/row data.
	 *
	 * To reliably group Outline rows into a parent node, we must look at the ACTUAL
	 * FEATURE FILE line:
	 * 1. If the line contains a pipe `|` and is not `Examples:`, it's an outline row.
	 * 2. If it's an outline row, scan backwards in the file to find the `Scenario Outline:` title.
	 * 3. Use the line number of `Scenario Outline:` as the grouping key.
	 */
	private void createExtentNode(Scenario scenario) {
		String id = scenario.getId(); // e.g., "file:///C:/.../02_Dashboard.feature:30"
		String name = scenario.getName();

		try {
			FeatureLineInfo info = analyzeFeatureLine(scenario.getUri(), scenario.getLine());

			if (info.isOutlineRow) {
				// We found it's a row in a Scenario Outline.
				// groupingKey = featurePath:outlineTitleLineNumber
				String groupingKey = scenario.getUri().toString() + ":" + info.outlineTitleLineNum;
				currentLineKey.set(groupingKey); // Save for @After

				// Create or reuse the parent node, then create a child for this iteration
				ReportManager.createOrReuseScenarioNode(extent, groupingKey, info.outlineTitle, info.rowName);
			} else {
				// Regular standalone scenario
				currentLineKey.set(id);
				ReportManager.createSingleTest(extent, id, name);
			}

		} catch (Exception e) {
			// Fallback if file parsing fails for any reason
			System.out.println("⚠️ Warning: Could not parse feature file for Extent grouping. Falling back to single test. (" + e.getMessage() + ")");
			currentLineKey.set(id);
			ReportManager.createSingleTest(extent, id, name);
		}
	}

	private static class FeatureLineInfo {
		boolean isOutlineRow = false;
		String outlineTitle = "";
		int outlineTitleLineNum = -1;
		String rowName = "";
	}

	/**
	 * Reads the feature file from disk to determine if the given line is an Examples row,
	 * and if so, finds the parent Scenario Outline title.
	 */
	private FeatureLineInfo analyzeFeatureLine(URI featureUri, Integer targetLineNum) throws Exception {
		FeatureLineInfo info = new FeatureLineInfo();

		if (featureUri == null || targetLineNum == null || targetLineNum <= 0) {
			return info;
		}

		List<String> lines = Files.readAllLines(Paths.get(featureUri));
		if (targetLineNum > lines.size()) {
			return info;
		}

		// targetLineNum is 1-based, list is 0-based
		String targetLine = lines.get(targetLineNum - 1).trim();

		// Check if the current line is a data row (contains | )
		if (targetLine.startsWith("|") && targetLine.endsWith("|")) {
			// It is an Examples row!
			info.isOutlineRow = true;
			info.rowName = extractFirstPipeCell(targetLine); // Use the first column as the iteration label

			// Now scan backwards to find "Scenario Outline:" or "Scenario Template:"
			for (int i = targetLineNum - 2; i >= 0; i--) {
				String line = lines.get(i).trim();
				if (line.startsWith("Scenario Outline:") || line.startsWith("Scenario Template:")) {
					info.outlineTitleLineNum = i + 1; // 1-based line number
					info.outlineTitle = line.substring(line.indexOf(":") + 1).trim();
					break;
				}
			}
		}

		return info;
	}

	/**
	 * Extracts the first data cell from a markdown table row.
	 * e.g., "| Copy Account Number | account number |" -> "Copy Account Number"
	 */
	private String extractFirstPipeCell(String line) {
		String[] parts = line.split("\\|");
		if (parts.length > 1) {
			return parts[1].trim(); // parts[0] is empty before the first pipe
		}
		return "Iteration";
	}
}
