package steps;

import driver.DriverFactory;
import io.cucumber.java.*;

import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.*;
import utils.ExtentManager;
import utils.ScreenshotUtil;

public class Hooks {

	private static ExtentReports extent = ExtentManager.getExtentReports();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@Before
	public void setup(Scenario scenario) {

		// ✅ Initialize driver (singleton) – will reuse existing session if already
		// started
		DriverFactory.initializeDriver();

		// ✅ Start Extent test
		ExtentTest extentTest = extent.createTest(scenario.getName());
		test.set(extentTest);
	}

	@After
	public void tearDown(Scenario scenario) {

		String scenarioName = scenario.getName().replaceAll(" ", "_");
		String status = scenario.isFailed() ? "FAIL" : "PASS";

		// ✅ Capture screenshot
		String screenshotPath = ScreenshotUtil.captureScreenshot(scenarioName, status);

		if (scenario.isFailed()) {
			test.get().fail("❌ Test Failed");
		} else {
			test.get().pass("✅ Test Passed");
		}

		// ✅ Add screenshot if captured
		if (screenshotPath != null) {
			try {
				test.get().addScreenCaptureFromPath(screenshotPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// ✅ Flush report
		extent.flush();

		// ❌ Do NOT quit driver here to allow session reuse
		// DriverFactory.quitDriver(); <-- Removed
	}

	// ✅ Quit driver only once at end of suite
	@AfterSuite
	public void tearDownSuite() {
		DriverFactory.quitDriver();
	}
}