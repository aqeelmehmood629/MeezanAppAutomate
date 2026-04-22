package steps;

import driver.DriverFactory;
import io.cucumber.java.*;

import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.*;
import utils.ExtentManager;
import utils.ReportManager;
import utils.ScreenshotUtil;

public class Hooks {

	private static ExtentReports extent = ExtentManager.getExtentReports();

	@Before
	public void setup(Scenario scenario) {

		// ✅ Initialize driver (singleton) – will reuse existing session if already
		// started
		DriverFactory.initializeDriver();

		// ✅ Start Extent test
		ExtentTest extentTest = extent.createTest(scenario.getName());
		ReportManager.setTest(extentTest);
	}

		@After
		public void tearDown(Scenario scenario) {

		    String scenarioName = scenario.getName().replaceAll(" ", "_");
		    String status = scenario.isFailed() ? "FAIL" : "PASS";

		    try {
		        Thread.sleep(1000);
		    } catch (Exception e) {}

		    // ✅ Screenshot
		    String screenshotPath = ScreenshotUtil.captureScreenshot(scenarioName, status);

		    // ✅ USE ReportManager (NOT local ThreadLocal)
		    if (scenario.isFailed()) {
		        ReportManager.getTest().fail("❌ Test Failed");
		    } else {
		        ReportManager.getTest().pass("✅ Test Passed");
		    }

		    // ✅ Attach screenshot
		    if (screenshotPath != null) {
		        try {
		            ReportManager.getTest()
		                    .addScreenCaptureFromPath("./" + screenshotPath);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}

	// Quit driver only once at end of suite
		@AfterSuite
	    public void tearDownSuite() {

	        extent.flush();   // ✅ correct place
	        DriverFactory.quitDriver();
	    }
}
