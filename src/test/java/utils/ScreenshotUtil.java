package utils;

import driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

	/**
	 * ✅ Captures screenshot ONLY on failure.
	 * Guards against dead driver sessions — returns null gracefully instead of throwing.
	 */
	public static String captureScreenshot(String testName, String status) {
		try {
			// ✅ Guard: do NOT attempt screenshot if driver is dead (prevents @After crash)
			if (!DriverFactory.isDriverResponsive()) {
				System.out.println("⚠️ Screenshot skipped — driver session is unresponsive.");
				return null;
			}

			WebDriver driver = DriverFactory.getDriver();

			if (driver == null) {
				System.out.println("⚠️ Screenshot skipped — driver is null.");
				return null;
			}

			// Sanitize filename — remove special chars
			String safeName = testName.replaceAll("[^a-zA-Z0-9_]", "_");
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = safeName + "_" + status + "_" + timeStamp + ".png";

			String folderPath = System.getProperty("user.dir") + "/reports/screenshots/";
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File dest = new File(folderPath + fileName);
			FileHandler.copy(src, dest);

			System.out.println("📸 Screenshot captured: " + dest.getAbsolutePath());
			return dest.getAbsolutePath();

		} catch (Exception e) {
			System.out.println("⚠️ Screenshot capture failed (non-fatal): " + e.getMessage());
			return null;
		}
	}
}
