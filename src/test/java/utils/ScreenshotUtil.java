package utils;

import driver.DriverFactory; // ✅ CHANGE
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

	public static String captureScreenshot(String testName, String status) {

		try {

			WebDriver driver = DriverFactory.getDriver(); // ✅ FIX

			if (driver == null) {
				System.out.println("Driver is NULL, screenshot skipped");
				return null;
			}

			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

			String fileName = testName + "_" + status + "_" + timeStamp + ".png";

			String folderPath = System.getProperty("user.dir") + "/reports/screenshots/";

			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File dest = new File(folderPath + fileName);

			FileHandler.copy(src, dest);

			return dest.getAbsolutePath();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}