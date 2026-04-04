package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;

	public synchronized static ExtentReports getExtentReports() {

		if (extent == null) {

			ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");

			spark.config().setDocumentTitle("Automation Test Report");
			spark.config().setReportName("Appium + Cucumber Test Results");
			spark.config().setTheme(Theme.STANDARD);

			extent = new ExtentReports();
			extent.attachReporter(spark);

			// Optional system info
			extent.setSystemInfo("Tester", "Aqeel Mehmood");
			extent.setSystemInfo("Device", "Android Device");
			extent.setSystemInfo("Platform", "Android");
			extent.setSystemInfo("Automation Tool", "Appium");
		}

		return extent;
	}
}